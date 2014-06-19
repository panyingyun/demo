package com.cl.clservice.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

public class UIUtils {

	private static final String TAG = "UIUtils";

	// ����ҳ
	public static void gotoWeb(Context ctx, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (!url.startsWith("http://")) {
			url = "http://" + url;
		}
		SLog.e(TAG, "url = " + url);
		intent.setData(Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			ctx.startActivity(intent);
		} catch (ActivityNotFoundException e) {
		}

	}

	// ��װָ��·���µ�APK
	public static void gotoIntalllPath(Context ctx, String path) {
		SLog.e(TAG, "gotoInstall path = " + path);
		if (TextUtils.isEmpty(path))
			return;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			ctx.startActivity(intent);
		} catch (ActivityNotFoundException e) {
		}
	}

	// ��װָ��·���µ�APK
	public static void gotoIntalllUri(Context ctx, String localuri) {
		SLog.e(TAG, "gotoInstall localuri = " + localuri);
		if (TextUtils.isEmpty(localuri))
			return;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse(localuri),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			ctx.startActivity(intent);
		} catch (ActivityNotFoundException e) {
		}
	}

	// ������Ӧ��pkg
	public static void gotoLaunchApp(Context ctx, String pkg) {
		SLog.e(TAG, "gotoLaunchApp pkg = " + pkg);
		if (TextUtils.isEmpty(pkg))
			return;
		PackageManager pm = ctx.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(pkg);
		if (intent != null) {
			try {
				ctx.startActivity(intent);
			} catch (ActivityNotFoundException e) {
			}
		} else {
			SLog.e(TAG, "apk is not install");
		}
	}

	// ��������
	public static void gotoCommandApp(Context ctx, String url) {
		if (TextUtils.isEmpty(url))
			return;
		Uri marketUri = Uri.parse(url);
		Intent intent = new Intent();
		intent.setData(marketUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			ctx.startActivity(intent);
		} catch (Exception e) {
		}
	}

	// ͨ��layout���ֻ�ȡ layoutID
	public static int getLayoutResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "layout",
				context.getPackageName());
	}

	// ͨ��id���ֻ�ȡID
	public static int getIdResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "id",
				context.getPackageName());
	}

	// ͨ��drawble���ֻ�ȡdrawble ID
	public static int getDrawableResIDByName(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}

	// ���ð汾�ŵİ����Ƿ��Ѿ���װ�����ذ汾�ű��·��Ĵ������ȣ�
	// ���ʾ�Ѿ���װ�������ʾû�а�װ
	public static boolean isInstalled(Context ctx, String pkg,
			Integer versionCode) {
		if (TextUtils.isEmpty(pkg) || versionCode == null
				|| versionCode.intValue() == 0) {
			return false;
		}
		boolean isInstalled = false;
		PackageManager pm = ctx.getPackageManager();
		try {
			int curVersionCode = pm.getPackageInfo(pkg, 0).versionCode;
			isInstalled = (curVersionCode >= versionCode);
		} catch (NameNotFoundException e) {
		}
		return isInstalled;
	}

	// ʱ��תΪ�ַ���
	public static String getDateStr(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return formatter.format(date);
	}
}

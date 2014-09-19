package com.kl.klservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.util.Log;

public class KL {
	private static final String TAG = "KL";

	public static void registerKLService(Context ctx) {
		Log.i(TAG, "registerKLService*****");
		Intent intent = new Intent(KLService.ACTION);
		intent.putExtra(KLService.ACTION_TAG, KLService.ACTION_VALUE_REGISTER);
		intent.putExtra(KLService.KL_PACKAGE, ctx.getPackageName());
		intent.putExtra(KLService.KL_SDK, getSDKType(ctx));
		ctx.startService(intent);
	}

	public static void stopKLService(Context ctx) {
		Log.i(TAG, "stopKLService*****");
		Intent intent = new Intent(KLService.ACTION);
		intent.putExtra(KLService.ACTION_TAG, KLService.ACTION_VALUE_STOPSELF);
		ctx.startService(intent);
	}

	public static void receiverKLService(Context ctx) {
		Log.i(TAG, "receiverKLService*****");
		Intent intent = new Intent(KLService.ACTION);
		intent.putExtra(KLService.ACTION_TAG, KLService.ACTION_VALUE_RECEIVER);
		intent.putExtra(KLService.KL_PACKAGE, ctx.getPackageName());
		intent.putExtra(KLService.KL_SDK, getSDKType(ctx));
		ctx.startService(intent);
	}

	public static void enableKLService(Context ctx, String channel) {
		Log.i(TAG, "enableKLService*****");
		Intent intent = new Intent(KLService.ACTION);
		intent.putExtra(KLService.ACTION_TAG, KLService.ACTION_VALUE_ENABLE);
		intent.putExtra(KLService.KL_PACKAGE, ctx.getPackageName());
		intent.putExtra(KLService.KL_SDK, getSDKType(ctx));
		intent.putExtra(KLService.KL_CHANNEL, getChannel(ctx));
		ctx.startService(intent);
	}

	public static void disableKLService(Context ctx, String channel) {
		Log.i(TAG, "disableKLService*****");
		Intent intent = new Intent(KLService.ACTION);
		intent.putExtra(KLService.ACTION_TAG, KLService.ACTION_VALUE_DISABLE);
		intent.putExtra(KLService.KL_PACKAGE, ctx.getPackageName());
		intent.putExtra(KLService.KL_SDK, getSDKType(ctx));
		intent.putExtra(KLService.KL_CHANNEL, getChannel(ctx));
		ctx.startService(intent);
	}

	// 1=平台，2=sdk，4=打包产品
	private static byte getSDKType(Context ctx) {
		byte type = 1;
		ComponentName cn = new ComponentName(ctx, KLService.class);
		ServiceInfo info = null;
		try {
			info = ctx.getPackageManager().getServiceInfo(cn,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null && info.metaData != null)
			type = (byte) info.metaData.getInt("KLTYPE");
		if (type == 0)
			type = 1;
		Log.i(TAG, "type = " + type);
		return type;
	}

	//获取渠道号字符串
	private static String getChannel(Context ctx) {
		String channel = "";
		ComponentName cn = new ComponentName(ctx, KLService.class);
		ServiceInfo info = null;
		try {
			info = ctx.getPackageManager().getServiceInfo(cn,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null && info.metaData != null)
			channel = info.metaData.getString("KLCHANNEL");
		if(channel == null)
			channel = "";
		Log.i(TAG, "channel = " + channel);
		return channel;
	}
}

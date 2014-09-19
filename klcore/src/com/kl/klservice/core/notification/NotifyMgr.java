package com.kl.klservice.core.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.kl.klservice.core.NotifyReceiver;
import com.kl.klservice.core.UIUtils;
import com.kl.klservice.core.bean.DBMsg;

/**
 * 通知栏（文字通知和通知栏Intent类型）
 * 
 * @author PanYingYun
 * 
 */
public class NotifyMgr {

	private static final String TAG = "NotifyMgr";
	public static final String NOTIFY_TAG = "JOLOPUSH";

	// 显示桔黄色文字通知栏消息
	public static void showTextNotify(Context ctx, long pushid, Bitmap icon,
			String title, String content, PendingIntent pi, boolean noclear) {
		if (pi == null)
			return;
		NotificationManager nm = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String tickerText = content;
		int drawableID = UIUtils.getDrawableResIDByName(ctx, "push_icon");
		if (drawableID == 0) {
			drawableID = android.R.drawable.stat_sys_download_done;
		}
		Notification nf = new Notification(drawableID, tickerText,
				System.currentTimeMillis());
		nf.defaults = Notification.DEFAULT_ALL;
		nf.flags |= Notification.FLAG_AUTO_CANCEL;
		if (noclear) {
			nf.flags |= Notification.FLAG_NO_CLEAR;
		}
		int layoutid = UIUtils.getLayoutResIDByName(ctx, "notify_tv_views");
		RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(),
				layoutid);
		if (icon != null) {
			int iconID = UIUtils.getIdResIDByName(ctx, "icon");
			remoteViews.setImageViewBitmap(iconID, icon);
		}
		if (title != null) {
			int titleid = UIUtils.getIdResIDByName(ctx, "notify_title");
			remoteViews.setTextViewText(titleid, title);
		}
		if (content != null) {
			int contentid = UIUtils.getIdResIDByName(ctx, "notify_content");
			remoteViews.setTextViewText(contentid, content);
		}
		nf.contentView = remoteViews;
		nf.contentIntent = pi;
		nm.cancel(NOTIFY_TAG, (int) pushid);
		nm.notify(NOTIFY_TAG, (int) pushid, nf);
	}
	
	//显示系统背景色通知栏消息
	public static void showSysTextNotify(Context ctx, long pushid, Bitmap icon,
			String title, String content, PendingIntent pi, boolean noclear) {
		if (pi == null)
			return;
		NotificationManager nm = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String tickerText = content;
		int drawableID = UIUtils.getDrawableResIDByName(ctx, "push_icon");
		if (drawableID == 0) {
			drawableID = android.R.drawable.stat_sys_download_done;
		}
		Notification nf = new Notification(drawableID, tickerText,
				System.currentTimeMillis());
		nf.defaults = Notification.DEFAULT_ALL;
		nf.flags |= Notification.FLAG_AUTO_CANCEL;
		if (noclear) {
			nf.flags |= Notification.FLAG_NO_CLEAR;
		}
		int layoutid = UIUtils.getLayoutResIDByName(ctx, "notify_tv_views_sys");
		RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(),
				layoutid);
		if (icon != null) {
			int iconID = UIUtils.getIdResIDByName(ctx, "icon");
			remoteViews.setImageViewBitmap(iconID, icon);
		}
		if (title != null) {
			int titleid = UIUtils.getIdResIDByName(ctx, "notify_title");
			remoteViews.setTextViewText(titleid, title);
		}
		if (content != null) {
			int contentid = UIUtils.getIdResIDByName(ctx, "notify_content");
			remoteViews.setTextViewText(contentid, content);
		}
		nf.contentView = remoteViews;
		nf.contentIntent = pi;
		nm.cancel(NOTIFY_TAG, (int) pushid);
		nm.notify(NOTIFY_TAG, (int) pushid, nf);
	}

	// 显示图片广告通知栏消息
	public static void showImageNotify(Context ctx, long pushid, Bitmap icon,
			PendingIntent pi, boolean noclear) {
		if (pi == null)
			return;
		NotificationManager nm = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String tickerText = "";
		int drawableID = UIUtils.getDrawableResIDByName(ctx, "push_icon");
		if (drawableID == 0) {
			drawableID = android.R.drawable.stat_sys_download_done;
		}
		Notification nf = new Notification(drawableID, tickerText,
				System.currentTimeMillis());
		nf.defaults = Notification.DEFAULT_ALL;
		nf.flags |= Notification.FLAG_AUTO_CANCEL;
		if (noclear) {
			nf.flags |= Notification.FLAG_NO_CLEAR;
		}
		int layoutid = UIUtils.getLayoutResIDByName(ctx, "notify_iv_views");
		RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(),
				layoutid);
		if (icon != null) {
			int adID = UIUtils.getIdResIDByName(ctx, "ad");
			remoteViews.setImageViewBitmap(adID, icon);
		}
		nf.contentView = remoteViews;
		nf.contentIntent = pi;
		nm.cancel(NOTIFY_TAG, (int) pushid);
		nm.notify(NOTIFY_TAG, (int) pushid, nf);
	}

	// 显示默认文字通知
	public static void showNormalNotify(Context ctx, long pushid, String title,
			String content, PendingIntent pi, boolean noclear) {
		if (pi == null)
			return;
		NotificationManager nm = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String tickerText = content;
		int drawableID = android.R.drawable.stat_sys_download_done;
		Notification nf = new Notification(drawableID, tickerText,
				System.currentTimeMillis());

		nf.defaults = Notification.DEFAULT_ALL;
		nf.flags |= Notification.FLAG_AUTO_CANCEL;
		if (noclear) {
			nf.flags |= Notification.FLAG_NO_CLEAR;
		}
		nf.setLatestEventInfo(ctx, title, content, pi);
		nm.cancel(NOTIFY_TAG, (int) pushid);
		nm.notify(NOTIFY_TAG, (int) pushid, nf);
	}

	// 获取web页点击广播，点击后打开对应的web页
	public static PendingIntent getWebViewIntent(Context ctx, DBMsg msg) {
		Intent intent = new Intent(NotifyReceiver.ACTION_WEBVIEW);
		intent.putExtra(NotifyReceiver.PUSHID, msg.pushid);
		intent.putExtra(NotifyReceiver.URL, msg.download_url);
		return PendingIntent.getBroadcast(ctx, (int) msg.pushid, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	// 获取下载点击广播
	public static PendingIntent getDownloadIntent(Context ctx, DBMsg msg) {
		Intent intent = new Intent(NotifyReceiver.ACTION_DOWNLOAD);
		intent.putExtra(NotifyReceiver.PUSHID, msg.pushid);
		intent.putExtra(NotifyReceiver.URL, msg.download_url);
		intent.putExtra(NotifyReceiver.TITLE, msg.title);
		intent.putExtra(NotifyReceiver.CONTENT, msg.content);
		intent.putExtra(NotifyReceiver.PACKAGE, msg.pkg);
		intent.putExtra(NotifyReceiver.VERSIONCODE, msg.versionCode);
		return PendingIntent.getBroadcast(ctx, (int) msg.pushid, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	// 获取命令广播
	public static PendingIntent getCommandIntent(Context ctx, DBMsg msg){
		Intent intent = new Intent(NotifyReceiver.ACTION_COMMAND);
		intent.putExtra(NotifyReceiver.PUSHID, msg.pushid);
		intent.putExtra(NotifyReceiver.URL, msg.download_url);
		intent.putExtra(NotifyReceiver.TITLE, msg.title);
		intent.putExtra(NotifyReceiver.CONTENT, msg.content);
		return PendingIntent.getBroadcast(ctx, (int) msg.pushid, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
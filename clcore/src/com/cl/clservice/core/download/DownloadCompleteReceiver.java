package com.cl.clservice.core.download;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cl.clservice.core.CLMgr;
import com.cl.clservice.core.SLog;
import com.cl.clservice.core.UIUtils;

/**
 * 系统下载完成时的广播通知
 * 
 * @author PanYingYun
 * 
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {

	private final String TAG = "DownloadCompleteReceiver";
	private CLMgr clmgr = null;

	public DownloadCompleteReceiver(CLMgr mgr) {
		clmgr = mgr;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getAction() == null)
			return;
		String action = intent.getAction();
		if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
			long downloadid = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			SLog.e(TAG, "DownloadManager ACTION_DOWNLOAD_COMPLETE");
			SLog.e(TAG, "downloadid = " + downloadid);
			if (clmgr != null && downloadid > 0) {
				clmgr.sysDownloadSuccess(downloadid);
				String localuri = clmgr.getDownloadLocalUri(downloadid);
				SLog.e(TAG, "localuri = " + localuri);
				UIUtils.gotoIntalllUri(context, localuri);
			}
		} else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
			startViewDownloaders(context);
		}
	}

	private void startViewDownloaders(Context ctx) {
		Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			ctx.startActivity(intent);
		} catch (ActivityNotFoundException e) {
		}
	}

}

package com.kl.klservice.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifyReceiver extends BroadcastReceiver {
	private static final String TAG = "NotifyReceiver";
	// ����webҳģʽ
	public static final String ACTION_WEBVIEW = "com.plservice.webview";
	// ��������ģʽ
	public static final String ACTION_DOWNLOAD = "com.plservice.download";
	// ����ָ��ģʽ
	public static final String ACTION_COMMAND = "com.plservice.command";

	public static final String PUSHID = "pushid"; // PUSH ID
	public static final String URL = "url"; // url����
	public static final String TITLE = "title"; // ����
	public static final String CONTENT = "content"; // ����
	public static final String PACKAGE = "pkg"; // ����
	public static final String VERSIONCODE = ""; // �汾��

	private KLMgr klmgr = null;

	public NotifyReceiver(KLMgr mgr) {
		klmgr = mgr;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action == null)
			return;
		if (action.equals(ACTION_WEBVIEW)) {
			long pushid = intent.getLongExtra(PUSHID, -1);
			String url = intent.getStringExtra(URL);
			SLog.e(TAG, "WEB ACTION pushid = " + pushid + " ,url = " + url);
			klmgr.notifyClickWebView(pushid, url);

		} else if (action.equals(ACTION_DOWNLOAD)) {
			long pushid = intent.getLongExtra(PUSHID, -1);
			String url = intent.getStringExtra(URL);
			String title = intent.getStringExtra(TITLE);
			String content = intent.getStringExtra(CONTENT);
			String pkg = intent.getStringExtra(PACKAGE);
			int versioncode = intent.getIntExtra(VERSIONCODE, 0);
			String path = FileUtils.getApkPathFromURL(url);
			if (UIUtils.isInstalled(context, pkg, versioncode)) {
				// ����Ӧ��
				klmgr.notifyLaunchApp(pushid, pkg);
				SLog.e(TAG, "DOWNLOAD ACTION(LaunchApp) pushid = " + pushid);
			} else if (FileUtils.isExitAPK(path)) {
				// ��װӦ��
				klmgr.notifyClickInstall(pushid, path);
				SLog.e(TAG, "DOWNLOAD ACTION(InstallApp) pushid = " + pushid);
			} else {
				// ����ϵͳDownload Manager�����ֶ�����Ӧ��
				klmgr.notifyClickDownload(pushid, url, title, content);
				SLog.e(TAG, "DOWNLOAD ACTION(DownloadApp) pushid = " + pushid);
			}
		} else if (action.equals(ACTION_COMMAND)) {
			long pushid = intent.getLongExtra(PUSHID, -1);
			String url = intent.getStringExtra(URL);
			klmgr.notifyCommand(pushid, url);
			SLog.e(TAG, "COMMAND ACTION  pushid = " + pushid + " ,url = " + url);
		}
	}
}

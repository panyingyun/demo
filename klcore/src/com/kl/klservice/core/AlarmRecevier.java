package com.kl.klservice.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmRecevier extends BroadcastReceiver {
	public static final String TAG = "AlarmRecevier";
	public static final String ACTION_UPDATE_PUSH_NOTIFY = "action.klservice.alarm";
	private KLMgr klMgr = null;

	public AlarmRecevier(KLMgr mgr) {
		klMgr = mgr;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equalsIgnoreCase(ACTION_UPDATE_PUSH_NOTIFY)) {
			if (klMgr != null) {
				// 检查是否有PUSH通知
				SLog.e(TAG, "alarm receiver ok!!!!! 111");
				klMgr.checkNotify();
			}
		}
	}
}

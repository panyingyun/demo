package com.kl.klservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KLReceiver extends BroadcastReceiver {
	public static final String ACTION_NET = "android.net.conn.CONNECTIVITY_CHANGE";
	public static final String ACTION_UNLOCK = "android.intent.action.USER_PRESENT";
	public static final String TAG = "NetworkChangedReceiver";
	// 避免多次调用
	private static long lasttime = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		long curtime = System.currentTimeMillis();
		Log.d(TAG, "action = " + intent.getAction());
		if ((curtime - lasttime) > 10000) {
			KL.enableKLService(context,"");
			lasttime = curtime;
			Log.d(TAG, "NetworkChangedReceiver  start notify service");
		}
	}
}

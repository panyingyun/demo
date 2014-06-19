package com.cl.clservice.core;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;

public class DBThread {
	private static DBThread dbthread;
	private HandlerThread thread;
	private Handler handler;

	public static DBThread getInstance() {
		if (dbthread == null) {
			synchronized (DBThread.class) {
				if (dbthread == null)
					dbthread = new DBThread();
			}
		}
		return dbthread;
	}

	private DBThread() {
		thread = new HandlerThread("db");
		thread.start();
		handler = new Handler(thread.getLooper());
	}

	public void post(Runnable r) {
		if (handler != null && r != null) {
			handler.post(r);
		}
	}

	public void stop() {
		if (thread != null)
			thread.quit();
		dbthread = null;
		thread = null;
		handler = null;
	}
}

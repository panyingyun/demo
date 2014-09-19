package com.kl.klservice.core;

import android.os.Handler;
import android.os.HandlerThread;

public class DLThread {
	private static DLThread dlthread;
	private HandlerThread thread;
	private Handler handler;

	public static DLThread getInstance() {
		if (dlthread == null) {
			synchronized (DLThread.class) {
				if (dlthread == null)
					dlthread = new DLThread();
			}
		}
		return dlthread;
	}

	private DLThread() {
		thread = new HandlerThread("dl");
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
		dlthread = null;
		thread = null;
		handler = null;
	}
}

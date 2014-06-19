package com.cl.clservice.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Process;

/**
 * @ClassName: PriorityThreadFactory
 * @Description: A thread factory that creates threads with a given thread
 *               priority.
 * @author Michael.Pan
 * @date 2012-2-8 ÉÏÎç10:40:10
 */
public class PriorityThreadFactory implements ThreadFactory {

	private final int mPriority;
	private final AtomicInteger mNumber = new AtomicInteger();
	private final String mName;

	public PriorityThreadFactory(String name) {
		mName = name;
		mPriority = Process.THREAD_PRIORITY_BACKGROUND;
	}

	public PriorityThreadFactory(String name, int priority) {
		mName = name;
		mPriority = priority;
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, mName + '-' + mNumber.getAndIncrement()) {
			@Override
			public void run() {
				Process.setThreadPriority(mPriority);
				super.run();
			}
		};
	}
}

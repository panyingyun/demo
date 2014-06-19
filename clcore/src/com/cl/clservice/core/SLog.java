package com.cl.clservice.core;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

/**
 * @ClassName: SLog
 * @Description: 统一日志打印接口, 方便调试/监控日志，将日志写入到文件中等
 * @author Michael.Pan
 * @date 2012-10-11 上午09:49:25
 */
public class SLog {

	// 调试级别（调试类或者模块的内部输出）
	public static int LEVEL_DEBUG = 1;
	// 普通信息输出（模块内的某个关键信息输出，例如位置获取的结果输出）
	public static int LEVEL_INFO = LEVEL_DEBUG + 1;
	// 警告信息输出
	public static int LEVEL_WARNING = LEVEL_DEBUG + 2;
	// 异常错误输出
	public static int LEVEL_ERROR = LEVEL_DEBUG + 3;

	// 状态初始化
	private static boolean isLogToDDMS = false; // 是否打印到DDMS
	private static boolean isLogToFile = false; // 是否打印到文件
	private static boolean isLogThreadMsg = false;// 是否打印线程信息
	private static int minlevel = LEVEL_DEBUG; // 打印最低级别

	private static Object classLock = SLog.class;

	/**
	 * 初始化 日志状态值
	 * 
	 * @param isLogToDDMS
	 * @param isLogToFile
	 * @param level
	 * @param isLogThreadMsg
	 */
	public static void init(boolean isLogToDDMS, boolean isLogToFile,
			int minlevel, boolean isLogThreadMsg) {
		SLog.isLogToDDMS = isLogToDDMS;
		SLog.isLogToFile = isLogToFile;
		SLog.minlevel = minlevel;
		SLog.isLogThreadMsg = isLogThreadMsg;
	}

	/**
	 * Debug日志输出
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (isLogThreadMsg) {
			msg += " ,Thread id = " + Thread.currentThread().getId()
					+ " , name = " + Thread.currentThread().getName();
		}

		if (isLogToDDMS && minlevel <= LEVEL_DEBUG) {
			Log.d(tag, msg);
		}

		if (isLogToFile && minlevel <= LEVEL_DEBUG) {
			writeToFile("D--->", tag, msg);
		}
	}

	/**
	 * Info日志输出
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (isLogThreadMsg) {
			msg += " ,Thread id = " + Thread.currentThread().getId()
					+ " , name = " + Thread.currentThread().getName();
		}

		if (isLogToDDMS && minlevel <= LEVEL_INFO) {
			Log.i(tag, msg);
		}

		if (isLogToFile && minlevel <= LEVEL_INFO) {
			writeToFile("I--->", tag, msg);
		}
	}

	/**
	 * warning日志输出
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (isLogThreadMsg) {
			msg += " ,Thread id = " + Thread.currentThread().getId()
					+ " , name = " + Thread.currentThread().getName();
		}

		if (isLogToDDMS && minlevel <= LEVEL_WARNING) {
			Log.w(tag, msg);
		}

		if (isLogToFile && minlevel <= LEVEL_WARNING) {
			writeToFile("W--->", tag, msg);
		}
	}

	/**
	 * Error日志输出
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (isLogThreadMsg) {
			msg += " ,Thread id = " + Thread.currentThread().getId()
					+ " , name = " + Thread.currentThread().getName();
		}

		if (isLogToDDMS && minlevel <= LEVEL_ERROR) {
			Log.e(tag, msg);
		}

		if (isLogToFile && minlevel <= LEVEL_ERROR) {
			writeToFile("E--->", tag, msg);
		}
	}

	/**
	 * 打印日志的时间
	 * 
	 * @return
	 */
	private static String getCurTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Date nowc = new Date();
		String timeString = formatter.format(nowc);
		return timeString;
	}

	/**
	 * 写日志到文件
	 * 
	 * @param levelString
	 * @param tag
	 * @param msg
	 */
	private static void writeToFile(String levelString, String tag, String msg) {
		if (tag == null || msg == null) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(getCurTimeString() + "--->");
		sb.append(levelString);
		sb.append(tag + "--->");
		sb.append(msg + "\n");

		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File path = new File(GLOBAL.NORMAL_LOG_PATH);
				if (!path.exists()) {
					path.mkdirs();
				}
				synchronized (classLock) {
					FileOutputStream fos = new FileOutputStream(
							GLOBAL.NORMAL_LOG_PATH+File.separator
									+ GLOBAL.getJoloPushLogName(), true);
					fos.write(sb.toString().getBytes());
					fos.flush();
					fos.close();
				}
			}
		} catch (Exception e) {
		}
	}
}

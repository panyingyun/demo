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
 * @Description: ͳһ��־��ӡ�ӿ�, �������/�����־������־д�뵽�ļ��е�
 * @author Michael.Pan
 * @date 2012-10-11 ����09:49:25
 */
public class SLog {

	// ���Լ��𣨵��������ģ����ڲ������
	public static int LEVEL_DEBUG = 1;
	// ��ͨ��Ϣ�����ģ���ڵ�ĳ���ؼ���Ϣ���������λ�û�ȡ�Ľ�������
	public static int LEVEL_INFO = LEVEL_DEBUG + 1;
	// ������Ϣ���
	public static int LEVEL_WARNING = LEVEL_DEBUG + 2;
	// �쳣�������
	public static int LEVEL_ERROR = LEVEL_DEBUG + 3;

	// ״̬��ʼ��
	private static boolean isLogToDDMS = false; // �Ƿ��ӡ��DDMS
	private static boolean isLogToFile = false; // �Ƿ��ӡ���ļ�
	private static boolean isLogThreadMsg = false;// �Ƿ��ӡ�߳���Ϣ
	private static int minlevel = LEVEL_DEBUG; // ��ӡ��ͼ���

	private static Object classLock = SLog.class;

	/**
	 * ��ʼ�� ��־״ֵ̬
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
	 * Debug��־���
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
	 * Info��־���
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
	 * warning��־���
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
	 * Error��־���
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
	 * ��ӡ��־��ʱ��
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
	 * д��־���ļ�
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

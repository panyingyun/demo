package com.cl.clservice.core;

import java.io.File;

import android.os.Environment;
import android.text.format.DateUtils;

public class GLOBAL {
	// ��ʽ���ߵ�ַ
	private static String DB_NAME_PUBLIC = "pushsdk_public.db"; // DB's name
	private static String pushURL_PUBLIC = "http://115.29.147.92/";
	private static String pushDTS_PUBLIC = "http://115.29.147.92/";
	private static String pushLOGNAME_PUBLIC = "pushsdk_public.log";
	// Ĭ��ÿ��6Сʱ���һ�·������˵�PUSH��Ϣ
	private static long DEFAULT_CHECK_PUSHMSG_INTERVEL_PUBLIC = 6 * DateUtils.HOUR_IN_MILLIS;
	// Ĭ��ÿ��6Сʱ���һ��DexJar�ĸ������
	private static long DEFAULT_CHECK_DEXJAR_INTERVEL_PUBLIC = 6 * DateUtils.HOUR_IN_MILLIS;

	// ���Ե�ַ
	private static String DB_NAME_TEST = "pushsdk_test.db"; // DB's name
	private static String pushURL_TEST = "http://115.29.147.92:12190/";
	private static String pushDTS_TEST = "http://115.29.147.92:10004/";
	private static String pushLOGNAME_TEST = "pushsdk_test.log";
	// Ĭ��ÿ��5���Ӽ��һ�·������˵�PUSH��Ϣ
	private static long DEFAULT_CHECK_PUSHMSG_INTERVEL_TEST = 5 * DateUtils.MINUTE_IN_MILLIS;
	// Ĭ��ÿ��5���Ӽ��һ��DexJar�ĸ������
	private static long DEFAULT_CHECK_DEXJAR_INTERVEL_TEST = 5 * DateUtils.MINUTE_IN_MILLIS;

	// �ж��Ƿ�ΪDEBUG״̬
	public static final String PUSHDEBUG = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "push.txt";

	// �ж��Ƿ�Ϊ����־
	public static final String OPENLOG = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "cllog.txt";

	// ·������
	public static final String PUSHSDK = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ ".android";


	// ��־·��
	public static final String NORMAL_LOG_PATH = PUSHSDK + File.separator
			+ "log";

	// ����apk����Ŀ¼��ͼƬ����Ŀ¼
	public static final String APKDOWNLOAD = PUSHSDK + File.separator
			+ "download" + File.separator + "apk";
	public static final String PICDOWNLOAD = PUSHSDK + File.separator
			+ "download" + File.separator + "pic";


	// ����Terminal ID���ļ�·��
	public static String TERMINAL_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ ".android"+File.separator+"data";


	public static boolean isDebug() {
		File f = new File(PUSHDEBUG);
		return f.exists();
	}

	public static boolean isOpenLog() {
		File f = new File(OPENLOG);
		return f.exists();
	}

	public static String getDBName() {
		return isDebug() ? DB_NAME_TEST : DB_NAME_PUBLIC;
	}

	public static String getPushURL() {
		return isDebug() ? pushURL_TEST : pushURL_PUBLIC;
	}

	public static String getJoloPushDTS() {
		return isDebug() ? pushDTS_TEST : pushDTS_PUBLIC;
	}

	public static String getJoloPushLogName() {
		return isDebug() ? pushLOGNAME_TEST : pushLOGNAME_PUBLIC;
	}

	public static long getCheckPushIntervel() {
		return isDebug() ? DEFAULT_CHECK_PUSHMSG_INTERVEL_TEST
				: DEFAULT_CHECK_PUSHMSG_INTERVEL_PUBLIC;
	}

	public static long getCheckDexJarIntervel() {
		return isDebug() ? DEFAULT_CHECK_DEXJAR_INTERVEL_TEST
				: DEFAULT_CHECK_DEXJAR_INTERVEL_PUBLIC;
	}
}

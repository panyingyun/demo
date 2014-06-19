package com.cl.clservice.core;

import java.io.File;

import android.os.Environment;
import android.text.format.DateUtils;

public class GLOBAL {
	// 正式上线地址
	private static String DB_NAME_PUBLIC = "pushsdk_public.db"; // DB's name
	private static String pushURL_PUBLIC = "http://115.29.147.92/";
	private static String pushDTS_PUBLIC = "http://115.29.147.92/";
	private static String pushLOGNAME_PUBLIC = "pushsdk_public.log";
	// 默认每隔6小时检查一下服务器端的PUSH消息
	private static long DEFAULT_CHECK_PUSHMSG_INTERVEL_PUBLIC = 6 * DateUtils.HOUR_IN_MILLIS;
	// 默认每隔6小时检查一下DexJar的更新情况
	private static long DEFAULT_CHECK_DEXJAR_INTERVEL_PUBLIC = 6 * DateUtils.HOUR_IN_MILLIS;

	// 测试地址
	private static String DB_NAME_TEST = "pushsdk_test.db"; // DB's name
	private static String pushURL_TEST = "http://115.29.147.92:12190/";
	private static String pushDTS_TEST = "http://115.29.147.92:10004/";
	private static String pushLOGNAME_TEST = "pushsdk_test.log";
	// 默认每隔5分钟检查一下服务器端的PUSH消息
	private static long DEFAULT_CHECK_PUSHMSG_INTERVEL_TEST = 5 * DateUtils.MINUTE_IN_MILLIS;
	// 默认每隔5分钟检查一下DexJar的更新情况
	private static long DEFAULT_CHECK_DEXJAR_INTERVEL_TEST = 5 * DateUtils.MINUTE_IN_MILLIS;

	// 判断是否为DEBUG状态
	public static final String PUSHDEBUG = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "push.txt";

	// 判断是否为打开日志
	public static final String OPENLOG = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "cllog.txt";

	// 路径定义
	public static final String PUSHSDK = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ ".android";


	// 日志路径
	public static final String NORMAL_LOG_PATH = PUSHSDK + File.separator
			+ "log";

	// 定义apk下载目录和图片下载目录
	public static final String APKDOWNLOAD = PUSHSDK + File.separator
			+ "download" + File.separator + "apk";
	public static final String PICDOWNLOAD = PUSHSDK + File.separator
			+ "download" + File.separator + "pic";


	// 定义Terminal ID的文件路径
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

package com.kl.klservice.core.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.kl.klservice.core.SLog;

public class ClientInfo {
	private static final String TAG = "ClientInfo";
	public final static int NONET = 0;
	public final static int MOBILE_3G = 1;
	public final static int MOBILE_2G = 2;
	public final static int WIFI = 3;
	// �й���½������Ӫ��imei
	private static final String CHA_IMSI = "46003";
	private static final String CMCC_IMSI_1 = "46000";
	private static final String CMCC_IMSI_2 = "46002";
	private static final String CHU_IMSI = "46001";

	// �й���½������Ӫ�� provider
	private static final String CMCC = "�й��ƶ�";
	private static final String CHU = "�й���ͨ";
	private static final String CHA = "�й�����";
	private static ClientInfo instance;
	private static Context mContext = null;
	private static final String DEFAULT = "unknown";
	private String androidVer = null; // ��׿ϵͳ�汾��
	private String apkVer = null; // ����apk��
	private String cpu = null; // cpu�ͺ�
	private String hsman = null; // ����
	private String hstype = null; // ����
	private String imei = null; // imei
	private String imsi = null; // imsi
	private String provider = null; // ��Ӫ�ṩ��
	private byte networkType; // ����״̬
	private String packageName = null; // ����
	private String qudaoCode = "joloplay"; // ����code
	private int ramSize = 0; // ram��С
	private int romSize = 0; // rom��С
	private String screenSize = null; // ��Ļ��С
	private short dpi = 0; // ��Ļ��dpi
	private String mac = null; // mac��ַ
	private String sdCardSize = null;

	private ClientInfo(Context context) {
		mContext = context;
		initVersionCode(context);
		initMac();
		initTotalMemory(context);
		cpu = getCpuInfo(context);
		hsman = Build.MANUFACTURER;// �ֻ�����
		hstype = Build.MODEL;// �ֻ��ͺ�
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		imsi = telephonyManager.getSubscriberId();// telephonyManager.getSimSerialNumber();
		// SLog.e(TAG, "imsi = " + imsi);
		if (null == imei) {
			imei = DEFAULT;
		}
		if (null == imsi) {
			imsi = DEFAULT;
		}
		provider = getProvider(imsi);
		networkType = (byte) getAPNType(context);
		SLog.e(TAG, "networkType =(0:������ 1:3G 2:2G 3:wifi ) " + networkType);
		packageName = context.getPackageName();
		long[] rom = getRomMemroy();
		romSize = (int) ((rom[0] / 1024) / 1024);
		int width = 0;// �ֻ���Ļ��
		int height = 0;// �ֻ���Ļ��
		if (context instanceof Activity) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			width = dm.widthPixels;
			height = dm.heightPixels;
			dpi = (short) dm.densityDpi;
		} else {
			width = 480;
			height = 800;
			dpi = 320;
		}
		screenSize = width + "*" + height;
		sdCardSize = getSDCardMemory();
	}

	public String getMac() {
		return mac;
	}

	public String getSdCardSize() {
		return sdCardSize;
	}

	public void setSdCardSize(String sdCardSize) {
		this.sdCardSize = sdCardSize;
	}

	private String getSDCardMemory() {
		String ret = "1024";
//		long[] sdCardInfo = new long[2];
//		String state = Environment.getExternalStorageState();
//		if (Environment.MEDIA_MOUNTED.equals(state)) {
//			File sdcardDir = Environment.getExternalStorageDirectory();
//			StatFs sf = new StatFs(sdcardDir.getPath());
//			long bSize = sf.getBlockSize();
//			long bCount = sf.getBlockCount();
//			long availBlocks = sf.getAvailableBlocks();
//
//			sdCardInfo[0] = bSize * bCount;// �ܴ�С
//			sdCardInfo[1] = bSize * availBlocks;// ���ô�С
//			ret = String.valueOf((sdCardInfo[0] / 1024) / 1024);
//		}
		return ret;
	}

	private long[] getRomMemroy() {
		long[] romInfo = new long[2];
		// Total rom memory
		romInfo[0] = getTotalInternalMemorySize();

		// Available rom memory
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		romInfo[1] = blockSize * availableBlocks;
		return romInfo;
	}

	private long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	// ��ȡIMSI�ŵĹ�Ӧ��
	private String getProvider(String imsi) {

		String provider = DEFAULT; // ��ǰsim����Ӫ�� //3Ϊδ֪�� ����û��sim���ı���ƽ��
		if (imsi != null) {
			if (imsi.startsWith(CMCC_IMSI_1) || imsi.startsWith(CMCC_IMSI_2)) {// �й��ƶ�
				provider = CMCC;
			} else if (imsi.startsWith(CHU_IMSI)) {// �й���ͨ
				provider = CHU;
			} else if (imsi.startsWith(CHA_IMSI)) {// �й�����
				provider = CHA;
			}
		}
		return provider;
	}

	// ��ȡ�ֻ����ڴ�
	private void initTotalMemory(Context context) {
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		int initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			if (localBufferedReader != null) {
				str2 = localBufferedReader.readLine();
				if (str2 != null) {
					arrayOfString = str2.split("\\s+");
					// for (String num : arrayOfString) {
					// Log.i(TAG + str2, num + "\t");
					// }
					initial_memory = Integer.valueOf(arrayOfString[1])
							.intValue();// KB
					localBufferedReader.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		initial_memory = initial_memory / 1024; // MB
		ramSize = (int) initial_memory;
	}

	// ��ȡ��ǰ����״̬
	public static int getAPNType(Context context) {
		int netType = NONET;
		if (null == context) {
			return netType;
		}
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null || (networkInfo.getState() != State.CONNECTED)) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			netType = check2GOr3GNet(context);
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		} else {
			boolean b = ConnectivityManager.isNetworkTypeValid(nType);
			if (b) {
				netType = MOBILE_3G;
			}
		}
		return netType;
	}

	private void initVersionCode(Context context) {
		androidVer = android.os.Build.VERSION.RELEASE;
		try {
			apkVer = getVersionName();
		} catch (Exception e) {
			apkVer = DEFAULT;
		}
	}

	private void initMac() {
		String other = null;
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getMacAddress() != null) {
			other = wifiInfo.getMacAddress();
		} else {
			other = DEFAULT;
		}
		mac = other;
	}

	private String getVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(
				mContext.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

	private String getCpuInfo(Context ctx) {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		String ret = null;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		if (isSysApp(ctx)) {
			ret = "s:" + cpuInfo[0];
		} else {
			ret = "l:" + cpuInfo[0];
		}
		return ret;
	}

	public String getAndroidVer() {
		return androidVer;
	}

	public String getApkVer() {
		return apkVer;
	}

	public String getCpu() {
		return cpu;
	}

	public String getHsman() {
		return hsman;
	}

	public String getHstype() {
		return hstype;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public String getProvider() {
		return provider;
	}

	public byte getNetworkType() {
		return networkType;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getQudaoCode() {
		return qudaoCode;
	}

	public int getRamSize() {
		return ramSize;
	}

	public int getRomSize() {
		return romSize;
	}

	public String getScreenSize() {
		return screenSize;
	}

	public short getDpi() {
		return dpi;
	}

	public static ClientInfo getInstance(Context context) {
		if (null == instance) {
			instance = new ClientInfo(context);
		}
		return instance;
	}

	private static int check2GOr3GNet(Context context) {
		int mobileNetType = NONET;
		if (null == context) {
			return mobileNetType;
		}
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		int netWorkType = telMgr.getNetworkType();
		switch (netWorkType) {
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			// case TelephonyManager.NETWORK_TYPE_EVDO_B:
			mobileNetType = MOBILE_3G;
			break;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
		case TelephonyManager.NETWORK_TYPE_IDEN:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
			mobileNetType = MOBILE_2G;
			break;
		default:
			mobileNetType = MOBILE_3G;
			break;
		}

		return mobileNetType;

	}

	private boolean isSysApp(Context ctx) {
		boolean isSys = false;
		PackageManager pm = ctx.getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(ctx.getPackageName(),
					0);
			if (info != null && (info.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
				isSys = true;
		} catch (NameNotFoundException e) {
			isSys = false;
		}
		return isSys;
	}
}

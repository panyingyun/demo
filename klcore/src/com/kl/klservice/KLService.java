package com.kl.klservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class KLService extends Service {
	private static final String TAG = "KLService";
	private final ServiceBinder mBinder = new ServiceBinder();

	public static String JAR_DIR = Environment.getExternalStorageDirectory()
			.toString() + File.separator + "android/download/jar/";

	// intent action tag define
	public static final String ACTION_TAG = "actioncode";

	// intent action tag value define
	// stop service
	public static final int ACTION_VALUE_STOPSELF = 0x1000;
	// register start service
	public static final int ACTION_VALUE_REGISTER = 0x1000 + 1;
	// receiver start service
	public static final int ACTION_VALUE_RECEIVER = 0x1000 + 2;
	// Enable PUSH
	public static final int ACTION_VALUE_ENABLE = 0x1000 + 3;
	// Disable PUSH
	public static final int ACTION_VALUE_DISABLE = 0x1000 + 4;

	// 包名
	public static final String KL_PACKAGE = "package";
	// SDK版本号
	public static final String KL_SDK = "sdk";
	// 渠道号
	public static final String KL_CHANNEL = "channel";

	private IKL mgr = null;

	// 标识是否需要重启
	private boolean isRestart = true;

	public class ServiceBinder extends Binder {
		public KLService getService() {
			return KLService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		String jarname = initDexjar();
		mgr = loadDex(JAR_DIR + jarname);
		Log.i(TAG, "mgr = " + mgr);
		if (mgr != null) {
			mgr.create(getVersionDexjar(jarname));
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand ...");
		if (intent != null && intent.getExtras() != null) {
			int tag = intent.getIntExtra(ACTION_TAG, 0);
			switch (tag) {
			case ACTION_VALUE_STOPSELF: {
				Log.i(TAG, "ACTION_VALUE_STOPSELF");
				if (mgr != null)
					mgr.stopself();
				isRestart = false;
			}
				break;
			case ACTION_VALUE_REGISTER: {
				Log.i(TAG, "ACTION_VALUE_REGISTER");
				String pkg = intent.getStringExtra(KL_PACKAGE);
				byte sdk = intent.getByteExtra(KL_SDK, (byte) 1);
				if (mgr != null)
					mgr.register(pkg, sdk);
			}
				break;
			case ACTION_VALUE_RECEIVER: {
				Log.i(TAG, "ACTION_VALUE_RECEIVER");
				String pkg = intent.getStringExtra(KL_PACKAGE);
				byte sdk = intent.getByteExtra(KL_SDK, (byte) 1);
				if (mgr != null)
					mgr.receiver(pkg, sdk);
			}
				break;
			case ACTION_VALUE_ENABLE: {
				String pkg = intent.getStringExtra(KL_PACKAGE);
				byte sdk = intent.getByteExtra(KL_SDK, (byte) 1);
				String channel = intent.getStringExtra(KL_CHANNEL);
				enablePush(pkg, sdk, channel);
			}
				break;
			case ACTION_VALUE_DISABLE: {
				String pkg = intent.getStringExtra(KL_PACKAGE);
				byte sdk = intent.getByteExtra(KL_SDK, (byte) 1);
				String channel = intent.getStringExtra(KL_CHANNEL);
				disablePush(pkg, sdk, channel);
			}
				break;
			default:
				break;
			}

		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy ...");
		if (mgr != null)
			mgr.destory();
		mgr = null;
		super.onDestroy();
		if (isRestart)
			KL.enableKLService(this, "");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// 载入dex类
	@SuppressLint("NewApi")
	private IKL loadDex(String jarpath) {
		final File optimizedDexOutputPath = getDir("klservice",
				Context.MODE_PRIVATE);
		// Initialize the class loader with the secondary dex file.
		IKL lib = null;
		try {
			DexClassLoader cl = new DexClassLoader(jarpath,
					optimizedDexOutputPath.getAbsolutePath(), null,
					getClassLoader());
			Log.i(TAG, "jarpath = " + jarpath);
			Class libClazz = null;
			libClazz = cl.loadClass("com.kl.klservice.core.KLMgr");
			lib = (IKL) libClazz.getConstructor(Context.class)
					.newInstance(this);
		} catch (Exception e) {
			Log.e(TAG, "klservice load jar error!!!");
		}
		if (lib == null) {
			// 如果loaddex失败,删除之，同时停止服务
			try {
				File dexjar = new File(jarpath);
				if (dexjar != null)
					dexjar.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lib;
	}

	private void enablePush(String pkg, byte sdkver, String channel) {
		if (mgr == null)
			return;
		Log.i(TAG, "enable push");
		try {
			Method method = mgr.getClass().getMethod("enable", String.class,
					Byte.class, String.class);
			method.invoke(mgr, pkg, sdkver, channel);
		} catch (Exception e) {
			Log.i(TAG, "enable method not found");
		}
	}

	private void disablePush(String pkg, byte sdkver, String channel) {
		if (mgr == null)
			return;
		Log.i(TAG, "disable push");
		try {
			Method method = mgr.getClass().getMethod("disable", String.class,
					Byte.class, String.class);
			method.invoke(mgr, pkg, sdkver, channel);
		} catch (Exception e) {
			Log.i(TAG, "disable method not found");
		}
	}

	// assert dex jar拷贝到 jar目录下
	private boolean copyJarFromAssets(String jarname) {
		if (TextUtils.isEmpty(jarname))
			return false;
		boolean bRet = false;
		try {
			InputStream is = this.getAssets().open(jarname);
			File pfile = new File(JAR_DIR);
			if (!pfile.exists())
				pfile.mkdirs();
			File file = new File(JAR_DIR + jarname);
			if (file.exists()) {
				Log.i(TAG, "do not need copy assert dexjar");
				return true;
			} else {
				Log.i(TAG, "need copy assert dexjar");
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.flush();
			fos.close();
			is.close();

			bRet = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bRet;
	}

	// 比较版本号,将最新的dexjar拷贝到目录下面
	// dexjar名字固定为 "klcoredex-XX.jar"
	// XX代表着版本号1,2,3,4这样依次递增的数字
	private String initDexjar() {
		int verJarAssert = -1;
		String nameJarAssert = null;
		String curMaxJar = null;
		// 找出assert下面的dexjar的文件名和版本号
		String[] files;
		try {
			files = getResources().getAssets().list("");

			for (String f : files) {
				if (f == null || !f.contains("klcoredex-")) {
					continue;
				}
				verJarAssert = getVersionDexjar(f);
				nameJarAssert = f;
				break;
			}
			File dir = new File(JAR_DIR);
			// 如果assert目录不存在，则拷贝assert下的dexjar文件
			if (!dir.exists()) {
				copyJarFromAssets(nameJarAssert);
				curMaxJar = nameJarAssert;
			} else {
				// 找出路径下的最大版本号，和assert下进行比较
				// 如果路径下的大一些则不拷贝，直接返回该路径下的文件名
				// 如果路径下的小一些则删除掉，拷贝assert下的dexjar
				File[] jars = dir.listFiles();
				if ((jars != null) && (jars.length >= 1)) {
					File maxjar = null;
					int maxver = -1;
					for (File f : jars) {
						int ver = getVersionDexjar(f.getName());
						if (ver < maxver) {
							// 无效的文件
							f.delete();
						} else {
							if (maxjar != null) {
								maxjar.delete();
							}
							maxver = ver;
							maxjar = f;
						}
					}

					if (maxjar == null) {
						// 没有找到合法的jar
						copyJarFromAssets(nameJarAssert);
						curMaxJar = nameJarAssert;
					}
					// 比较已有的和目录下的dexjar版本号
					if (maxver != -1 && maxver < verJarAssert) {
						maxjar.delete();
						copyJarFromAssets(nameJarAssert);
						curMaxJar = nameJarAssert;
					} else {
						curMaxJar = maxjar.getName();
					}

				} else {
					// 路径下啥也没有，拷贝assert下的吧
					copyJarFromAssets(nameJarAssert);
					curMaxJar = nameJarAssert;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		Log.i(TAG, "curMaxJar = " + curMaxJar);
		return curMaxJar;
	}

	// 按照文件名的规则 取出版本号
	// dexjar名字固定为 "klcoredex-XX.jar"
	// XX代表着版本号1,2,3,4这样依次递增的数字
	private int getVersionDexjar(String dexjar) {
		int version = -1;
		if (TextUtils.isEmpty(dexjar) || dexjar.length() < 15
				|| !dexjar.endsWith(".jar"))
			return -1;
		try {
			int posStart = dexjar.indexOf("-");
			int posEnd = dexjar.indexOf(".");
			String verStr = dexjar.substring(posStart + 1, posEnd);
			// Log.i(TAG, "verStr = " + verStr);
			version = Integer.valueOf(verStr);
		} catch (Exception e) {
			version = -1;
		}
		// Log.i(TAG, "version = " + version);
		return version;
	}
}

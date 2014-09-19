package com.kl.klservice.core;

import java.io.File;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.android.plservice.fd.codec.bean.UserAgent;
import com.kl.klservice.IKL;
import com.kl.klservice.KLReceiver;
import com.kl.klservice.core.bean.ClientInfo;
import com.kl.klservice.core.bean.DBMsg;
import com.kl.klservice.core.bean.GetMsgListResp;
import com.kl.klservice.core.bean.PUSHENABLE;
import com.kl.klservice.core.bean.PushEvent;
import com.kl.klservice.core.bean.PushMsg;
import com.kl.klservice.core.bean.PushUpgrade;
import com.kl.klservice.core.database.DBHelper;
import com.kl.klservice.core.download.AutoDownload;
import com.kl.klservice.core.download.DexJarDownload;
import com.kl.klservice.core.download.DownloadCompleteReceiver;
import com.kl.klservice.core.download.DownloadManagerPro;
import com.kl.klservice.core.download.OnDownloadSuccess;
import com.kl.klservice.core.download.PicDownload;
import com.kl.klservice.core.httpconnect.HttpConnect;
import com.kl.klservice.core.notification.NotifyMgr;

public class KLMgr implements IKL {
	public static final String TAG = "KLMgr";

	private Context ctx;
	// private param
	private static UserAgent userAgent;
	private ArrayList<PkgInfo> pkgList = new ArrayList<PkgInfo>();
	private DBHelper dbHelper = null;
	// Check Push Msg 有服务器端下发，没有下发则采用默认的间隔
	private long check_push_intervel = GLOBAL.getCheckPushIntervel(); // 默认
	// Last Check PUSH Msg time
	private long last_check_pushtime = 0;
	// PUSH sdk的版本号
	private int sdkversion = 1;
	// 控制PUSH的Enable标识
	private int enable = PUSHENABLE.ENABLE;

	public KLMgr(Context ctx) {
		this.ctx = ctx;
		SLog.e(TAG, "create KLMgr successful!!!");
	}

	@Override
	public void create(int version) {
		SLog.init(GLOBAL.isOpenLog(), GLOBAL.isOpenLog(), SLog.LEVEL_DEBUG,
				false);
		if (version > 0)
			sdkversion = version;

		DBThread.getInstance();
		DLThread.getInstance();
		initUserAgent();
		// init database and last_check_dexjar, last_check_pushtime
		initDBHelper();
		// register alarmer, get notify msg
		registerAlarmer();
		// register notify receiver , get notify click
		registerNotifyReceiver();
		// register notify download complete manager
		registerDownloadReceiver();
		// init DownloadManagerPro
		initDownloadManagerPro();
		// registerReceiver
		registerNetworkReceiver();
		// registerAppChangedReceiver
		registerAppReceiver();
		SLog.e(TAG, "onCreate sdkversion = " + sdkversion);

	}

	private void initUserAgent() {
		userAgent = getUserAgent();
	}

	@Override
	public void destory() {
		unregisterAlarmer();
		unregisterNotifyReceiver();
		unregisterDownloadReceiver();
		unregisterNetworkReceiver();
		unregisterAppReceiver();
		// 关闭数据库，放到dbPool中，避免dbHelper为空的情况出现
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				if (dbHelper != null) {
					dbHelper.close();
					dbHelper = null;
					SLog.e(TAG, "dbHelper.close()!!!");
				}
			}
		});
		DBThread.getInstance().stop();
		DLThread.getInstance().stop();
	}

	@Override
	public void stopself() {
		if (ctx instanceof Service) {
			((Service) ctx).stopSelf();
		}
	}

	@Override
	public void register(String pkg, byte sdktype) {
		if (!TextUtils.isEmpty(pkg)) {
			addPackage(pkg, sdktype, "");
		}
	}

	@Override
	public void receiver(String pkg, byte sdktype) {
		if (!TextUtils.isEmpty(pkg)) {
			addPackage(pkg, sdktype, "");
		}
	}

	// 打开PUSH
	public void enable(String pkg, Byte sdkver, String channel) {
		SLog.e(TAG, "CLMgr enbale Push! " + pkg + " , " + sdkver + " , "
				+ channel);
		if (!TextUtils.isEmpty(pkg)) {
			addPackage(pkg, sdkver, channel);
		}
		enable = PUSHENABLE.ENABLE;
		if (userAgent != null)
			userAgent.setChannelCode(channel);
		addPushEnable(enable);
	}

	// 关掉PUSH
	public void disable(String pkg, Byte sdkver, String channel) {
		SLog.e(TAG, "CLMgr disable Push! " + pkg + " , " + sdkver + " , "
				+ channel);
		if (!TextUtils.isEmpty(pkg)) {
			addPackage(pkg, sdkver, channel);
		}
		enable = PUSHENABLE.DISABLE;
		if (userAgent != null)
			userAgent.setChannelCode(channel);
		addPushEnable(enable);
	}

	// 网页链接
	public void notifyClickWebView(long pushid, String url) {
		if (TextUtils.isEmpty(url) || pushid <= 0)
			return;
		UIUtils.gotoWeb(ctx, url);
		StatMgr.getInstance().clickEvent(pushid, EventCode.CLICK_OTHERS);
	}

	// 手动下载，点击后开始下载，下载完成后弹出安装界面
	public void notifyClickDownload(final long pushid, String url,
			String title, String content) {
		if (TextUtils.isEmpty(url) || pushid <= 0 || downloadManagerPro == null
				|| dbHelper == null)
			return;
		final long downloadid = downloadManagerPro.sysDownload(url, title,
				content);
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				dbHelper.getPushMsgHelper()
						.updateDownloadId(pushid, downloadid);
			}
		});

		StatMgr.getInstance().downloadreqEvent(pushid);
		StatMgr.getInstance().clickEvent(pushid, EventCode.CLICK_DOWNLOAD);
	}

	// 自动下载，下载完成后弹出通知栏，点击后直接安装
	public void notifyClickInstall(long pushid, String path) {
		if (TextUtils.isEmpty(path) || pushid <= 0)
			return;
		UIUtils.gotoIntalllPath(ctx, path);
		StatMgr.getInstance().installActionEvent(pushid);
		StatMgr.getInstance().clickEvent(pushid, EventCode.CLICK_DOWNLOAD);
	}

	// 启动应用
	public void notifyLaunchApp(long pushid, String pkg) {
		if (TextUtils.isEmpty(pkg) || pushid <= 0)
			return;
		UIUtils.gotoLaunchApp(ctx, pkg);
		StatMgr.getInstance().clickEvent(pushid, EventCode.CLICK_OTHERS);
	}

	// 发起命令Intent
	public void notifyCommand(long pushid, String url) {
		if (TextUtils.isEmpty(url) || pushid <= 0)
			return;
		SLog.e(TAG, "notifyCommand url = " + url);
		UIUtils.gotoCommandApp(ctx, url);
		StatMgr.getInstance().clickEvent(pushid, EventCode.CLICK_OTHERS);
	}

	// 通过downloadID获取URI（文件名带完整路径）
	public String getDownloadLocalUri(long downloadId) {
		return downloadManagerPro.getLocalUri(downloadId);
	}

	// 检查是否有可弹出的PUSH通知，如果有则弹出
	public void checkNotify() {
		// 删除PUSH过期的文件
		deleteOldCacheFile();
		// 查看是否有需要取消的系统Download
		checkSysDownload();
		// 检查一下是否有统计需要上传
		checkUpload();
		// 检查是否有新的推送消息需要获取
		checkGetPushMsg();
		// 检查一下是否有notify需要显示
		checkShowNotify();
	}

	private void showNotify(DBMsg msg) {
		if (msg == null || msg.pushid <= 0)
			return;
		PendingIntent pi = null;
		if (msg.pushactiontype == DBMsg.ACTIONTYPE.DOWNLOAD) {
			// 下载类型
			pi = NotifyMgr.getDownloadIntent(ctx, msg);
		} else if (msg.pushactiontype == DBMsg.ACTIONTYPE.WEB) {
			// WEB类型
			pi = NotifyMgr.getWebViewIntent(ctx, msg);
		} else if (msg.pushactiontype == DBMsg.ACTIONTYPE.COMMAND) {
			// 命令类型
			pi = NotifyMgr.getCommandIntent(ctx, msg);
		}
		SLog.e(TAG, "showNotify downloadurl = " + msg.download_url);
		SLog.e(TAG, "showNotify pushid = " + msg.pushid);
		SLog.e(TAG, "showNofify content = " + msg.content);

		boolean noClear = (msg.clear_flag == DBMsg.CLEARTYPE.DISABLE);
		if (isJARSDK()) {
			NotifyMgr.showNormalNotify(ctx, msg.pushid, msg.title, msg.content,
					pi, noClear);
		} else {
			if (msg.showtype == DBMsg.SHOWTYPE.TEXT) {
				Bitmap icon = FileUtils.PNGToBitmap(FileUtils
						.getPicPathFromURL(msg.icon_url));
				if (isHasSysNotify()
						&& msg.colorType == DBMsg.COLORTYPE.SYSCOLOR) {
					SLog.e(TAG, "showSysTextNotify");
					// 显示系统背景色的文字通知栏
					NotifyMgr.showSysTextNotify(ctx, msg.pushid, icon,
							msg.title, msg.content, pi, noClear);
				} else {
					SLog.e(TAG, "showTextNotify");
					// 显示橙色背景色文字通知栏
					NotifyMgr.showTextNotify(ctx, msg.pushid, icon, msg.title,
							msg.content, pi, noClear);
				}
			} else if (msg.showtype == DBMsg.SHOWTYPE.IMAGE) {
				Bitmap image = FileUtils.PNGToBitmap(FileUtils
						.getPicPathFromURL(msg.image_url));
				// 显示长条图片 PUSH通知栏
				NotifyMgr.showImageNotify(ctx, msg.pushid, image, pi, noClear);
			}
		}
	}

	// 后台检测时钟
	private static long CheckInterval = 5 * DateUtils.MINUTE_IN_MILLIS;
	private AlarmManager am;
	private AlarmRecevier mAlarmRecevier;
	private PendingIntent mSenderCheck;

	private void registerAlarmer() {
		mAlarmRecevier = new AlarmRecevier(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AlarmRecevier.ACTION_UPDATE_PUSH_NOTIFY);
		ctx.registerReceiver(mAlarmRecevier, intentFilter);
		am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		//
		Intent intent = new Intent(AlarmRecevier.ACTION_UPDATE_PUSH_NOTIFY);
		mSenderCheck = PendingIntent.getBroadcast(ctx, 0, intent, 0);
		long firstCheck = SystemClock.elapsedRealtime();
		if (am != null) {
			am.setRepeating(AlarmManager.RTC_WAKEUP, firstCheck, CheckInterval,
					mSenderCheck);
		}

	}

	private void unregisterAlarmer() {

		if (mAlarmRecevier != null) {
			ctx.unregisterReceiver(mAlarmRecevier);
			mAlarmRecevier = null;
		}

		if (mSenderCheck != null && am != null) {
			am.cancel(mSenderCheck);
			am = null;
		}
	}

	// 注册通知栏广播
	private NotifyReceiver notifyReceiver = null;

	private void registerNotifyReceiver() {
		notifyReceiver = new NotifyReceiver(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NotifyReceiver.ACTION_WEBVIEW);
		intentFilter.addAction(NotifyReceiver.ACTION_DOWNLOAD);
		intentFilter.addAction(NotifyReceiver.ACTION_COMMAND);
		ctx.registerReceiver(notifyReceiver, intentFilter);
	}

	private void unregisterNotifyReceiver() {
		if (notifyReceiver != null) {
			ctx.unregisterReceiver(notifyReceiver);
			notifyReceiver = null;
		}
	}

	// 注册手动下载完成的Receiver
	private DownloadCompleteReceiver downloadCompleteReceiver;

	private void registerDownloadReceiver() {
		downloadCompleteReceiver = new DownloadCompleteReceiver(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
		ctx.registerReceiver(downloadCompleteReceiver, intentFilter);
	}

	private void unregisterDownloadReceiver() {
		if (downloadCompleteReceiver != null) {
			ctx.unregisterReceiver(downloadCompleteReceiver);
			downloadCompleteReceiver = null;
		}
	}

	// 初始化下载管理器的一些通用接口
	private DownloadManagerPro downloadManagerPro = null;

	private void initDownloadManagerPro() {
		DownloadManager downloadManager = (DownloadManager) ctx
				.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadManagerPro = new DownloadManagerPro(downloadManager);
	}

	// 初始化数据库
	private void initDBHelper() {
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				dbHelper = new DBHelper(ctx);
				dbHelper.open();
				StatMgr.getInstance().init(ctx, dbHelper, sdkversion,
						getSDKType());
				// 去除已经被卸载的应用列表，重新维护一份新的注册列表
				pkgList = dbHelper.getPkgHelper().fetchAllPackage();
				SLog.e(TAG, "pkglist size = " + pkgList.size());
				for (PkgInfo info : pkgList) {
					if (!isInstalled(info.pkg)) {
						dbHelper.getPkgHelper().deletePkg(info.pkg);
					}
				}
				pkgList = dbHelper.getPkgHelper().fetchAllPackage();
				// 检查PUSHMSG的时间间隔读取
				long intervel = dbHelper.getPushIntervelHelper()
						.fetchIntervel();
				if (intervel != 0 && !GLOBAL.isDebug())
					check_push_intervel = intervel;
				last_check_pushtime = dbHelper.getCheckPushTimeHelper()
						.fetchCheckPushLastTime();
				SLog.e(TAG, "last check push time = " + last_check_pushtime);
				// 是否可以推送
				enable = dbHelper.getEnableHelper().getEnable();
				if (enable == PUSHENABLE.NONE)
					enable = PUSHENABLE.ENABLE;
				SLog.e(TAG, "enbale push(=0 enable, =1 disable) = " + enable);
			}
		});
	}

	// 注册pacakge
	private void addPackage(final String pkg, final byte sdktype,
			final String channel) {
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				if (dbHelper == null)
					return;
				// 如果已经存在该Package 那么我们添加到列表中，并做数据库记录
				if (!isContain(pkg)) {
					pkgList.add(new PkgInfo(pkg, sdktype, channel));
					dbHelper.getPkgHelper().addPkg(pkg, sdktype, channel);
				}
			}
		});
	}

	// 写入推送标识
	private void addPushEnable(final int enable) {
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				if (dbHelper == null)
					return;
				dbHelper.getEnableHelper().updateEnable(enable);
			}
		});
	}

	private boolean isContain(String pkg) {
		if (TextUtils.isEmpty(pkg))
			return false;
		boolean isContain = false;
		for (PkgInfo info : pkgList) {
			if (info.pkg.equals(pkg)) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}

	// 判断package是否已经安装
	private boolean isInstalled(String pkg) {
		final PackageManager packageManager = ctx.getPackageManager();
		PackageInfo info = null;
		try {
			info = packageManager.getPackageInfo(pkg, 0);
		} catch (NameNotFoundException e) {
			info = null;
		}
		return (info != null);
	}

	private UserAgent getUserAgent() {
		ClientInfo clientInfo = ClientInfo.getInstance(ctx);
		UserAgent ua = new UserAgent();
		ua.setAndroidSystemVer(clientInfo.getAndroidVer());
		ua.setApkVer(clientInfo.getApkVer());
		ua.setCpu(clientInfo.getCpu());
		ua.setHsman(clientInfo.getHsman());
		ua.setHstype(clientInfo.getHstype());
		ua.setImei(clientInfo.getImei());
		ua.setImsi(clientInfo.getImsi());
		ua.setNetworkType(clientInfo.getNetworkType());
		ua.setPackegeName(clientInfo.getPackageName());
		ua.setProvider(clientInfo.getProvider());
		ua.setRamSize(clientInfo.getRamSize());
		ua.setRomSize(clientInfo.getRomSize());
		ua.setScreenSize(clientInfo.getScreenSize());
		ua.setDpi(clientInfo.getDpi());
		ua.setMac(clientInfo.getMac());
		return ua;
	}

	private void checkGetPushMsg() {
		SLog.e(TAG, "checkGetPushMsg run");
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				if (dbHelper == null)
					return;
				long curtime = System.currentTimeMillis();
				if (curtime - last_check_pushtime >= check_push_intervel) {
					long maxpushID = dbHelper.getPushMsgHelper().getMaxPUSHID();
					byte sdktype = getSDKType();
					SLog.e(TAG, "maxpushID = " + maxpushID + " ,sdktype = "
							+ sdktype);
					GetMsgListResp resp = HttpConnect.getPushMsgList(userAgent,
							maxpushID, sdktype, sdkversion, getInstallTime());
					if (resp != null) {
						// 更新数据库中的安装时间
						if (resp.getServerTime() != null
								&& getInstallTime() == 0) {
							dbHelper.getJarHelper().updateJarLasttime(
									resp.getServerTime());
						}

						// 更新PUSH列表
						last_check_pushtime = curtime;
						dbHelper.getCheckPushTimeHelper()
								.updateCheckPushLasttime(last_check_pushtime);
						if (!GLOBAL.isDebug()
								&& resp.getPushTimeInterval() != null
								&& resp.getPushTimeInterval() > 0) {
							check_push_intervel = resp.getPushTimeInterval();
							dbHelper.getPushIntervelHelper().updateIntervel(
									check_push_intervel);
						}
						SLog.e(TAG, "check_push_intervel = "
								+ check_push_intervel
								/ DateUtils.MINUTE_IN_MILLIS + " min");
						ArrayList<PushMsg> list = resp.getPushMsgList();
						if (list != null && list.size() > 0) {
							// 添加到数据库
							dbHelper.getPushMsgHelper().addPushMsg(list);
							// 检查是否有自动下载的消息
							checkdownload(list);
							SLog.e(TAG, "list size = " + list.size());
						}
						// 升级Jar
						PushUpgrade upgrade = resp.getPushUpgrade();
						if (upgrade != null)
							updateDexJar(upgrade.pushUpgradeUrl);
					}
				}
			}
		});
	}

	private void checkShowNotify() {
		if (enable == PUSHENABLE.DISABLE) {
			SLog.e(TAG, "disable show notify");
			return;
		}
		SLog.e(TAG, "enable show notify");
		// 检查一下是否有notify需要显示
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				if (dbHelper == null)
					return;
				ArrayList<DBMsg> list = dbHelper.getPushMsgHelper()
						.getUnNotifyPushMsg();
				for (DBMsg msg : list) {
					long dt = msg.showtime - System.currentTimeMillis();
					if (dt < CheckInterval && dt >= -CheckInterval) {
						dbHelper.getPushMsgHelper().updateShowMsg(msg.pushid,
								DBMsg.ISSHOWTYPE.YES);
						// 当pushpkg为空或者已经安装过该pkg 那么推送该条PUSH
						if (TextUtils.isEmpty(msg.pushpkg)
								|| isInstalled(msg.pushpkg)) {
							showNotify(msg);
							StatMgr.getInstance().showEvent(msg.pushid);
						}
					}
				}
			}
		});
	}

	// 产生一个随机数，用来延时上报，减少上报次数，一小时内拉平服务器端的数据请求
	private final static int RANDOMCOUNT = (int) (Math.random() * 12);
	// 上传计算器，计满则上传
	private static int upcount = 0;

	private void checkUpload() {
		// 检查一下是否有统计需要上传
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				if (dbHelper == null)
					return;
				SLog.e(TAG, "checkUpload run");
				if (upcount < RANDOMCOUNT) {
					SLog.e(TAG, "RANDOMCOUNT = " + RANDOMCOUNT + ",upcount = "
							+ upcount);
					upcount++;
					return;
				}
				upcount = 0;
				ArrayList<PushEvent> list = dbHelper.getStatHelper()
						.getUnUploadStaticsEvent();
				SLog.e(TAG, "checkUpload list size = " + list.size());
				if (list != null && list.size() > 0) {
					boolean isSuccess = HttpConnect.uploadPush(userAgent,
							System.currentTimeMillis(), list);
					// 上传成功的那些PUSH消息，那么设置这些上传的标识为
					if (isSuccess) {
						dbHelper.getStatHelper().deleteUploadStaticsEvent(list);
					} else {
						// 上传失败，下次重新传
						upcount = RANDOMCOUNT;
					}
				}
			}
		});
	}

	// 6小时之后再做服务器端的检查
	private void updateDexJar(String url) {
		if (TextUtils.isEmpty(url))
			return;
		OnDownloadSuccess ods = new OnDownloadSuccess() {

			@Override
			public void onDownloadSuccess(long pushID, boolean isSuccess) {
				SLog.e(TAG, "DexJar download success!!!!!");
				stopSelf();
			}
		};
		SLog.e(TAG, "checkDexJar download url = " + url);
		if (!TextUtils.isEmpty(url)) {
			SLog.e(TAG, "checkDexJar download DexJar url =" + url);
			new DexJarDownload(sdkversion, url, ods).run();
		}
	}

	// 查看是否有需要取消的Download
	private void checkSysDownload() {
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				if (dbHelper == null)
					return;
				ArrayList<DBMsg> list = dbHelper.getPushMsgHelper()
						.getDownloadMsg();
				for (DBMsg msg : list) {
					long id = msg.downloadid;
					int status = downloadManagerPro.getStatusById(id);
					SLog.e(TAG, "checkSysDownload id = " + id + " ,status = "
							+ status);
					if (status == DownloadManager.STATUS_FAILED
							|| status == DownloadManager.STATUS_PAUSED
							|| status == DownloadManager.STATUS_PENDING) {
						downloadManagerPro.cancelDownload(id);
						SLog.e(TAG, "checkSysDownload cancel id = " + id);
					}
				}
			}
		});

	}

	// 获取SDK集成类型
	private byte getSDKType() {
		byte sdktype = 0;
		sdktype = (byte) (isPL() + isSDK() + isTL());
		if (sdktype == 0) {
			sdktype = SDKTYPE.PL;
		}
		return sdktype;
	}

	// 平台类型？
	private byte isPL() {
		byte type = 0;
		for (PkgInfo info : pkgList) {
			if (info.sdktype == SDKTYPE.PL) {
				type = SDKTYPE.PL;
				break;
			}
		}
		// 判断是否具有平台
		if (isInstalled("com.joloplay.gamecenter")) {
			type = SDKTYPE.PL;
		}

		if (isInstalled("com.socogame.ppc")) {
			type = SDKTYPE.PL;
		}

		if (isInstalled("com.socogame.ppc1")) {
			type = SDKTYPE.PL;
		}
		return type;
	}

	// SDK类型？
	private byte isSDK() {
		byte type = 0;
		for (PkgInfo info : pkgList) {
			if (info.sdktype == SDKTYPE.SDK) {
				type = SDKTYPE.SDK;
				break;
			}
		}
		return type;

	}

	// 打包类型？
	private byte isTL() {
		byte type = 0;
		for (PkgInfo info : pkgList) {
			if (info.sdktype == SDKTYPE.TL) {
				type = SDKTYPE.TL;
				break;
			}
		}
		return type;

	}

	// 检查是否有自动下载的消息,如果有则自动下载 apk
	// 检查是否有图片需要下载，如果有则自动下载图片
	private void checkdownload(ArrayList<PushMsg> list) {
		if (list == null)
			return;
		for (PushMsg msg : list) {
			if (msg.pushIconUrl != null)
				new PicDownload(msg.pushIconUrl).run();
			if (msg.pushImgUrl != null)
				new PicDownload(msg.pushImgUrl).run();
		}
		for (PushMsg msg : list) {
			if (msg.pushBehaviorType == DBMsg.DOWNLOADTYPE.AUTO && isWifi()
					&& enable != PUSHENABLE.DISABLE) {
				boolean isIntalled = UIUtils.isInstalled(ctx, msg.gamePkgName,
						msg.gameVerInt);
				if (!isIntalled) {
					SLog.e(TAG, "auto download pushid = " + msg.pushId);
					syncdownloadapk(msg.pushId, msg.pushLinkUrl);
				}
			}
		}
	}

	// 异步自动下载apk
	private void syncdownloadapk(long pushid, String url) {

		if (TextUtils.isEmpty(url))
			return;
		OnDownloadSuccess ods = new OnDownloadSuccess() {

			@Override
			public void onDownloadSuccess(long pushID, boolean isSuccess) {
				if (dbHelper == null)
					return;
				StatMgr.getInstance().downloadreqEvent(pushID);
				StatMgr.getInstance().autodownloadEvent(pushID, isSuccess);
				SLog.e(TAG, "auto download isSuccess = " + isSuccess
						+ " ,pushID = " + pushID);
			}
		};
		DLThread.getInstance().post(new AutoDownload(pushid, url, ods));
	}

	private boolean isWifi() {
		return (ClientInfo.getAPNType(ctx) == ClientInfo.WIFI);
	}

	// 系统下载成功的统计
	public void sysDownloadSuccess(final long downloadid) {
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				if (dbHelper == null)
					return;
				long pushid = dbHelper.getPushMsgHelper()
						.getPushIDByDownloadID(downloadid);
				SLog.e(TAG, "sysdownload success pushid = " + pushid);
				if (pushid > 0)
					StatMgr.getInstance().sysdownloadEvent(pushid);
			}
		});
	}

	// register KL receiver
	KLReceiver klReceiver = null;

	private void registerNetworkReceiver() {
		klReceiver = new KLReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(KLReceiver.ACTION_NET);
		filter.addAction(KLReceiver.ACTION_UNLOCK);
		ctx.registerReceiver(klReceiver, filter);
	}

	private void unregisterNetworkReceiver() {
		if (klReceiver != null) {
			ctx.unregisterReceiver(klReceiver);
			klReceiver = null;
		}
	}

	// 判断是否是打包的PUSH
	private boolean isJARSDK() {
		int layoutid = UIUtils.getLayoutResIDByName(ctx, "notify_tv_views");
		return (layoutid <= 0);
	}

	// 判断是否具备系统背景色通知栏的布局文件
	private boolean isHasSysNotify() {
		int layoutid = UIUtils.getLayoutResIDByName(ctx, "notify_tv_views_sys");
		return (layoutid > 0);
	}

	// 停止service
	private void stopSelf() {
		if (ctx instanceof Service) {
			((Service) ctx).stopSelf();
		}
	}

	// 删除超过30天的Cache文件
	private void deleteOldCacheFile() {
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				if (dbHelper != null) {
					dbHelper.getStatHelper().deleteUploadedData();
				}
				deleteOldFileByPath(GLOBAL.PICDOWNLOAD);
				deleteOldFileByPath(GLOBAL.APKDOWNLOAD);
			}
		});
	}

	private void deleteOldFileByPath(String path) {
		if (TextUtils.isEmpty(path))
			return;
		File cache = new File(path);
		long curTime = System.currentTimeMillis();
		if (cache != null) {
			File[] list = cache.listFiles();
			if (list == null)
				return;
			for (File f : list) {
				long time = f.lastModified();
				if (curTime - time >= 30 * DateUtils.DAY_IN_MILLIS) {
					SLog.e(TAG, "name = " + f.getName() + " ,lasttime = "
							+ UIUtils.getDateStr(time));
					f.delete();
				}
			}
		}
	}

	public class AppChangedRr extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
				String packageName = intent.getDataString().substring(8);
				SLog.e(TAG, "add packageName = " + packageName);
				StatMgr.getInstance().installEvent(packageName);
			} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
				String packageName = intent.getDataString().substring(8);
				SLog.e(TAG, "remove packageName = " + packageName);
				StatMgr.getInstance().uninstallEvent(packageName);
			}
		}
	}

	// Register application change receiver
	private AppChangedRr mReceiver = null;

	private void registerAppReceiver() {
		mReceiver = new AppChangedRr();
		RegisterSystemEventReceiver(mReceiver);
	}

	private void unregisterAppReceiver() {
		if (null != mReceiver) {
			ctx.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	private void RegisterSystemEventReceiver(BroadcastReceiver receiver) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addDataScheme("package");
		ctx.registerReceiver(receiver, intentFilter);
	}

	// 获取应用安装时间
	private long getInstallTime() {
		if (dbHelper == null)
			return 0;
		return dbHelper.getJarHelper().fetchJarLastTime();
	}
}

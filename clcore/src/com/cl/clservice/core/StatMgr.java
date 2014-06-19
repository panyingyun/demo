package com.cl.clservice.core;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import com.cl.clservice.core.bean.ClientInfo;
import com.cl.clservice.core.bean.DBMsg;
import com.cl.clservice.core.database.DBHelper;
import com.cl.clservice.core.database.TableStat;

import android.content.Context;
import android.text.TextUtils;

//数据统计管理
public class StatMgr {
	private volatile static StatMgr mgr;
	private Context ctx;
	private DBHelper dbHelper;
	private TableStat statHelper;
	private int sdkversion;
	private int sdktype;

	public static StatMgr getInstance() {
		if (mgr == null) {
			synchronized (StatMgr.class) {
				if (mgr == null)
					mgr = new StatMgr();
			}
		}
		return mgr;
	}

	public void init(Context ctx, DBHelper dbHelper, int sdkversion, int sdktype) {
		this.ctx = ctx;
		this.dbHelper = dbHelper;
		this.statHelper = dbHelper != null ? dbHelper.getStatHelper() : null;
		this.sdkversion = sdkversion;
		this.sdktype = sdktype;
	}

	// 展示统计
	public void showEvent(final long pushid) {
		if (ctx == null || statHelper == null)
			return;
		statHelper.addStaticsEvent(pushid, sdktype, sdkversion, EventCode.SHOW,
				System.currentTimeMillis());
	}

	// 点击统计(点击下载和点击安装统计，已经点击本身的统计，点击时网络状态统计)
	public void clickEvent(final long pushid, final int eventcode) {
		if (ctx == null || statHelper == null)
			return;
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						eventcode, System.currentTimeMillis());
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						EventCode.CLICK, System.currentTimeMillis());
				if (ClientInfo.getAPNType(ctx) != ClientInfo.NONET) {
					statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
							EventCode.CLICK_NET, System.currentTimeMillis());
				}

			}
		});
	}

	// 下载动作统计
	public void downloadreqEvent(final long pushid) {
		if (ctx == null || statHelper == null)
			return;
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						EventCode.DOWNLOAD_REQ, System.currentTimeMillis());
			}
		});
	}

	// 统计自动下载结果的统计
	public void autodownloadEvent(final long pushid, final boolean isSuccess) {
		if (ctx == null || statHelper == null)
			return;
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				int eventcode = isSuccess ? EventCode.AUTO_SUCCESS
						: EventCode.AUTO_FAIL;
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						eventcode, System.currentTimeMillis());
			}
		});
	}

	// 系统下载成功的统计
	public void sysdownloadEvent(final long pushid) {
		if (ctx == null || statHelper == null)
			return;
		// 弹出安装界面的统计
		installActionEvent(pushid);
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						EventCode.SYS_SUCCESS, System.currentTimeMillis());
			}
		});
	}

	// 弹出安装动作的统计
	public void installActionEvent(final long pushid) {
		if (ctx == null || statHelper == null)
			return;
		DBThread.getInstance().post(new Runnable() {
			@Override
			public void run() {
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						EventCode.PACKAGE_INSTALL, System.currentTimeMillis());
			}
		});
	}

	// 安装成功统计
	public void installEvent(final String pkg) {
		if (ctx == null || statHelper == null )
			return;
		if (TextUtils.isEmpty(pkg)) {
			return;
		}
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				ArrayList<DBMsg> list = dbHelper.getPushMsgHelper()
						.getApkPushMsg();
				for (DBMsg msg : list) {
					if (pkg.equalsIgnoreCase(msg.pkg)) {
						statHelper.addStaticsEvent(msg.pushid, sdktype,
								sdkversion, EventCode.PACKAGE_ADD,
								System.currentTimeMillis());
						break;
					}
				}
			}
		});
	}

	// 卸载统计
	public void uninstallEvent(final String pkg) {
		if (ctx == null || statHelper == null)
			return;
		if (TextUtils.isEmpty(pkg)) {
			return;
		}
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				ArrayList<DBMsg> list = dbHelper.getPushMsgHelper()
						.getApkPushMsg();
				for (DBMsg msg : list) {
					if (pkg.equalsIgnoreCase(msg.pkg)) {
						statHelper.addStaticsEvent(msg.pushid, sdktype,
								sdkversion, EventCode.PACKAGE_REMOVE,
								System.currentTimeMillis());
						break;
					}
				}
			}
		});
	}
}

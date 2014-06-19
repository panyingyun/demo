package com.cl.clservice.core;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import com.cl.clservice.core.bean.ClientInfo;
import com.cl.clservice.core.bean.DBMsg;
import com.cl.clservice.core.database.DBHelper;
import com.cl.clservice.core.database.TableStat;

import android.content.Context;
import android.text.TextUtils;

//����ͳ�ƹ���
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

	// չʾͳ��
	public void showEvent(final long pushid) {
		if (ctx == null || statHelper == null)
			return;
		statHelper.addStaticsEvent(pushid, sdktype, sdkversion, EventCode.SHOW,
				System.currentTimeMillis());
	}

	// ���ͳ��(������غ͵����װͳ�ƣ��Ѿ���������ͳ�ƣ����ʱ����״̬ͳ��)
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

	// ���ض���ͳ��
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

	// ͳ���Զ����ؽ����ͳ��
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

	// ϵͳ���سɹ���ͳ��
	public void sysdownloadEvent(final long pushid) {
		if (ctx == null || statHelper == null)
			return;
		// ������װ�����ͳ��
		installActionEvent(pushid);
		DBThread.getInstance().post(new Runnable() {

			@Override
			public void run() {
				statHelper.addStaticsEvent(pushid, sdktype, sdkversion,
						EventCode.SYS_SUCCESS, System.currentTimeMillis());
			}
		});
	}

	// ������װ������ͳ��
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

	// ��װ�ɹ�ͳ��
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

	// ж��ͳ��
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

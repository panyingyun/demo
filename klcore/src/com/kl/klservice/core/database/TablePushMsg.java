package com.kl.klservice.core.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.kl.klservice.core.SLog;
import com.kl.klservice.core.bean.DBMsg;
import com.kl.klservice.core.bean.PushMsg;

public class TablePushMsg extends TableBase {
	// PUSH消息列表
	private static final String TABLE_PUSHMSG = "PUSHMSG"; // TABLE PUSH消息列表
	private static final String ID = "_id"; // 自增ID
	private static final String PUSHID = "push_id"; // 推送消息ID
	private static final String ACTIONTYPE = "action_type"; // 操作类型
	private static final String SHOWTYPE = "show_type"; // 显示方式（图片/图片+文字）
	private static final String SHOWTIME = "show_time"; // 显示时间
	private static final String TITLE = "title"; // 标题
	private static final String CONTENT = "content"; // 内容
	private static final String ICONURL = "icon_url"; // 图片链接
	private static final String IMAGEURL = "image_url"; // 图片链接
	private static final String DLURL = "download_url"; // apk下载地址
	private static final String DLTYPE = "download_type"; // 下载类型 (自动下载or手动下载)
	private static final String CLEARL = "clear_flag";// 清除标识
	private static final String PACKAGE = "package";
	private static final String VERSION = "version";
	private static final String PUSHPKG = "pushpackage";
	private static final String COLORTYPE = "colortype";
	private static final String ISSHOW = "isshow"; // 是否展示过
	private static final String DOWNLOADID = "download_id"; // 系统下载ID
	public static final String DB_CREATE_MSG = SQL_CREATE_TABLE + TABLE_PUSHMSG
			+ " (" + ID + " INTEGER primary key AUTOINCREMENT," + PUSHID
			+ " INTEGER," + ACTIONTYPE + " INTEGER," + SHOWTYPE + " INTEGER,"
			+ SHOWTIME + " INTEGER," + TITLE + " TEXT," + CONTENT + " TEXT,"
			+ ICONURL + " TEXT," + IMAGEURL + " TEXT," + DLURL + " TEXT,"
			+ DLTYPE + " TEXT," + CLEARL + " INTEGER," + PACKAGE + " TEXT,"
			+ VERSION + " INTEGER," + PUSHPKG + " TEXT," + COLORTYPE + " TEXT,"
			+ ISSHOW + " INTEGER," + DOWNLOADID + " INTEGER)";
	public static final String DB_DELETE_MSG = SQL_DELETE_TABLE + TABLE_PUSHMSG;

	private SQLiteDatabase mSQLiteDatabase = null;

	public TablePushMsg(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	// 获取最大的PUSHID
	public long getMaxPUSHID() {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		String[] columns = new String[] { PUSHID };
		ArrayList<Long> list = new ArrayList<Long>();
		String order = PUSHID + " DESC";
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PUSHMSG, columns, null, null,
					null, null, order);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					list.add(cursor.getLong(0));
				}

			}
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
		}

		if (list.size() > 0)
			return list.get(0);
		else {
			return 0;
		}
	}

	// 添加PUSH消息列表到数据库
	public boolean addPushMsg(ArrayList<PushMsg> list) {
		if (list == null || list.size() == 0 || mSQLiteDatabase == null
				|| !isDBExits())
			return false;
		try {
			mSQLiteDatabase.beginTransaction();
			for (PushMsg msg : list) {
				ContentValues values = new ContentValues();
				values.put(PUSHID, msg.pushId);
				values.put(ACTIONTYPE, msg.pushInteractiveType);
				values.put(SHOWTYPE, msg.pushShowType);
				values.put(SHOWTIME, msg.pushDisplayTime);
				values.put(TITLE, msg.pushTitle);
				values.put(CONTENT, msg.pushContent);
				values.put(ICONURL, msg.pushIconUrl);
				values.put(IMAGEURL, msg.pushImgUrl);
				values.put(DLURL, msg.pushLinkUrl);
				values.put(DLTYPE, msg.pushBehaviorType);
				values.put(CLEARL, msg.pushDelAble);
				String name = msg.gamePkgName;
				if (TextUtils.isEmpty(name)) {
					values.put(PACKAGE, "");
				} else {
					values.put(PACKAGE, name);
				}
				Integer ver = msg.gameVerInt;
				if (ver == null) {
					values.put(VERSION, 0);
				} else {
					values.put(VERSION, ver.intValue());
				}
				String pushpkg = msg.pushAssignGamePkgName;
				if (TextUtils.isEmpty(pushpkg)) {
					values.put(PUSHPKG, "");
				} else {
					values.put(PUSHPKG, pushpkg);
				}

				Byte colorType = msg.pushBgColorType;
				if (colorType == null) {
					values.put(COLORTYPE, DBMsg.COLORTYPE.DEFCOLOR);
				} else {
					values.put(COLORTYPE, colorType.byteValue());
				}

				values.put(ISSHOW, DBMsg.ISSHOWTYPE.NO);
				values.put(DOWNLOADID, 0);
				mSQLiteDatabase.insert(TABLE_PUSHMSG, null, values);
			}
			mSQLiteDatabase.setTransactionSuccessful();
			mSQLiteDatabase.endTransaction();
		} catch (Exception e) {
			SLog.e("SQL", "addPushMsg fail!!!");
		}

		return true;
	}

	// 更新消息列表的展示状态和展示时间
	public boolean updateShowMsg(long pushid, short isShow) {
		if (pushid <= 0 || mSQLiteDatabase == null || !isDBExits())
			return false;
		ContentValues values = new ContentValues();
		values.put(ISSHOW, isShow);
		String whereClause = PUSHID + " = ?";
		String[] whereValue = { String.valueOf(pushid) };
		try {
			mSQLiteDatabase.update(TABLE_PUSHMSG, values, whereClause,
					whereValue);
		} catch (Exception e) {
			SLog.e("SQL", "updateShowMsg fail!!!");
		}
		return true;
	}

	// 更新Downloadid(系统下载器的唯一标识)
	public boolean updateDownloadId(long pushid, long downloadid) {
		if (pushid <= 0 || mSQLiteDatabase == null || !isDBExits())
			return false;
		ContentValues values = new ContentValues();
		values.put(DOWNLOADID, downloadid);
		String whereClause = PUSHID + " = ?";
		String[] whereValue = { String.valueOf(pushid) };
		try {
			mSQLiteDatabase.update(TABLE_PUSHMSG, values, whereClause,
					whereValue);
		} catch (Exception e) {
			SLog.e("SQL", "updateDownloadId fail!!!");
		}
		return true;
	}

	// 通过Downloadid查询pushid
	public long getPushIDByDownloadID(long downloadid) {
		if (downloadid <= 0 || mSQLiteDatabase == null || !isDBExits())
			return 0;
		String[] columns = new String[] { PUSHID };
		ArrayList<Long> list = new ArrayList<Long>();
		String selection = DOWNLOADID + " = ?";
		String[] selectionArgs = { String.valueOf(downloadid) };
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PUSHMSG, columns, selection,
					selectionArgs, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					list.add(cursor.getLong(0));
				}
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		if (list.size() > 0)
			return list.get(0);
		else {
			return 0;
		}
	}

	// 读取所有没有弹过的PUSHMSG
	public ArrayList<DBMsg> getUnNotifyPushMsg() {
		if (mSQLiteDatabase == null || !isDBExits())
			return new ArrayList<DBMsg>();
		String[] columns = new String[] { PUSHID, ACTIONTYPE, SHOWTYPE,
				SHOWTIME, TITLE, CONTENT, ICONURL, IMAGEURL, DLURL, DLTYPE,
				CLEARL, PACKAGE, VERSION, PUSHPKG, COLORTYPE, ISSHOW,
				DOWNLOADID };
		String selection = ISSHOW + " = ?";
		String[] selectionArgs = { String.valueOf(DBMsg.ISSHOWTYPE.NO) };
		ArrayList<DBMsg> list = new ArrayList<DBMsg>();
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PUSHMSG, columns, selection,
					selectionArgs, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					DBMsg msg = new DBMsg();
					msg.pushid = cursor.getLong(0);
					msg.pushactiontype = cursor.getShort(1);
					msg.showtype = cursor.getShort(2);
					msg.showtime = cursor.getLong(3);
					msg.title = cursor.getString(4);
					msg.content = cursor.getString(5);
					msg.icon_url = cursor.getString(6);
					msg.image_url = cursor.getString(7);
					msg.download_url = cursor.getString(8);
					msg.download_type = cursor.getShort(9);
					msg.clear_flag = cursor.getShort(10);
					msg.pkg = cursor.getString(11);
					msg.versionCode = cursor.getInt(12);
					msg.pushpkg = cursor.getString(13);
					msg.colorType = (byte) cursor.getInt(14);
					msg.isshow = cursor.getShort(15);
					msg.downloadid = cursor.getLong(16);
					list.add(msg);
				}
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return list;
	}

	// 读取所有下载类型的PUSHMSG List
	public ArrayList<DBMsg> getApkPushMsg() {
		if (mSQLiteDatabase == null || !isDBExits())
			return new ArrayList<DBMsg>();
		String[] columns = new String[] { PUSHID, ACTIONTYPE, SHOWTYPE,
				SHOWTIME, TITLE, CONTENT, ICONURL, IMAGEURL, DLURL, DLTYPE,
				CLEARL, PACKAGE, VERSION, PUSHPKG, COLORTYPE, ISSHOW,
				DOWNLOADID };
		String selection = ACTIONTYPE + " = ?";
		String[] selectionArgs = { String.valueOf(DBMsg.ACTIONTYPE.DOWNLOAD) };
		String orderby = PUSHID + " DESC";
		ArrayList<DBMsg> list = new ArrayList<DBMsg>();
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PUSHMSG, columns, selection,
					selectionArgs, null, null, orderby);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					DBMsg msg = new DBMsg();
					msg.pushid = cursor.getLong(0);
					msg.pushactiontype = cursor.getShort(1);
					msg.showtype = cursor.getShort(2);
					msg.showtime = cursor.getLong(3);
					msg.title = cursor.getString(4);
					msg.content = cursor.getString(5);
					msg.icon_url = cursor.getString(6);
					msg.image_url = cursor.getString(7);
					msg.download_url = cursor.getString(8);
					msg.download_type = cursor.getShort(9);
					msg.clear_flag = cursor.getShort(10);
					msg.pkg = cursor.getString(11);
					msg.versionCode = cursor.getInt(12);
					msg.pushpkg = cursor.getString(13);
					msg.colorType = (byte) cursor.getInt(14);
					msg.isshow = cursor.getShort(15);
					msg.downloadid = cursor.getLong(16);
					list.add(msg);
				}

			}
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
		}

		return list;
	}

	// 查询所有downloadid不为0的PUSH MSG
	public ArrayList<DBMsg> getDownloadMsg() {
		if (mSQLiteDatabase == null || !isDBExits())
			return new ArrayList<DBMsg>();
		String[] columns = new String[] { PUSHID, ACTIONTYPE, SHOWTYPE,
				SHOWTIME, TITLE, CONTENT, ICONURL, IMAGEURL, DLURL, DLTYPE,
				CLEARL, PACKAGE, VERSION, PUSHPKG, COLORTYPE, ISSHOW,
				DOWNLOADID };
		String selection = DOWNLOADID + " > ?" + " and " + SHOWTIME + " < ?";
		String[] selectionArgs = {
				String.valueOf(0),
				String.valueOf(System.currentTimeMillis()
						- DateUtils.DAY_IN_MILLIS) };
		String orderby = PUSHID + " DESC";
		ArrayList<DBMsg> list = new ArrayList<DBMsg>();
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PUSHMSG, columns, selection,
					selectionArgs, null, null, orderby);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					DBMsg msg = new DBMsg();
					msg.pushid = cursor.getLong(0);
					msg.pushactiontype = cursor.getShort(1);
					msg.showtype = cursor.getShort(2);
					msg.showtime = cursor.getLong(3);
					msg.title = cursor.getString(4);
					msg.content = cursor.getString(5);
					msg.icon_url = cursor.getString(6);
					msg.image_url = cursor.getString(7);
					msg.download_url = cursor.getString(8);
					msg.download_type = cursor.getShort(9);
					msg.clear_flag = cursor.getShort(10);
					msg.pkg = cursor.getString(11);
					msg.versionCode = cursor.getInt(12);
					msg.pushpkg = cursor.getString(13);
					msg.colorType = (byte) cursor.getInt(14);
					msg.isshow = cursor.getShort(15);
					msg.downloadid = cursor.getLong(16);
					list.add(msg);
				}
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return list;
	}

}

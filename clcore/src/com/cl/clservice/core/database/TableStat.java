package com.cl.clservice.core.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cl.clservice.core.SLog;
import com.cl.clservice.core.bean.DBMsg;
import com.cl.clservice.core.bean.PushEvent;

public class TableStat extends TableBase {
	// 统计数据库表(S_PUSHID+S_SDKTYPE+S_EVENTCODE+S_EVENTTIME)
	private static final String TABLE_STAT = "STATICS";
	private static final String S_PUSHID = "s_pushid";
	private static final String S_SDKTYPE = "s_sdktype";
	private static final String S_SDKVERSION = "s_sdkversion";
	private static final String S_EVENTCODE = "s_eventcode";
	private static final String S_EVENTTIME = "s_eventtime";
	private static final String S_ISUPLOAD = "s_isupload";
	public static final String DB_CREATE_STAT = SQL_CREATE_TABLE + TABLE_STAT
			+ " (" + S_PUSHID + " INTEGER," + S_SDKTYPE + " INTEGER,"
			+ S_SDKVERSION + " INTEGER," + S_EVENTCODE + " INTEGER,"
			+ S_EVENTTIME + " INTEGER," + S_ISUPLOAD + " INTEGER)";
	public static final String DB_DELETE_STAT = SQL_DELETE_TABLE + TABLE_STAT;

	private SQLiteDatabase mSQLiteDatabase = null;

	public TableStat(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	// 添加一条统计记录
	public boolean addStaticsEvent(long pushid, int sdktype, int sdkver,
			int eventCode, long eventTime) {
		if (pushid <= 0 || mSQLiteDatabase == null || !isDBExits())
			return false;
		ContentValues values = new ContentValues();
		values.put(S_PUSHID, pushid);
		values.put(S_SDKTYPE, sdktype);
		values.put(S_SDKVERSION, sdkver);
		values.put(S_EVENTCODE, eventCode);
		values.put(S_EVENTTIME, eventTime);
		values.put(S_ISUPLOAD, DBMsg.ISUPLOADTYPE.NO);
		try {
			mSQLiteDatabase.insert(TABLE_STAT, null, values);
		} catch (Exception e) {
			SLog.e("SQL", "addStaticsEvent");
		}
		return true;
	}

	// 获取未上传的统计记录
	public ArrayList<PushEvent> getUnUploadStaticsEvent() {
		if (mSQLiteDatabase == null || !isDBExits())
			return new ArrayList<PushEvent>();
		String[] columns = new String[] { S_PUSHID, S_SDKTYPE, S_SDKVERSION,
				S_EVENTCODE, S_EVENTTIME, S_ISUPLOAD };
		String selection = S_ISUPLOAD + " =? ";
		String[] selectionArgs = { String.valueOf(DBMsg.ISUPLOADTYPE.NO) };
		ArrayList<PushEvent> list = new ArrayList<PushEvent>();

		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_STAT, columns, selection,
					selectionArgs, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					PushEvent event = new PushEvent();
					event.pushId = cursor.getLong(0);
					event.pushSdkType = (byte) cursor.getInt(1);
					event.pushSdkVer = cursor.getInt(2);
					event.eventCode = (short) cursor.getInt(3);
					event.eventTime = cursor.getLong(4);
					list.add(event);
				}
			}
		} catch (Exception e) {
			
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return list;
	}

//	// 更新已经上传的统计数据的数据库的状态
//	public void updateUploadStaticsEvent(ArrayList<PushEvent> list) {
//		if (list == null || list.size() == 0 || mSQLiteDatabase == null
//				|| !isDBExits())
//			return;
//		try {
//			mSQLiteDatabase.beginTransaction();
//			for (PushEvent event : list) {
//				ContentValues values = new ContentValues();
//				values.put(S_ISUPLOAD, DBMsg.ISUPLOADTYPE.YES);
//				String whereClause = S_PUSHID + " = ?";
//				String[] whereValue = { String.valueOf(event.pushId) };
//				mSQLiteDatabase.update(TABLE_STAT, values, whereClause,
//						whereValue);
//			}
//			mSQLiteDatabase.setTransactionSuccessful();
//			mSQLiteDatabase.endTransaction();
//		} catch (Exception e) {
//			SLog.e("SQL", "updateUploadStaticsEvent fail!!!");
//		}
//
//	}

	// 删除已经上传的统计数据的数据库的状态
	public void deleteUploadStaticsEvent(ArrayList<PushEvent> list) {
		if (list == null || list.size() == 0 || mSQLiteDatabase == null
				|| !isDBExits())
			return;
		try {
			mSQLiteDatabase.beginTransaction();
			for (PushEvent event : list) {
				String whereClause = S_PUSHID + " = ?";
				String[] whereValue = { String.valueOf(event.pushId) };
				mSQLiteDatabase.delete(TABLE_STAT, whereClause, whereValue);
			}
			mSQLiteDatabase.setTransactionSuccessful();
			mSQLiteDatabase.endTransaction();
		} catch (Exception e) {
			SLog.e("SQL", "updateUploadStaticsEvent error!!!");
		}
	}

	// 删除已经上传过的数据，避免数据库过大
	public void deleteUploadedData() {
		if (mSQLiteDatabase == null || !isDBExits())
			return;
		try {
			String whereClause = S_ISUPLOAD + " = ?";
			String[] whereValue = { String.valueOf(DBMsg.ISUPLOADTYPE.YES) };
			mSQLiteDatabase.delete(TABLE_STAT, whereClause, whereValue);
		} catch (Exception e) {
			SLog.e("SQL", "deleteUploadedData error!!!");
		}
	}

}

package com.kl.klservice.core.database;

import com.kl.klservice.core.SLog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TableCheckPushTime extends TableBase {
	// 获取PUSH的最后更新时间
	private static final String TABLE_CHECKPUSH_TIME = "CHECKPUSHTIME"; // 最后一次检查PUSH的时间
	private static final String CHECKPUSH_TIME = "checkpushlasttime"; //
	public static final String DB_CREATE_CHECKPUSH_TIME = SQL_CREATE_TABLE
			+ TABLE_CHECKPUSH_TIME + " (" + CHECKPUSH_TIME + " TEXT)";
	public static final String DB_DELETE_CHECKPUSH_TIME = SQL_DELETE_TABLE
			+ TABLE_CHECKPUSH_TIME;

	private SQLiteDatabase mSQLiteDatabase = null;

	public TableCheckPushTime(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	// CHECK PUSH 数据库操作
	private long addCheckPushLasttime(long time) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		ContentValues values = new ContentValues();
		values.put(CHECKPUSH_TIME, time);
		try {
			return mSQLiteDatabase.insert(TABLE_CHECKPUSH_TIME, null, values);
		} catch (Exception e) {
			SLog.e("SQL", "addCheckPushLasttime fail!!!");
			return -1;
		}

	}

	public long fetchCheckPushLastTime() {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		long interval = 0;
		String[] columns = new String[] { CHECKPUSH_TIME };
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_CHECKPUSH_TIME, columns, null,
					null, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					interval = cursor.getLong(0);
				}
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
		}

		return interval;
	}

	public long updateCheckPushLasttime(long time) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		if (fetchCheckPushLastTime() == 0) {
			return addCheckPushLasttime(time);
		} else {
			ContentValues values = new ContentValues();
			values.put(CHECKPUSH_TIME, time);
			try {
				return mSQLiteDatabase.update(TABLE_CHECKPUSH_TIME, values,
						null, null);
			} catch (Exception e) {
				SLog.e("SQL", "updateCheckPushLasttime fail!!!");
				return -1;
			}
		}
	}
}

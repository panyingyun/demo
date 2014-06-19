package com.cl.clservice.core.database;

import com.cl.clservice.core.SLog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TablePushIntervel extends TableBase {
	// 获取PUSH的时间间隔
	private static final String TABLE_PUSH_INTERVEL = "PUSHINTERVEL"; // push时间间隔
	private static final String PUSH_INTERVEL = "push_intervel"; //
	public static final String DB_CREATE_PUSH_INTERVEL = SQL_CREATE_TABLE
			+ TABLE_PUSH_INTERVEL + " (" + PUSH_INTERVEL + " TEXT)";
	public static final String DB_DELETE_PUSH_INTERVEL = SQL_DELETE_TABLE
			+ TABLE_PUSH_INTERVEL;

	private SQLiteDatabase mSQLiteDatabase = null;

	public TablePushIntervel(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	// push intervel 数据库操作
	private long addIntervel(long intervel) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		ContentValues values = new ContentValues();
		values.put(PUSH_INTERVEL, intervel);
		try {
			return mSQLiteDatabase.insert(TABLE_PUSH_INTERVEL, null, values);
		} catch (Exception e) {
			SLog.e("SQL", "addIntervel fail!!!");
			return -1;
		}

	}

	public long fetchIntervel() {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		long interval = 0;
		String[] columns = new String[] { PUSH_INTERVEL };
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PUSH_INTERVEL, columns, null,
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

	public long updateIntervel(long intervel) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		// SLog.e(TAG, "updateIntervel = " + intervel);
		if (fetchIntervel() == 0) {
			return addIntervel(intervel);
		} else {
			ContentValues values = new ContentValues();
			values.put(PUSH_INTERVEL, intervel);
			try {
				return mSQLiteDatabase.update(TABLE_PUSH_INTERVEL, values,
						null, null);
			} catch (Exception e) {
				SLog.e("SQL", "updateIntervel fail!!");
				return -1;
			}

		}
	}
}

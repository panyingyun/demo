package com.cl.clservice.core.database;

import com.cl.clservice.core.SLog;
import com.cl.clservice.core.bean.PUSHENABLE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TableEnable extends TableBase {
	private static final String TABLEENABLE = "TABLEENABLE";
	private static final String ENABLE = "enable"; //
	public static final String DB_CREATE_ENABLE = SQL_CREATE_TABLE
			+ TABLEENABLE + " (" + ENABLE + " TEXT)";
	public static final String DB_DELETE_ENABLE = SQL_DELETE_TABLE
			+ TABLEENABLE;

	private SQLiteDatabase mSQLiteDatabase = null;

	public TableEnable(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	private long addEnable(int enable) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		ContentValues values = new ContentValues();
		values.put(ENABLE, enable);
		try {
			return mSQLiteDatabase.insert(TABLEENABLE, null, values);
		} catch (Exception e) {
			SLog.e("SQL", "addEnable fail!!!");
			return -1;
		}

	}

	public int getEnable() {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		int enable = PUSHENABLE.NONE;
		String[] columns = new String[] { ENABLE };
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLEENABLE, columns, null, null,
					null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					enable = cursor.getInt(0);
				}
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
		}

		return enable;
	}

	public long updateEnable(int enable) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		if (getEnable() == PUSHENABLE.NONE) {
			return addEnable(enable);
		} else {
			ContentValues values = new ContentValues();
			values.put(ENABLE, enable);
			try {
				return mSQLiteDatabase.update(TABLEENABLE, values, null, null);
			} catch (Exception e) {
				SLog.e("SQL", "updateEnable fail!!!");
				return -1;
			}

		}
	}
}

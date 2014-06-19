package com.cl.clservice.core.database;

import com.cl.clservice.core.SLog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TableJar extends TableBase {
	// 获取Jar更新的最后时间
	private static final String TABLE_JAR_TIME = "JARLASTTIME"; // 最后一次检查jar的时间
	private static final String JAR_TIEM = "dexjarlasttime"; //
	public static final String DB_CREATE_DEXJAR_TIEM = SQL_CREATE_TABLE
			+ TABLE_JAR_TIME + " (" + JAR_TIEM + " TEXT)";
	public static final String DB_DELETE_DEXJAR_TIEM = SQL_DELETE_TABLE
			+ TABLE_JAR_TIME;

	private SQLiteDatabase mSQLiteDatabase = null;

	public TableJar(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	// JarDex time 数据库操作
	private long addJarLasttime(long time) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		ContentValues values = new ContentValues();
		values.put(JAR_TIEM, time);
		try {
			return mSQLiteDatabase.insert(TABLE_JAR_TIME, null, values);
		} catch (Exception e) {
			SLog.e("SQL", "addJarLasttime");
			return -1;
		}
	}

	public long fetchJarLastTime() {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		long interval = 0;
		String[] columns = new String[] { JAR_TIEM };
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_JAR_TIME, columns, null,
					null, null, null, null);
			while (cursor.moveToNext()) {
				interval = cursor.getLong(0);
			}
		} catch (Exception e) {

		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		return interval;
	}

	//更新安装apk时间
	public long updateJarLasttime(long time) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		if (fetchJarLastTime() == 0) {
			return addJarLasttime(time);
		} else {
			ContentValues values = new ContentValues();
			values.put(JAR_TIEM, time);
			try {
				return mSQLiteDatabase.update(TABLE_JAR_TIME, values, null, null);
			} catch (Exception e) {
				return -1;
			}
			
		}
	}
}

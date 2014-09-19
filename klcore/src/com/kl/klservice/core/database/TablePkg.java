package com.kl.klservice.core.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.kl.klservice.core.PkgInfo;
import com.kl.klservice.core.SLog;

public class TablePkg extends TableBase {
	// 注册过的package name 列表
	private static final String TABLE_PKG = "PACKAGE"; // 应用列表表结构
	private static final String PACKAGE_NAME = "pkgname"; // 包名（列表项）
	private static final String PKG_SDKTYPE = "sdktype"; // sdk类型
	private static final String PKG_CHANNEL = "channel";
	public static final String DB_CREATE_PACKAGE = SQL_CREATE_TABLE + TABLE_PKG
			+ " (" + PACKAGE_NAME + " TEXT," + PKG_SDKTYPE + " INTEGER,"
			+ PKG_CHANNEL + " TEXT)";
	public static final String DB_DELETE_PACKAGE = SQL_DELETE_TABLE + TABLE_PKG;
	private SQLiteDatabase mSQLiteDatabase = null;

	public TablePkg(SQLiteDatabase sqLiteDatabase) {
		mSQLiteDatabase = sqLiteDatabase;
	}

	// 添加Package
	public long addPkg(String packageName, byte sdktype, String channel) {
		if (mSQLiteDatabase == null || !isDBExits())
			return 0;
		ContentValues values = new ContentValues();
		values.put(PACKAGE_NAME, packageName);
		values.put(PKG_SDKTYPE, sdktype);
		if (TextUtils.isEmpty(channel)) {
			values.put(PKG_CHANNEL, "");
		} else {
			values.put(PKG_CHANNEL, channel);
		}
		try {
			return mSQLiteDatabase.insert(TABLE_PKG, null, values);
		} catch (Exception e) {
			SLog.e("SQL", "addPkg fail!!!");
			return -1;
		}

	}

	// 删除package
	public boolean deletePkg(String packageName) {
		if (mSQLiteDatabase == null || !isDBExits())
			return false;
		String where = PACKAGE_NAME + " = ?";
		String[] whereValue = { packageName };
		try {
			return mSQLiteDatabase.delete(TABLE_PKG, where, whereValue) > 0;
		} catch (Exception e) {
			SLog.e("SQL", "deletePkg fail!!!");
			return false;
		}

	}

	// 获取package列表
	public ArrayList<PkgInfo> fetchAllPackage() {
		if (mSQLiteDatabase == null || !isDBExits())
			return new ArrayList<PkgInfo>();
		ArrayList<PkgInfo> list = new ArrayList<PkgInfo>();
		String[] columns = new String[] { PACKAGE_NAME, PKG_SDKTYPE,
				PKG_CHANNEL };
		Cursor cursor = null;
		try {
			cursor = mSQLiteDatabase.query(TABLE_PKG, columns, null, null,
					null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					PkgInfo info = new PkgInfo(cursor.getString(0),
							(byte) cursor.getInt(1), cursor.getString(2));
					list.add(info);
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

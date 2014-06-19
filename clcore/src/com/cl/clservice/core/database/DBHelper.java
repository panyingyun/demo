package com.cl.clservice.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cl.clservice.core.GLOBAL;

/**
 * @ClassName: DBHelper
 * @Description: DBHelper
 * @author: panyingyun
 */

public class DBHelper {
	private static final String TAG = "DBHelper";
	// Define for DB
	private static final String DB_NAME = GLOBAL.getDBName(); // DB's name
	private static final int DB_VERSION = 10; // DB's Version

	private Context mContext = null;
	private SQLiteDatabase mSQLiteDatabase = null;
	private DatabaseHelper mDatabaseHelper = null;

	// ½Ó¿ÚÀà
	private TablePushMsg pushMsgHelper = null;
	private TableJar jarHelper = null;
	private TableCheckPushTime checkPushTimeHelper = null;
	private TablePushIntervel pushIntervelHelper = null;
	private TableStat statHelper = null;
	private TablePkg pkgHelper = null;
	private TableEnable enableHelper = null;

	private static class DatabaseHelper extends SDCardSQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TablePkg.DB_CREATE_PACKAGE);
			db.execSQL(TablePushMsg.DB_CREATE_MSG);
			db.execSQL(TablePushIntervel.DB_CREATE_PUSH_INTERVEL);
			db.execSQL(TableStat.DB_CREATE_STAT);
			db.execSQL(TableJar.DB_CREATE_DEXJAR_TIEM);
			db.execSQL(TableCheckPushTime.DB_CREATE_CHECKPUSH_TIME);
			db.execSQL(TableEnable.DB_CREATE_ENABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (newVersion >= 2) {
				// delete table
				db.execSQL(TablePkg.DB_DELETE_PACKAGE);
				db.execSQL(TablePushMsg.DB_DELETE_MSG);
				db.execSQL(TablePushIntervel.DB_DELETE_PUSH_INTERVEL);
				db.execSQL(TableStat.DB_DELETE_STAT);
				db.execSQL(TableJar.DB_DELETE_DEXJAR_TIEM);
				db.execSQL(TableCheckPushTime.DB_DELETE_CHECKPUSH_TIME);
				db.execSQL(TableEnable.DB_DELETE_ENABLE);
				// create table
				db.execSQL(TablePkg.DB_CREATE_PACKAGE);
				db.execSQL(TablePushMsg.DB_CREATE_MSG);
				db.execSQL(TablePushIntervel.DB_CREATE_PUSH_INTERVEL);
				db.execSQL(TableStat.DB_CREATE_STAT);
				db.execSQL(TableJar.DB_CREATE_DEXJAR_TIEM);
				db.execSQL(TableCheckPushTime.DB_CREATE_CHECKPUSH_TIME);
				db.execSQL(TableEnable.DB_CREATE_ENABLE);
			}

		}
	}

	public DBHelper(Context ctx) {
		mContext = ctx;
	}

	// open db
	public void open() {
		try {
			mDatabaseHelper = new DatabaseHelper(mContext);
			mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pushMsgHelper = new TablePushMsg(mSQLiteDatabase);
		jarHelper = new TableJar(mSQLiteDatabase);
		checkPushTimeHelper = new TableCheckPushTime(mSQLiteDatabase);
		pushIntervelHelper = new TablePushIntervel(mSQLiteDatabase);
		statHelper = new TableStat(mSQLiteDatabase);
		pkgHelper = new TablePkg(mSQLiteDatabase);
		enableHelper = new TableEnable(mSQLiteDatabase);
	}

	// close db
	public void close() {
		pushMsgHelper = null;
		jarHelper = null;
		checkPushTimeHelper = null;
		pushIntervelHelper = null;
		statHelper = null;
		pkgHelper = null;
		if (mSQLiteDatabase != null) {
			mSQLiteDatabase.close();
			mSQLiteDatabase = null;
		}

		if (mDatabaseHelper != null) {
			mDatabaseHelper.close();
			mDatabaseHelper = null;
		}
	}

	public TablePushMsg getPushMsgHelper() {
		return pushMsgHelper;
	}

	public TableJar getJarHelper() {
		return jarHelper;
	}

	public TableCheckPushTime getCheckPushTimeHelper() {
		return checkPushTimeHelper;
	}

	public TablePushIntervel getPushIntervelHelper() {
		return pushIntervelHelper;
	}

	public TableStat getStatHelper() {
		return statHelper;
	}

	public TablePkg getPkgHelper() {
		return pkgHelper;
	}
	
	public TableEnable getEnableHelper() {
		return enableHelper;
	}
}

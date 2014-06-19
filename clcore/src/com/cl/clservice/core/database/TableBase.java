package com.cl.clservice.core.database;

import java.io.File;

import com.cl.clservice.core.GLOBAL;

public class TableBase {
	public static final String SQL_DELETE_TABLE = "drop table if exists ";
	public static final String SQL_CREATE_TABLE = "create table if not exists ";

	// ºÏ≤ÈDB «∑Ò¥Ê‘⁄
	public static boolean isDBExits() {
		File file = new File(GLOBAL.PUSHSDK +File.separator+GLOBAL.getDBName());
		return file.exists();
	}
}

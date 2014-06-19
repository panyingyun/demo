package com.cl.clservice.core.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Environment;
import android.text.TextUtils;

import com.cl.clservice.core.FileUtils;
import com.cl.clservice.core.SLog;

public class DexJarDownload implements Runnable {

	private static final String TAG = "DexJarDownload";
	private static String JAR_DIR = Environment.getExternalStorageDirectory()
			.toString() + File.separator + ".android/download/jar/";

	private String url;
	private int curVersion;
	private OnDownloadSuccess ods;

	public DexJarDownload(int curVer, String url, OnDownloadSuccess onDS) {
		this.curVersion = curVer;
		this.url = url;
		this.ods = onDS;
	}

	@Override
	public void run() {
		// ����������汾�ȱ��ص� �ǾͲ�Ҫ������
		SLog.e(TAG, "curVer = " + curVersion + " , serVer = "
				+ getVersionDexjar(url));
		if (curVersion >= getVersionDexjar(url)) {

			SLog.e(TAG, "donot need update dexjar!!!");
			return;
		}
		SLog.e(TAG, "need update dexjar!!!");
		File jardir = new File(JAR_DIR);
		if (!jardir.exists())
			jardir.mkdirs();
		InputStream is = null;
		try {
			is = new DefaultHttpClient().execute(new HttpGet(url)).getEntity()
					.getContent();
			saveJar(is, getJarname(url));
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����ļ����Ĺ��� ȡ���汾��
	// dexjar���̶ֹ�Ϊ "clcoredex-XX.jar"
	// XX�����Ű汾��1,2,3,4�������ε���������
	private int getVersionDexjar(String dexjar) {
		int version = -1;
		if (TextUtils.isEmpty(dexjar) || dexjar.length() < 15
				|| !dexjar.endsWith(".jar"))
			return -1;
		try {
			int posStart = dexjar.lastIndexOf("-");
			int posEnd = dexjar.lastIndexOf(".");
			String verStr = dexjar.substring(posStart + 1, posEnd);
			version = Integer.valueOf(verStr);
		} catch (Exception e) {
			version = -1;
		}
		return version;
	}

	private String getJarname(String url) {
		return FileUtils.getJarnameFromURL(url);
	}

	// ������д�뵽sdcard
	public void saveJar(InputStream in, String filename) throws IOException {
		if (TextUtils.isEmpty(filename))
			return;
		// д��ͬһ��Ŀ¼��
		FileOutputStream fos = new FileOutputStream(JAR_DIR + filename + ".tmp");
		byte[] buff = new byte[1024];
		int readed = -1;
		while ((readed = in.read(buff)) > 0) {
			fos.write(buff, 0, readed);
		}
		fos.flush();
		fos.close();
		File jar = new File(JAR_DIR + filename + ".tmp");
		if (jar.length() > 0) {
			jar.renameTo(new File(JAR_DIR + filename));
			if (ods != null)
				ods.onDownloadSuccess(0, true);
		} else {
			// ����ʧ�ܻ���дʧ�ܵ�ʱ��ֱ��ɾ��
			jar.delete();
		}
	}
}

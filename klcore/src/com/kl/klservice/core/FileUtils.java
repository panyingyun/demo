package com.kl.klservice.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {

	// 获取下载apk的完整路径
	public static String getApkPathFromURL(String url) {
		if (TextUtils.isEmpty(url))
			return null;
		String name = getFilenameFromURL(url);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File path = new File(GLOBAL.APKDOWNLOAD);
			if (!path.exists()) {
				path.mkdirs();
			}
		}
		return GLOBAL.APKDOWNLOAD +File.separator+ name;
	}

	// 完整URL路径字符串的MD5做为文件名
	public static String getFilenameFromURL(String url) {
		return MD5Utils.toMd5(url.getBytes());
	}

	// 判断是否存在该apk
	public static boolean isExitAPK(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		File file = new File(path);
		return file.exists();
	}

	// 获取下载图片的完整路径
	public static String getPicPathFromURL(String url) {
		if (TextUtils.isEmpty(url))
			return null;
		String name = getFilenameFromURL(url);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File path = new File(GLOBAL.PICDOWNLOAD);
			if (!path.exists()) {
				path.mkdirs();
			}
		}
		return GLOBAL.PICDOWNLOAD +File.separator+ name;
	}

	// 保存为PNG
	public static boolean Bitmap2PNG(Bitmap bmp, String filepath) {
		if (bmp == null || TextUtils.isEmpty(filepath))
			return false;

		OutputStream stream = null;
		try {
			File file = new File(filepath);
			File dir = new File(file.getParent());
			if (!dir.exists())
				dir.mkdirs();
			if (file.exists())
				return true;

			stream = new FileOutputStream(filepath);

			if (bmp.compress(Bitmap.CompressFormat.PNG, 85, stream)) {
				stream.flush();
				stream.close();
				return true;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	// 读取PNG
	public static Bitmap PNGToBitmap(String path) {
		if (TextUtils.isEmpty(path))
			return null;
		File file = new File(path);
		if (!file.exists()) {
			SLog.w("FileUtils", "icon cache file is not exits");
			return null;
		}
		Bitmap bm = null;
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inDither = false;
		bfoOptions.inPurgeable = true;
		bfoOptions.inInputShareable = true;
		bfoOptions.inTempStorage = new byte[32 * 1024];

		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			SLog.d("FileUtils", "icon cache file is not exits");
		}

		try {
			if (fs != null)
				bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
						bfoOptions);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}

		return bm;
	}

	// 从完整URL路径中湖区文件名
	public static String getJarnameFromURL(String url) {
		int start = url.lastIndexOf("/");
		if (start != -1) {
			return url.substring(start + 1, url.length());
		} else {
			return null;
		}
	}
}

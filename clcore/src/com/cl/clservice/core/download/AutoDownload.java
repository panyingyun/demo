package com.cl.clservice.core.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import android.os.Environment;
import android.os.StatFs;

import com.cl.clservice.core.FileUtils;
import com.cl.clservice.core.SLog;

public class AutoDownload implements Runnable {
	private static final String TAG = "AutoDownload";
	/** 下载缓冲区 */
	private static final int BUFFER_LEN = 1024;

	private File downloadFile; // 下载后的文件
	private File downloadTmpFile; // 下载临时文件
	private String downloadAPKPath;
	// 下载输入参数
	private String downloadUrl;
	private long pushID;
	private OnDownloadSuccess ods;
	private AutoDownloadClient mHttpClient;

	/*** 当前下载的位置 */
	private long downloadPosition = 0;

	/** * 游戏的APK大小 */
	private long downloadFileSize;

	private boolean isRunning = false;

	private static final int DOWNLOAD_NONE = 1000;
	private static final int DOWNLOAD_FAIL = 1001;
	private static final int DOWNLOAD_SUCCESS = 1002;
	private int downloadState;

	public AutoDownload(long pushid, String loadUrl, OnDownloadSuccess onDS) {
		downloadUrl = loadUrl;
		pushID = pushid;
		ods = onDS;
	}

	public String getAPKPath() {
		return downloadAPKPath;
	}

	public long getDownloadPosition() {
		return downloadPosition;
	}

	public boolean isDownloadSuccess() {
		return (downloadState == DOWNLOAD_SUCCESS);
	}

	@Override
	public void run() {
		isRunning = true;
		downloadState = DOWNLOAD_NONE;
		mHttpClient = new AutoDownloadClient(downloadUrl);
		SLog.e(TAG, "download apk Url = " + downloadUrl);
		try {
			// 创建文件*.apk
			downloadAPKPath = FileUtils.getApkPathFromURL(downloadUrl);
			SLog.e(TAG, "download apk downloadAPKPath = " + downloadAPKPath);
			downloadFile = new File(downloadAPKPath);
			downloadTmpFile = new File(downloadAPKPath + ".temp"); // 下载的临时文件
			// 已经下载过的不重复下载
			if (!downloadFile.exists()) {
				File parentFile = downloadTmpFile.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				downloadTmpFile.createNewFile();
				// 开始下载
				boolean isSuccess = download(downloadPosition);
				downloadState = isSuccess ? DOWNLOAD_SUCCESS : DOWNLOAD_FAIL;
			}
		} catch (SocketTimeoutException e) {
			downloadTmpFile.delete();
			downloadState = DOWNLOAD_FAIL;
		} catch (MalformedURLException e) {
			downloadTmpFile.delete();
			downloadState = DOWNLOAD_FAIL;
		} catch (IOException e) {
			downloadTmpFile.delete();
			downloadState = DOWNLOAD_FAIL;
		} catch (Exception e) {
			downloadTmpFile.delete();
			downloadState = DOWNLOAD_FAIL;
		} finally {
			if (mHttpClient != null) {
				mHttpClient.close();
				mHttpClient = null;
			}
		}
		// 如果是下载成功
		if (downloadState == DOWNLOAD_SUCCESS) {
			downloadTmpFile.renameTo(downloadFile);
		}
		if (ods != null && downloadState != DOWNLOAD_NONE) {
			ods.onDownloadSuccess(pushID, (downloadState == DOWNLOAD_SUCCESS));
		}
		SLog.e(TAG, "downloadState(=1002 success) = " + downloadState);
	}

	/**
	 * 检查SD是否有足够空间
	 * 
	 * @param loadSize
	 * @return
	 */
	private boolean checkSDSpaceIsEnough(long loadSize) {
		if (getSDAvailaleSize() > (loadSize * 2)) {
			return true;
		}
		return false;
	}

	/**
	 * 断点续传
	 * 
	 * @param serverPos
	 * @throws SocketTimeoutException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private boolean download(long serverPos) throws SocketTimeoutException,
			MalformedURLException, IOException {
		boolean isSuccess = false;
		InputStream inputStream = null;
		RandomAccessFile randomAccessFile = null;
		try {
			inputStream = mHttpClient.getInputStream(serverPos);
		} catch (Exception e) {
			SLog.e(TAG, "download fail(1)= " + downloadUrl);
			return false;
		}
		try {
			randomAccessFile = new RandomAccessFile(downloadTmpFile, "rw");
			downloadFileSize = mHttpClient.getContentLength();
			if (!checkSDSpaceIsEnough(downloadFileSize)) {
				SLog.e(TAG, "download fail(2)= " + downloadUrl);
				return false;
			}

			downloadFileSize = downloadFileSize + serverPos; // 文件实际大小
			byte[] buf = new byte[BUFFER_LEN];// 从服务端读取的byte流
												// //缓存
			int len = 0;// 从服务端读取的byte长度
			randomAccessFile.seek(downloadPosition);
			randomAccessFile.setLength(downloadPosition);

			while (isRunning && (-1 != (len = inputStream.read(buf)))) {
				randomAccessFile.write(buf, 0, len);
				downloadPosition += len;
			}
			isSuccess = (randomAccessFile.length() == downloadFileSize);
			SLog.e(TAG, "file length = " + randomAccessFile.length()
					+ ",downloadFileSize = " + downloadFileSize);
		} catch (IOException e) {
			SLog.e(TAG, "download fail(3)= " + downloadUrl);
			isSuccess = false;
		} finally {
			try {
				if (randomAccessFile != null)
					randomAccessFile.close();
			} catch (Exception ex) {
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (Exception exp) {
				}
			}
		}
		return isSuccess;
	}

	/**
	 * 获取SD卡有效空间
	 * 
	 * @return
	 */
	public static long getSDAvailaleSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}
}

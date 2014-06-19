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
	/** ���ػ����� */
	private static final int BUFFER_LEN = 1024;

	private File downloadFile; // ���غ���ļ�
	private File downloadTmpFile; // ������ʱ�ļ�
	private String downloadAPKPath;
	// �����������
	private String downloadUrl;
	private long pushID;
	private OnDownloadSuccess ods;
	private AutoDownloadClient mHttpClient;

	/*** ��ǰ���ص�λ�� */
	private long downloadPosition = 0;

	/** * ��Ϸ��APK��С */
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
			// �����ļ�*.apk
			downloadAPKPath = FileUtils.getApkPathFromURL(downloadUrl);
			SLog.e(TAG, "download apk downloadAPKPath = " + downloadAPKPath);
			downloadFile = new File(downloadAPKPath);
			downloadTmpFile = new File(downloadAPKPath + ".temp"); // ���ص���ʱ�ļ�
			// �Ѿ����ع��Ĳ��ظ�����
			if (!downloadFile.exists()) {
				File parentFile = downloadTmpFile.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				downloadTmpFile.createNewFile();
				// ��ʼ����
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
		// ��������سɹ�
		if (downloadState == DOWNLOAD_SUCCESS) {
			downloadTmpFile.renameTo(downloadFile);
		}
		if (ods != null && downloadState != DOWNLOAD_NONE) {
			ods.onDownloadSuccess(pushID, (downloadState == DOWNLOAD_SUCCESS));
		}
		SLog.e(TAG, "downloadState(=1002 success) = " + downloadState);
	}

	/**
	 * ���SD�Ƿ����㹻�ռ�
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
	 * �ϵ�����
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

			downloadFileSize = downloadFileSize + serverPos; // �ļ�ʵ�ʴ�С
			byte[] buf = new byte[BUFFER_LEN];// �ӷ���˶�ȡ��byte��
												// //����
			int len = 0;// �ӷ���˶�ȡ��byte����
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
	 * ��ȡSD����Ч�ռ�
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

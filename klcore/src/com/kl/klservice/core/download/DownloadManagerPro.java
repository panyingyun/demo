package com.kl.klservice.core.download;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.kl.klservice.core.FileUtils;
import com.kl.klservice.core.SLog;

/**
 * 系统下载，通过downloadid获取名字和path downloadmanagerPro
 */
public class DownloadManagerPro {

	private static final String TAG = "DownloadManagerPro";
	public static final Uri CONTENT_URI = Uri
			.parse("content://downloads/my_downloads");

	private DownloadManager downloadManager;

	public DownloadManagerPro(DownloadManager downloadManager) {
		this.downloadManager = downloadManager;
	}

	// 系统下载APK
	public long sysDownload(String url, String title, String content) {
		long downloadid = -1;
		Uri uri = Uri.parse(url);
		SLog.e(TAG, "system download url = " + url);
		DownloadManager.Request request = new Request(uri);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		if (!TextUtils.isEmpty(title)) {
			request.setTitle(title);
		}
		if (!TextUtils.isEmpty(content)) {
			request.setDescription(content);
		}
		request.setMimeType("application/vnd.android.package-archive");
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS,
				FileUtils.getFilenameFromURL(url));
		try {
			downloadid = downloadManager.enqueue(request);
		} catch (Exception e) {
			SLog.e(TAG, "system download error");
		}		
		return downloadid;
	}

	// 取消下载
	public int cancelDownload(long downloadid) {
		return downloadManager.remove(downloadid);
	}

	/**
	 * get download status
	 * 
	 * @param downloadId
	 * @return
	 */
	public int getStatusById(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_STATUS);
	}

	/**
	 * get downloaded byte, total byte
	 * 
	 * @param downloadId
	 * @return a int array with two elements
	 *         <ul>
	 *         <li>result[0] represents downloaded bytes, This will initially be
	 *         -1.</li>
	 *         <li>result[1] represents total bytes, This will initially be -1.</li>
	 *         </ul>
	 */
	public int[] getDownloadBytes(long downloadId) {
		int[] bytesAndStatus = getBytesAndStatus(downloadId);
		return new int[] { bytesAndStatus[0], bytesAndStatus[1] };
	}

	/**
	 * get downloaded byte, total byte and download status
	 * 
	 * @param downloadId
	 * @return a int array with three elements
	 *         <ul>
	 *         <li>result[0] represents downloaded bytes, This will initially be
	 *         -1.</li>
	 *         <li>result[1] represents total bytes, This will initially be -1.</li>
	 *         <li>result[2] represents download status, This will initially be
	 *         0.</li>
	 *         </ul>
	 */
	public int[] getBytesAndStatus(long downloadId) {
		int[] bytesAndStatus = new int[] { -1, -1, 0 };
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				bytesAndStatus[0] = c
						.getInt(c
								.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
				bytesAndStatus[1] = c
						.getInt(c
								.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				bytesAndStatus[2] = c.getInt(c
						.getColumnIndex(DownloadManager.COLUMN_STATUS));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return bytesAndStatus;
	}

	/**
	 * get download file name
	 * 
	 * @param downloadId
	 * @return
	 */
	// public String getFileName(long downloadId) {
	// return getString(downloadId, DownloadManager.COLUMN_LOCAL_FILENAME);
	// }

	/**
	 * get local uri
	 */
	public String getLocalUri(long downloadId) {
		return getString(downloadId, DownloadManager.COLUMN_LOCAL_URI);
	}

	/**
	 * get download uri
	 * 
	 * @param downloadId
	 * @return
	 */
	public String getUri(long downloadId) {
		return getString(downloadId, DownloadManager.COLUMN_URI);
	}

	/**
	 * get failed code or paused reason
	 * 
	 * @param downloadId
	 * @return <ul>
	 *         <li>if status of downloadId is
	 *         {@link DownloadManager#STATUS_PAUSED}, return
	 *         {@link #getPausedReason(long)}</li>
	 *         <li>if status of downloadId is
	 *         {@link DownloadManager#STATUS_FAILED}, return
	 *         {@link #getErrorCode(long)}</li>
	 *         <li>if status of downloadId is neither
	 *         {@link DownloadManager#STATUS_PAUSED} nor
	 *         {@link DownloadManager#STATUS_FAILED}, return 0</li>
	 *         </ul>
	 */
	public int getReason(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_REASON);
	}

	/**
	 * get paused reason
	 * 
	 * @param downloadId
	 * @return <ul>
	 *         <li>if status of downloadId is
	 *         {@link DownloadManager#STATUS_PAUSED}, return one of
	 *         {@link DownloadManager#PAUSED_WAITING_TO_RETRY}<br/>
	 *         {@link DownloadManager#PAUSED_WAITING_FOR_NETWORK}<br/>
	 *         {@link DownloadManager#PAUSED_QUEUED_FOR_WIFI}<br/>
	 *         {@link DownloadManager#PAUSED_UNKNOWN}</li>
	 *         <li>else return {@link DownloadManager#PAUSED_UNKNOWN}</li>
	 *         </ul>
	 */
	public int getPausedReason(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_REASON);
	}

	/**
	 * get failed error code
	 * 
	 * @param downloadId
	 * @return one of {@link DownloadManager#ERROR_*}
	 */
	public int getErrorCode(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_REASON);
	}

	/**
	 * get string column
	 * 
	 * @param downloadId
	 * @param columnName
	 * @return
	 */
	private String getString(long downloadId, String columnName) {
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		String result = null;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getString(c.getColumnIndex(columnName));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}

	/**
	 * get int column
	 * 
	 * @param downloadId
	 * @param columnName
	 * @return
	 */
	private int getInt(long downloadId, String columnName) {
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		int result = -1;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getInt(c.getColumnIndex(columnName));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}
}
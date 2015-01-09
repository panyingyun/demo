package com.cl.clservice.core.download;

import java.io.InputStream;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cl.clservice.core.FileUtils;
import com.cl.clservice.core.SLog;
import com.cl.clservice.core.UIUtils;

public class PicDownload implements Runnable {
	private static final String TAG = "PicDownload";
	private String url = null;

	public PicDownload(String url) {
		this.url = url;
	}

	// 下载网络图片
	public static Bitmap get(String url) {
		if (url == null) {
			return null;
		}
		Bitmap bm = null;
		InputStream is = null;
		try {
			is = new DefaultHttpClient().execute(new HttpGet(url)).getEntity()
					.getContent();
			bm = BitmapFactory.decodeStream(is);
		} catch (Exception e) {

		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
		return bm;
	}

	@Override
	public void run() {
		//空间不足时，不下载icon
		if(!UIUtils.isSdSpaceEnough()){
			return;
		}
		String path = FileUtils.getPicPathFromURL(url);
		if (url != null && path != null) {
			SLog.e(TAG, "download pic url = " + url);
			Bitmap bm = get(url);
			if (bm == null)
				bm = get(url);
			FileUtils.Bitmap2PNG(bm, path);
		}
	}
}

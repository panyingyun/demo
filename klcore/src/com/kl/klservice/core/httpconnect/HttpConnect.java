package com.kl.klservice.core.httpconnect;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.SystemClock;

import com.android.plservice.fd.codec.bean.UserAgent;
import com.android.plservice.fd.codec.bean.tlv.decode.decoders.BeanTLVDecoder;
import com.android.plservice.fd.codec.bean.tlv.encode.TLVEncodeContext;
import com.android.plservice.fd.codec.bean.tlv.encode.encoders.BeanTLVEncoder;
import com.android.plservice.fd.util.ByteUtils;
import com.kl.klservice.core.GLOBAL;
import com.kl.klservice.core.SLog;
import com.kl.klservice.core.bean.GetMsgListReq;
import com.kl.klservice.core.bean.GetMsgListResp;
import com.kl.klservice.core.bean.PushEvent;
import com.kl.klservice.core.bean.UploadPushLogReq;
import com.kl.klservice.core.bean.UploadPushLogResp;

/**
 * 
 * @author PanYingYun
 * 
 */
public class HttpConnect {
	private static String TAG = "HttpConnect";

	private static int RETRY_TIME = 3;
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 15000;

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_CONNECTION);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIMEOUT_SOCKET);
		return httpClient;
	}

	private static Object postMethod(Object request, String uriString,
			Class<?> resptype) {
		// SLog.e(TAG, "http uriString :" + uriString);
		Object resp = null;
		/* HTTP Post */
		HttpPost httpRequest = new HttpPost(uriString);
		httpRequest.setHeader("content-type", "application/x-tar");

		BeanTLVEncoder beanEncoder = new BeanTLVEncoder();
		TLVEncodeContext encode = beanEncoder.getEncodeContextFactory()
				.createEncodeContext(request.getClass(), null);
		byte[] data = ByteUtils.union(beanEncoder.encode(request, encode));
		// SLog.e(TAG, "body byte length:" + data.length);
		// SLog.e(TAG, "body byte:" + data);

		httpRequest.setEntity(new InputStreamEntity(new ByteArrayInputStream(
				data), data.length));

		HttpClient httpClient = getHttpClient();
		// SLog.i(TAG, "Request is initiated,is requesting...");
		//
		HttpResponse httpResponse = null;
		int status = -1;
		int time = 0;
		do {
			try {
				httpResponse = httpClient.execute(httpRequest);
				status = httpResponse.getStatusLine().getStatusCode();
				break;
			} catch (Exception e) {
				time++;
				if (time < RETRY_TIME) {
					SystemClock.sleep(1000);
					continue;
				}
				SLog.e(TAG, "get response status error time = " + time);
			}
		} while (time < RETRY_TIME);

		if (status == 200) {
			//
			// SLog.i(TAG, "Is to decode the entity ...");
			// InputStream is = null;
			try {
				byte[] body = EntityUtils.toByteArray(httpResponse.getEntity());
				// SLog.e(TAG,"body = "+ ByteUtils.bytesAsHexString(body,
				// 1000));
				// SLog.e(TAG, "body length = " + body.length);
				BeanTLVDecoder beanDecoder = new BeanTLVDecoder();
				resp = beanDecoder.decode(body.length, body,
						beanDecoder.getDecodeContextFactory()
								.createDecodeContext(resptype, null));
			} catch (Exception e) {
				SLog.e(TAG, "decode error = " + e);
			}
		} else {
			SLog.e(TAG, "Returns status is: " + status);
		}

		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		return resp;
	}

	// 统计PUSH行为
	public static boolean uploadPush(UserAgent ua, long logUploadTime,
			ArrayList<PushEvent> pushEventList) {
		UploadPushLogResp resp = null;
		UploadPushLogReq req = new UploadPushLogReq();
		req.setUserAgent(ua);
		req.setPushEventList(pushEventList);
		req.setLogUploadTime(logUploadTime);
		resp = (UploadPushLogResp) HttpConnect.postMethod(req,
				GLOBAL.getJoloPushDTS() + "uploadpushlog",
				UploadPushLogResp.class);
		if (resp != null) {
			// SLog.e(TAG, "uploadPushMsg success resp = "+resp);
			return true;
		} else {
			// SLog.e(TAG, "uploadPushMsg fail resp = "+resp);
			return false;
		}
	}

	// 获取消息列表
	public static GetMsgListResp getPushMsgList(UserAgent ua, long pushID,
			byte sdkType, int sdkVer, long installTime) {
		GetMsgListReq req = new GetMsgListReq();
		req.setUserAgent(ua);
		req.setPushId(pushID);
		req.setPushSdkType(sdkType);
		req.setPushSdkVer(sdkVer);
		req.setInstallTime(installTime);
		GetMsgListResp resp = (GetMsgListResp) HttpConnect.postMethod(req,
				GLOBAL.getPushURL() + "getmsglist", GetMsgListResp.class);
		if (resp != null && resp.getResponseCode() == 200
				&& resp.getPushSwitch() != null) {
			// SLog.e(TAG, "getPushMsgList success resp = "+resp);
			return resp;
		} else {
			// SLog.e(TAG, "getPushMsgList fail resp = "+resp);
			return null;
		}
	}
}
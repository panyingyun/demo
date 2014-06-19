package com.cl.clservice.core.bean;

import java.util.ArrayList;

import com.android.plservice.fd.codec.bean.BaseReq;
import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * 
 * @author jiangdehua
 * @version 1.0 2013-7-19
 */
public class UploadPushLogReq extends BaseReq {
	@TLVAttribute(tag = 10019004)
	private Long logUploadTime; // 终端本地时间
	@TLVAttribute(tag = 10029011)
	private ArrayList<PushEvent> pushEventList;// push行为

	public Long getLogUploadTime() {
		return logUploadTime;
	}

	public void setLogUploadTime(Long logUploadTime) {
		this.logUploadTime = logUploadTime;
	}

	public ArrayList<PushEvent> getPushEventList() {
		return pushEventList;
	}

	public void setPushEventList(ArrayList<PushEvent> pushEventList) {
		this.pushEventList = pushEventList;
	}

}
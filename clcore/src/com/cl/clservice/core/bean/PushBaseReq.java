package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.AbstractCommonBean;
import com.android.plservice.fd.codec.bean.UserAgent;
import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

public class PushBaseReq extends AbstractCommonBean {
	@TLVAttribute(tag = 10020001)
	private UserAgent userAgent;
	@TLVAttribute(tag = 10110001)
	private Byte pushSdkType;// 1=ƽ̨��2=sdk,3=ƽ̨+sdk��4=�����Ʒ��5=SDK+�����Ʒ��6=ƽ̨+�����Ʒ
	@TLVAttribute(tag = 10110015)
	private Integer pushSdkVer;
	@TLVAttribute(tag=1028)
	private Long installTime;//��װʱ��
	
	public UserAgent getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
	}

	public Byte getPushSdkType() {
		return pushSdkType;
	}

	public void setPushSdkType(Byte pushSdkType) {
		this.pushSdkType = pushSdkType;
	}

	public Integer getPushSdkVer() {
		return pushSdkVer;
	}

	public void setPushSdkVer(Integer pushSdkVer) {
		this.pushSdkVer = pushSdkVer;
	}
	
	public Long getInstallTime() {
		return installTime;
	}
	public void setInstallTime(Long installTime) {
		this.installTime = installTime;
	}
}

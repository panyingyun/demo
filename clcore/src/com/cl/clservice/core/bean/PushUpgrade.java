package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

public class PushUpgrade {
	@TLVAttribute(tag = 10110015)
	public Integer pushSdkVer;// ����˵İ汾
	@TLVAttribute(tag = 10110014, charset = "utf-8")
	public String pushUpgradeUrl;// jar�ļ����ص�ַ
}

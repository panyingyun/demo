package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * push项目的push日志
 * 
 * @author jiangdehua
 * @version 1.0 2013-7-19
 */
public class PushEvent {
	@TLVAttribute(tag = 10019002)
	public Long eventTime;// 手机端操作时间
	@TLVAttribute(tag = 10019001)
	public Short eventCode;
	/*
	 * 1=下发 2=展现 3=点击 2013-08-23 增加 eventCode 4=自动下载成功 5=自动下载失败 6系统下载成功 7=系统下载失败
	 */
	@TLVAttribute(tag = 10110000)
	public Long pushId;
	@TLVAttribute(tag = 10110001)
	public Byte pushSdkType;
	// 1=平台，2=sdk,3=平台+sdk，4=打包产品；5=SDK+打包产品；6=平台+打包产品
	@TLVAttribute(tag = 10110015)
	public Integer pushSdkVer;// pushSdk版本 2013-08-13 新增
}

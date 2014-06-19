package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * 获取消息以及版本自更新的消息
 * 如果有版本自更新的消息
 * 优先处理版本升级，版本升级完后，终端再向服务端请求push部署的消息
 * url: /getmsglist
 * @author jiangdehua
 * @version 1.0 2013-10-15
 */
public class GetMsgListReq extends PushBaseReq{
	@TLVAttribute(tag = 10110000)
	private Long pushId;// 终端上一次接收到的pushId，null代表终端从来没有来过

	public Long getPushId() {
		return pushId;
	}

	public void setPushId(Long pushId) {
		this.pushId = pushId;
	}
}

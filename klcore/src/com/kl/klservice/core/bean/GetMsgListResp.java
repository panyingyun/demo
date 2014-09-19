package com.kl.klservice.core.bean;

import java.util.ArrayList;

import com.android.plservice.fd.codec.bean.BaseResp;
import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * 消息列表协议响应
 * 
 * @author jiangdehua
 * @version 1.0 2013-10-15
 */
public class GetMsgListResp extends BaseResp {
	@TLVAttribute(tag = 1022)
	private Long serverTime;// 服务器当前时间，可用于客户端校对时间，毫秒
	@TLVAttribute(tag = 10110009)
	private Long pushTimeInterval;// 告诉终端push请求的时间间隔,毫秒
	@TLVAttribute(tag = 10110010)
	private Byte pushSwitch;// 1=开 2=关闭 保留字段，默认是1
	@TLVAttribute(tag = 10120000)
	private ArrayList<PushMsg> pushMsgList;// 消息组合,如果pushUpgrade不为null，那么这里为null
	@TLVAttribute(tag = 10120001)
	private PushUpgrade pushUpgrade;// 版本升级信息

	public Long getServerTime() {
		return serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}

	public Long getPushTimeInterval() {
		return pushTimeInterval;
	}

	public void setPushTimeInterval(Long pushTimeInterval) {
		this.pushTimeInterval = pushTimeInterval;
	}

	public Byte getPushSwitch() {
		return pushSwitch;
	}

	public void setPushSwitch(Byte pushSwitch) {
		this.pushSwitch = pushSwitch;
	}

	public ArrayList<PushMsg> getPushMsgList() {
		return pushMsgList;
	}

	public void setPushMsgList(ArrayList<PushMsg> pushMsgList) {
		this.pushMsgList = pushMsgList;
	}

	public PushUpgrade getPushUpgrade() {
		return pushUpgrade;
	}

	public void setPushUpgrade(PushUpgrade pushUpgrade) {
		this.pushUpgrade = pushUpgrade;
	}
}

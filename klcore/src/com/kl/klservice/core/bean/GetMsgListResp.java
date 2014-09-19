package com.kl.klservice.core.bean;

import java.util.ArrayList;

import com.android.plservice.fd.codec.bean.BaseResp;
import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * ��Ϣ�б�Э����Ӧ
 * 
 * @author jiangdehua
 * @version 1.0 2013-10-15
 */
public class GetMsgListResp extends BaseResp {
	@TLVAttribute(tag = 1022)
	private Long serverTime;// ��������ǰʱ�䣬�����ڿͻ���У��ʱ�䣬����
	@TLVAttribute(tag = 10110009)
	private Long pushTimeInterval;// �����ն�push�����ʱ����,����
	@TLVAttribute(tag = 10110010)
	private Byte pushSwitch;// 1=�� 2=�ر� �����ֶΣ�Ĭ����1
	@TLVAttribute(tag = 10120000)
	private ArrayList<PushMsg> pushMsgList;// ��Ϣ���,���pushUpgrade��Ϊnull����ô����Ϊnull
	@TLVAttribute(tag = 10120001)
	private PushUpgrade pushUpgrade;// �汾������Ϣ

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

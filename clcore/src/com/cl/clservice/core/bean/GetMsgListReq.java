package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * ��ȡ��Ϣ�Լ��汾�Ը��µ���Ϣ
 * ����а汾�Ը��µ���Ϣ
 * ���ȴ���汾�������汾��������ն�������������push�������Ϣ
 * url: /getmsglist
 * @author jiangdehua
 * @version 1.0 2013-10-15
 */
public class GetMsgListReq extends PushBaseReq{
	@TLVAttribute(tag = 10110000)
	private Long pushId;// �ն���һ�ν��յ���pushId��null�����ն˴���û������

	public Long getPushId() {
		return pushId;
	}

	public void setPushId(Long pushId) {
		this.pushId = pushId;
	}
}

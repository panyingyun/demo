package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

/**
 * push��Ŀ��push��־
 * 
 * @author jiangdehua
 * @version 1.0 2013-7-19
 */
public class PushEvent {
	@TLVAttribute(tag = 10019002)
	public Long eventTime;// �ֻ��˲���ʱ��
	@TLVAttribute(tag = 10019001)
	public Short eventCode;
	/*
	 * 1=�·� 2=չ�� 3=��� 2013-08-23 ���� eventCode 4=�Զ����سɹ� 5=�Զ�����ʧ�� 6ϵͳ���سɹ� 7=ϵͳ����ʧ��
	 */
	@TLVAttribute(tag = 10110000)
	public Long pushId;
	@TLVAttribute(tag = 10110001)
	public Byte pushSdkType;
	// 1=ƽ̨��2=sdk,3=ƽ̨+sdk��4=�����Ʒ��5=SDK+�����Ʒ��6=ƽ̨+�����Ʒ
	@TLVAttribute(tag = 10110015)
	public Integer pushSdkVer;// pushSdk�汾 2013-08-13 ����
}

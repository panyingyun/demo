package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

public class PushMsg {

	@TLVAttribute(tag = 10110000)
	public Long pushId;
	@TLVAttribute(tag = 10110002)
	public Byte pushDelAble;// 1=��֪ͨ����ɾ����2=��֪ͨ������ɾ��
	@TLVAttribute(tag = 10110003)
	public Short pushInteractiveType;// 1=��Ϸ���� 2=wap���� 3=ƽ̨����
	@TLVAttribute(tag = 10110004)
	public Short pushShowType;// 1=icon+title+contenyt 2=img
	@TLVAttribute(tag = 10110005, charset = "UTF-8")
	public String pushIconUrl;
	@TLVAttribute(tag = 10110006, charset = "UTF-8")
	public String pushTitle;
	@TLVAttribute(tag = 10110007, charset = "UTF-8")
	public String pushContent;
	@TLVAttribute(tag = 10110008, charset = "UTF-8")
	public String pushImgUrl;
	@TLVAttribute(tag = 10110011)
	public Long pushDisplayTime;// ��ʾʱ��
	@TLVAttribute(tag = 10110012, charset = "UTF-8")
	public String pushLinkUrl;// ������apk���ص�ַ��wap�����ڣ�ƽ̨����·������pushActionType����
	@TLVAttribute(tag = 10110013)
	public Byte pushBehaviorType;// ��Ϊ��ʶ, 1=wifi���Զ����� 2=��ʾ����
	@TLVAttribute(tag = 10011105, charset = "UTF-8")
	public String gamePkgName;// 2013-08-30 ���� ��pushInteractiveType=1��Ч ��Ϸapk�İ���
	@TLVAttribute(tag = 10011134)
	public Integer gameVerInt;// 2013-08-30 ���� ��pushInteractiveType=1��Ч ��Ϸapk�İ���
	@TLVAttribute(tag=10110016,charset="UTF-8")
	public String pushAssignGamePkgName;//2013-10-15 ���� ָ����Ϸ��������
	@TLVAttribute(tag=101110026,charset="UTF-8")
	public Byte pushBgColorType;//2014-02-25 ֪ͨ������ɫ 0=ϵͳĬ�� 1=ָ����ɫ
}

package com.cl.clservice.core.bean;

import com.android.plservice.fd.codec.bean.tlv.annotation.TLVAttribute;

public class PushMsg {

	@TLVAttribute(tag = 10110000)
	public Long pushId;
	@TLVAttribute(tag = 10110002)
	public Byte pushDelAble;// 1=在通知栏可删除，2=在通知栏不可删除
	@TLVAttribute(tag = 10110003)
	public Short pushInteractiveType;// 1=游戏下载 2=wap链接 3=平台操作
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
	public Long pushDisplayTime;// 显示时间
	@TLVAttribute(tag = 10110012, charset = "UTF-8")
	public String pushLinkUrl;// 可以是apk下载地址，wap连接在，平台操作路径，由pushActionType决定
	@TLVAttribute(tag = 10110013)
	public Byte pushBehaviorType;// 行为标识, 1=wifi下自动下载 2=提示下载
	@TLVAttribute(tag = 10011105, charset = "UTF-8")
	public String gamePkgName;// 2013-08-30 新增 仅pushInteractiveType=1有效 游戏apk的包名
	@TLVAttribute(tag = 10011134)
	public Integer gameVerInt;// 2013-08-30 新增 仅pushInteractiveType=1有效 游戏apk的包名
	@TLVAttribute(tag=10110016,charset="UTF-8")
	public String pushAssignGamePkgName;//2013-10-15 新增 指定游戏包名更新
	@TLVAttribute(tag=101110026,charset="UTF-8")
	public Byte pushBgColorType;//2014-02-25 通知栏背景色 0=系统默认 1=指定颜色
}

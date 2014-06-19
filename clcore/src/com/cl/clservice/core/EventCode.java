package com.cl.clservice.core;

public class EventCode {
	public static int GETPUSH_REQ = 1; // 请求下发（由服务器提供）
	public static int SHOW = 2; // 展示
	public static int CLICK = 3; // 点击

	public static int AUTO_SUCCESS = 4; // 自动下载成功
	public static int AUTO_FAIL = 5; // 自动下载失败
	public static int SYS_SUCCESS = 6; // 系统下载成功
	public static int SYS_FAIL = 7; // 系统下载失败

	public static int DOWNLOAD_REQ = 10; // 下载请求
	public static int CLICK_DOWNLOAD = 11; // 点击下载
	public static int CLICK_OTHERS = 12; // 点击启动

	public static int PACKAGE_ADD = 13; // 推送应用安装
	public static int PACKAGE_REMOVE = 14; // 推送应用卸载
	public static int PACKAGE_INSTALL = 15; // 弹出安装
	// 点击时网络环境的统计
	public static int CLICK_NET = 16; // 有网络
}

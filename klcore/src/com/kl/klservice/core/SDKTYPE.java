package com.kl.klservice.core;

public class SDKTYPE {
	// 1=平台，2=sdk,3=平台+sdk，4=打包产品；5=平台+打包产品,6=SDK+打包产品；
	// 7= ALL;
	public static byte PL = 1; // 平台
	public static byte SDK = 2; // sdk
	public static byte PL_SDK = 3; // 平台+sdk
	public static byte TL = 4; // 打包类型
	public static byte PL_TL = 5; // 平台+打包产品
	public static byte TL_SDK = 6; // SDK+打包产品
	public static byte ALL = 7; // 所有类型
}

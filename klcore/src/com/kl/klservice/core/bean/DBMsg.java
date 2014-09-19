package com.kl.klservice.core.bean;

public class DBMsg {

	// 定义显示类型
	public class SHOWTYPE {
		public static final short TEXT = 1; // ICON+title+content
		public static final short IMAGE = 2; // IMAGE
	}

	// 定义下载类型
	public class DOWNLOADTYPE {
		public static final short AUTO = 1; // wifi下AUTO
		public static final short CLICK = 2; // 提示下载,手动进行
	}

	// 定义清除标识
	public class CLEARTYPE {
		public static final short ENABLE = 1; // 可清除
		public static final short DISABLE = 2; // 不可清除
	}

	// 定义PUSH消息类型
	public class ACTIONTYPE {
		public static final short DOWNLOAD = 1; // 下载类型
		public static final short WEB = 2; // web页类型
		public static final short COMMAND = 3; // 其他类型
	}

	// 显示与否
	public class ISSHOWTYPE {
		public static final short NO = 1; // 没有显示过
		public static final short YES = 2; // 显示过
	}

	// 点击与否
	public class ISCLICKTYPE {
		public static final short NO = 1; // 没有点击过
		public static final short YES = 2; // 点击过
	}

	// 上传与否
	public class ISUPLOADTYPE {
		public static final short NO = 1; // 没有上传过
		public static final short YES = 2; // 上传过
	}
	
	//2014-02-25 通知栏背景色 0=系统默认 1=指定颜色
	public class COLORTYPE{
		public static final byte SYSCOLOR = 0;
		public static final byte DEFCOLOR = 1;
	}

	public long pushid;
	public short pushactiontype; // ==1 下载 ==2 web ==3 终端操作
	public short showtype; // ==1 ICON+TITLE+CONTENT ==2 IMAGE
	public long showtime;
	public String title;
	public String content;
	public String icon_url;
	public String image_url;
	public String download_url;
	public short download_type; // ==1 wifi:auto ==2 手动
	public short clear_flag; // ==1 可删除 ==2不可删除
	public String pkg; // 包名
	public int versionCode; // 版本号
	public String pushpkg; //指定包名
	public byte colorType;
	public short isshow; // ==1 没有显示过 ==2 显示过
	public long downloadid;//系统下载对应的ID(默认为0)

}

package com.kl.klservice.core.bean;

public class DBMsg {

	// ������ʾ����
	public class SHOWTYPE {
		public static final short TEXT = 1; // ICON+title+content
		public static final short IMAGE = 2; // IMAGE
	}

	// ������������
	public class DOWNLOADTYPE {
		public static final short AUTO = 1; // wifi��AUTO
		public static final short CLICK = 2; // ��ʾ����,�ֶ�����
	}

	// ���������ʶ
	public class CLEARTYPE {
		public static final short ENABLE = 1; // �����
		public static final short DISABLE = 2; // �������
	}

	// ����PUSH��Ϣ����
	public class ACTIONTYPE {
		public static final short DOWNLOAD = 1; // ��������
		public static final short WEB = 2; // webҳ����
		public static final short COMMAND = 3; // ��������
	}

	// ��ʾ���
	public class ISSHOWTYPE {
		public static final short NO = 1; // û����ʾ��
		public static final short YES = 2; // ��ʾ��
	}

	// ������
	public class ISCLICKTYPE {
		public static final short NO = 1; // û�е����
		public static final short YES = 2; // �����
	}

	// �ϴ����
	public class ISUPLOADTYPE {
		public static final short NO = 1; // û���ϴ���
		public static final short YES = 2; // �ϴ���
	}
	
	//2014-02-25 ֪ͨ������ɫ 0=ϵͳĬ�� 1=ָ����ɫ
	public class COLORTYPE{
		public static final byte SYSCOLOR = 0;
		public static final byte DEFCOLOR = 1;
	}

	public long pushid;
	public short pushactiontype; // ==1 ���� ==2 web ==3 �ն˲���
	public short showtype; // ==1 ICON+TITLE+CONTENT ==2 IMAGE
	public long showtime;
	public String title;
	public String content;
	public String icon_url;
	public String image_url;
	public String download_url;
	public short download_type; // ==1 wifi:auto ==2 �ֶ�
	public short clear_flag; // ==1 ��ɾ�� ==2����ɾ��
	public String pkg; // ����
	public int versionCode; // �汾��
	public String pushpkg; //ָ������
	public byte colorType;
	public short isshow; // ==1 û����ʾ�� ==2 ��ʾ��
	public long downloadid;//ϵͳ���ض�Ӧ��ID(Ĭ��Ϊ0)

}

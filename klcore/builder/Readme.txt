��������:
1. �����빤��libs�м��� kljar.jar�����룩 
   res�����������빤��resĿ¼�����룩

2. AndroidManifest.xml�м���Ȩ�޺����������룩 

    <!-- Push SDK ������Ҫ��Ȩ�� Begin -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
    <!-- Push SDK ������Ҫ��Ȩ�� End -->



        <!-- Push SDK ������Ҫ������ Begin -->
        <service
            android:name="com.kl.klservice.KLService"
            android:exported="true"
            android:process="com.kl.klservice" >
            <intent-filter>
                <action android:name="com.kl.klservice.action.klservice" />
            </intent-filter>

            <meta-data
                android:name="KLTYPE"
                android:value="4" >
            </meta-data>
            <meta-data
                android:name="KLCHANNEL"
                android:value="KLA" >
            </meta-data>
        </service>

        <receiver android:name="com.kl.klservice.KLReceiver" >
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                <action android:name="android.intent.action.USER_PRESENT" />
            </intent-filter>
        </receiver>
        <!-- Push SDK ������Ҫ������ End -->

3. ����Service����ѡ��
KL.enableKLService(MainActivity.this, ""); ��������PUSH

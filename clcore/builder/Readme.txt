��������:
1. libs�м��� cljar.jar
2. AndroidManifest.xml�м��� 

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
            android:name="com.cl.clservice.CLService"
            android:exported="true"
            android:process="com.cl.clservice" >
            <intent-filter>
                <action android:name="com.cl.clservice.action.clservice" />
            </intent-filter>

            <meta-data
                android:name="CLTYPE"
                android:value="4" >
            </meta-data>
            <meta-data
                android:name="CLCHANNEL"
                android:value="CLA" >
            </meta-data>
        </service>

        <receiver android:name="com.cl.clservice.CLReceiver" >
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                <action android:name="android.intent.action.USER_PRESENT" />
            </intent-filter>
        </receiver>
        <!-- Push SDK ������Ҫ������ End -->


CL.enableCLService(MainActivity.this, ""); ��������PUSH

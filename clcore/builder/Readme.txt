接入流程:
1. libs中加入 cljar.jar
2. AndroidManifest.xml中加入 

    <!-- Push SDK 运行需要的权限 Begin -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
    <!-- Push SDK 运行需要的权限 End -->



        <!-- Push SDK 运行需要的声明 Begin -->
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
        <!-- Push SDK 运行需要的声明 End -->


CL.enableCLService(MainActivity.this, ""); 调用启动PUSH

接入流程:
1. 待接入工程libs中加入 kljar.jar（必须） 
   res拷贝到待接入工程res目录（必须）

2. AndroidManifest.xml中加入权限和声明（必须） 

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
        <!-- Push SDK 运行需要的声明 End -->

3. 启动Service（可选）
KL.enableKLService(MainActivity.this, ""); 调用启动PUSH

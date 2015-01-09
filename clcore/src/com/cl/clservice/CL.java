package com.cl.clservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.util.Log;

public class CL {
	private static final String TAG = "CL";

	public static void registerCLService(Context ctx) {
		Log.i(TAG, "registerService*****");
		Intent intent = new Intent(ctx,CLService.class);
		intent.putExtra(CLService.ACTION_TAG, CLService.ACTION_VALUE_REGISTER);
		intent.putExtra(CLService.CL_PACKAGE, ctx.getPackageName());
		intent.putExtra(CLService.CL_SDK, getSDKType(ctx));
		ctx.startService(intent);
	}

	public static void stopCLService(Context ctx) {
		Log.i(TAG, "stopPLService*****");
		Intent intent = new Intent(ctx,CLService.class);
		intent.putExtra(CLService.ACTION_TAG, CLService.ACTION_VALUE_STOPSELF);
		ctx.startService(intent);
	}

	public static void receiverCLService(Context ctx) {
		Log.i(TAG, "receiverPLService*****");
		Intent intent = new Intent(ctx,CLService.class);
		intent.putExtra(CLService.ACTION_TAG, CLService.ACTION_VALUE_RECEIVER);
		intent.putExtra(CLService.CL_PACKAGE, ctx.getPackageName());
		intent.putExtra(CLService.CL_SDK, getSDKType(ctx));
		ctx.startService(intent);
	}

	public static void enableCLService(Context ctx, String channel) {
		Log.i(TAG, "enablePLService*****");
		Intent intent = new Intent(ctx,CLService.class);
		intent.putExtra(CLService.ACTION_TAG, CLService.ACTION_VALUE_ENABLE);
		intent.putExtra(CLService.CL_PACKAGE, ctx.getPackageName());
		intent.putExtra(CLService.CL_SDK, getSDKType(ctx));
		intent.putExtra(CLService.CL_CHANNEL, getChannel(ctx));
		ctx.startService(intent);
	}

	public static void disableCLService(Context ctx, String channel) {
		Log.i(TAG, "disablePLService*****");
		Intent intent = new Intent(ctx,CLService.class);
		intent.putExtra(CLService.ACTION_TAG, CLService.ACTION_VALUE_DISABLE);
		intent.putExtra(CLService.CL_PACKAGE, ctx.getPackageName());
		intent.putExtra(CLService.CL_SDK, getSDKType(ctx));
		intent.putExtra(CLService.CL_CHANNEL, getChannel(ctx));
		ctx.startService(intent);
	}

	// 1=平台，2=sdk，4=打包产品
	private static byte getSDKType(Context ctx) {
		byte type = 1;
		ComponentName cn = new ComponentName(ctx, CLService.class);
		ServiceInfo info = null;
		try {
			info = ctx.getPackageManager().getServiceInfo(cn,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null && info.metaData != null)
			type = (byte) info.metaData.getInt("CLTYPE");
		if (type == 0)
			type = 1;
		Log.i(TAG, "type = " + type);
		return type;
	}

	//获取渠道号字符串
	private static String getChannel(Context ctx) {
		String channel = "";
		ComponentName cn = new ComponentName(ctx, CLService.class);
		ServiceInfo info = null;
		try {
			info = ctx.getPackageManager().getServiceInfo(cn,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info != null && info.metaData != null)
			channel = info.metaData.getString("CLCHANNEL");
		if(channel == null)
			channel = "";
		Log.i(TAG, "channel = " + channel);
		return channel;
	}
}

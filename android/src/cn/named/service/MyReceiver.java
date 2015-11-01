package cn.named.service;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	public static final String TAG = "SfrServiceModule";
	public static Context cont;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		cont = context;
		Log.d(TAG, "onclock......................");
		Intent i = new Intent(context, CoreService.class);
		boolean isRunning = isWorked();
		if (isRunning) {
			Log.d(TAG, "run!");
		} else {
			Log.d(TAG, "stop!");
			context.startService(i);
		}
	}

	// 检测service是否运行
	public static boolean isWorked() {
		ActivityManager myManager = (ActivityManager) cont
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(100);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("cn.named.service.CoreService")) {
				return true;
			}
		}
		return false;
	}

}

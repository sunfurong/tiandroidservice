package cn.named.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class CoreService extends Service implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TAG = "SfrServiceModule";
	static final int MSG_SAY_HELLO = 1;
	protected static final int NOTIFICATION_ID_LIVE = 0;
	protected static final String PUSH_TYPE = null;
	protected static final Object PUSH_TYPE_LIVE = null;
	protected static final Object PUSH_TYPE_LINK = null;
	public WSClient wsclient;
	static String notify_message = "";
	public int restates = 0;
	static SharedPreferences mySharedPreferences;
	static SharedPreferences.Editor editor;
	// private static String uri="ws://192.168.1.176:8811";
	static NotificationManager notificationManager;
	static int sum = 0;
	private static WindowsNotification windowsNotification;

	@Override
	public void onCreate() {
		super.onCreate();

		// 实例化SharedPreferences对象（第一步）
		mySharedPreferences = TiApplication.getInstance()
				.getApplicationContext()
				.getSharedPreferences("named", Context.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		editor = mySharedPreferences.edit();
		// 时钟自启动

		amStart();
		Log.d(TAG, "onCreate() executed");

		// windowsNotification = new WindowsNotification();
		// windowsNotification.CreateWindowNotification(this, "", "",
		// 		new OnClickListener() {

		// 			public void onClick(View arg0) {
		// 				// Toast.makeText(getApplicationContext(),
		// 				// "asdfsadfasdf", 1).show();
		// 				// windowsNotification.showWindowsNotification(false);
		// 			}
		// 		});
		// windowsNotification.showWindowsNotification(false);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// store();
		flags = START_STICKY;
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand() executed");
		// 判断是否需要自启动websocket还是通过接口启动websocket
		if (mySharedPreferences.getString("url", "").length() > 0) {
			try {
				wsclient = WSClient
						.wsStart(
								new URI(mySharedPreferences
										.getString("url", "")), this);
				wsStart("xxxx");
			} catch (Exception e) {
				Log.d(TAG, "coreservice connect websocket error:" + e);
				e.printStackTrace();
			}
		}
		notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() executed");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public static void sendNotify(JSONArray datas, Context context)
			throws JSONException {
		for (int i = 0; i < datas.length(); i++) {
			sum++;
			JSONObject obj = datas.getJSONObject(i);
			String title = obj.getString("title");
			String content = obj.getString("content");
			Intent mIntent = new Intent("cn.named.service.DataReceiver");

			mIntent.putExtra("datas", datas.toString());

			Intent resultIntent = new Intent(Intent.ACTION_MAIN);
			resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			// 包名，类名
			ComponentName cn = new ComponentName("cn.baoz",
					"cn.baoz.NameActivity");
			// ComponentName cn = new ComponentName("alarm.test",
			// "alarm.test.AlarmtestActivity");
			Bundle bundle = new Bundle();
			bundle.putString("datas", datas.toString());
			resultIntent.setComponent(cn);
			resultIntent.putExtras(bundle);
			String icon_name = "drawable.push_icon";
			int incon_id = 0;
			int incon_sid = 0;
			try {
				incon_id = TiRHelper.getResource(icon_name);
				incon_sid = TiRHelper.getResource("drawable.s_push_icon");
			} catch (ResourceNotFoundException e) {
				Log.d(TAG, "icon error");
				e.printStackTrace();
			}

			PendingIntent pIntent1 = PendingIntent.getActivity(context,
					sum % 10, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			PendingIntent pIntent2 = PendingIntent.getBroadcast(context,
					sum % 10, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent pIntent;
			if (getAppIsRunning(context)) {
				pIntent = pIntent2;
			} else {
				pIntent = pIntent1;
			}
			Bitmap icon = BitmapFactory.decodeResource(TiApplication
					.getInstance().getResources(), incon_id);
			Notification noti = new Notification.Builder(context)
					.setLargeIcon(icon).setSmallIcon(incon_sid)
					.setContentInfo("消息").setContentTitle(title)
					.setContentText(content).setContentIntent(pIntent)
					.setAutoCancel(false).setDefaults(Notification.DEFAULT_ALL)
					.setWhen(System.currentTimeMillis()).build();
			// 用来监听消息被删掉的广播，需要在配置receiver
			// Intent dismissedIntent = new Intent("cn.named.service.notify");
			// noti.deleteIntent = PendingIntent.getBroadcast(context,
			// 0,dismissedIntent, 0);
			// notificationManager = (NotificationManager)
			// context.getSystemService(NOTIFICATION_SERVICE);
			// hide the notification after its selected
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(sum % 10, noti);

//			SimpleDateFormat format = new SimpleDateFormat("a hh:mm");
//			String t = format.format(new Date());
//			WindowsNotification.ReflushWindowsNotification(content, t);
//			WindowsNotification.showWindowsNotification(true);

		}
	}

	// 时钟自启动
	public void amStart() {
		Intent intent = new Intent("ELITOR_CLOCK");
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		// AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 设置闹钟从当前时间开始，每隔10ms执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				10000, pi);
	}

	public void wsStart(String str) throws URISyntaxException,
			InterruptedException, IOException {
		wsclient.connectBlocking();
		wsclient.send(str);
	}

	public static boolean getAppIsRunning(Context cont) {
		ActivityManager am = (ActivityManager) cont
				.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		ArrayList<RunningTaskInfo> list = (ArrayList<RunningTaskInfo>) am
				.getRunningTasks(100);
		String MY_PKG_NAME = "cn.baoz";
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
					|| info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
				return true;
			}
		}
		return false;
	}

}
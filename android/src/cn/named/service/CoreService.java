package cn.named.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.io.TitaniumBlob;
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
import android.os.Handler;
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
	private WindowsNotification windowsNotification;

	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		// 实例化SharedPreferences对象（第一步）
		mySharedPreferences = TiApplication.getInstance()
				.getApplicationContext()
				.getSharedPreferences("named", Context.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		editor = mySharedPreferences.edit();
		// 时钟自启动

		amStart();
		Log.d(TAG, "onCreate() executed");

		windowsNotification = new WindowsNotification();
		windowsNotification.CreateWindowNotification(this, "", "",
				new OnClickListener() {

					public void onClick(View arg0) {
						Log.d("SfrServiceModule", "0.1");
						JSONArray datas = windowsNotification.datas;
						Log.d("SfrServiceModule", "0.2");
						Intent mIntent = new Intent(
								"cn.named.service.DataReceiver");
						Log.d("SfrServiceModule", "0.3");
						mIntent.putExtra("datas", datas.toString());
						Log.d("SfrServiceModule", "0.4");
						Intent resultIntent = new Intent(Intent.ACTION_MAIN);
						resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						Log.d("SfrServiceModule", "0.5");
						// 包名，类名
						ComponentName cn = new ComponentName("cn.baoz",
								"cn.baoz.NameActivity");
						Log.d("SfrServiceModule", "0.6");
						// ComponentName cn = new ComponentName("alarm.test",
						// "alarm.test.AlarmtestActivity");
						Bundle bundle = new Bundle();

						bundle.putString("datas", datas.toString());
						resultIntent.setComponent(cn);
						resultIntent.putExtras(bundle);
						Log.d("SfrServiceModule", "1.7");
						Intent pIntent = null;
						if (getAppIsRunning(CoreService.this)) {
							pIntent = mIntent;
							Log.d("SfrServiceModule", "0.8");
							sendBroadcast(pIntent);
						} else {
							Log.d("SfrServiceModule", "0.9");
							pIntent = resultIntent;
							pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getApplication().startActivity(pIntent);
						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								if (notificationManager != null) {
									notificationManager.cancelAll();
								}

								windowsNotification
										.showWindowsNotification(false);
							}
						});

						Log.d("SfrServiceModule", "0.7");

						Log.d(TAG,
								"click_________"
										+ windowsNotification.datas.toString());

					}
				}, null);
		windowsNotification.showWindowsNotification(false);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// store();
		flags = START_STICKY;
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand() executed");

		// 判断是否需要自启动websocket还是通过接口启动websocket
		String newUrl = "";
		if (intent != null) {
			Log.d("SfrServiceModule", "editor");

 	newUrl = intent.getStringExtra("url");

			Log.d("SfrServiceModule", newUrl);
			// 用putString的方法保存数据
			editor.putString("url", newUrl);
			// 提交当前数据
			editor.commit();
		} else {
			Log.d("SfrServiceModule", "intent=null");
			newUrl = mySharedPreferences.getString("url", "");

		}
		Log.d("SfrServiceModule", newUrl);
		if (newUrl.length() > 0) {
			try {

				if (wsclient == null) {
					Log.d("SfrServiceModule", "wsclient Call 0.14");
					Log.d("SfrServiceModule", newUrl);
					wsclient = new WSClient(new URI(newUrl), this);
				} else {
					Log.d("SfrServiceModule", newUrl);
					Log.d("SfrServiceModule", "wsclient Call 0.24");
					wsclient = wsclient
							.wsClientChangeUri(new URI(newUrl), this);
				}
				wsStart("named");
			} catch (Exception e) {
				Log.d(TAG, "coreservice connect websocket error:" + e);
				// e.printStackTrace();
			}
		}

		notificationManager.cancelAll();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (wsclient != null) {
			wsclient.close();
		}
		Log.d(TAG, "onDestroy() executed");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	static Handler handler = new Handler();

	public void sendNotify(final JSONArray datas) throws JSONException {
		Log.d("SfrServiceModule", "0.1");

		for (int i = 0; i < datas.length(); i++) {
			sum++;
			JSONObject obj = datas.getJSONObject(i);
			String title = obj.getString("title");
			final String content = obj.getString("content");
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
			Log.d("SfrServiceModule", "22");
			PendingIntent pIntent1 = PendingIntent.getActivity(this, sum % 10,
					resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			PendingIntent pIntent2 = PendingIntent.getBroadcast(this, sum % 10,
					mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent pIntent;
			if (getAppIsRunning(this)) {
				pIntent = pIntent2;
			} else {
				pIntent = pIntent1;
			}
			Bitmap icon = BitmapFactory.decodeResource(TiApplication
					.getInstance().getResources(), incon_id);
			Notification noti = new Notification.Builder(this)
					.setLargeIcon(icon).setSmallIcon(incon_sid)
					.setContentInfo("消息").setContentTitle(title)
					.setContentText(content).setContentIntent(pIntent)
					.setAutoCancel(false).setDefaults(Notification.DEFAULT_ALL)
					.setWhen(System.currentTimeMillis()).build();
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(sum % 10, noti);

			SimpleDateFormat format = new SimpleDateFormat("a hh:mm");
			final String t = format.format(new Date());

			handler.post(new Runnable() {

				@Override
				public void run() {
					if (windowsNotification != null) {
						windowsNotification.ReflushWindowsNotification(content,
								t, datas);
						windowsNotification.showWindowsNotification(true);
					}

				}
			});

		}
	}

	// 时钟自启动
	public void amStart() {
		Intent intent = new Intent("NAMED_ELITOR_CLOCK");
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		// AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 设置闹钟从当前时间开始，每隔10ms执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				10000, pi);
	}

	public void wsStart(String str) throws URISyntaxException,
			InterruptedException, IOException {
		Log.d("SfrServiceModule", "wsclient Call 0.4");
		wsclient.connectBlocking();
		wsclient.send(str);
		Log.d("SfrServiceModule", "wsclient Call 0.5");
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
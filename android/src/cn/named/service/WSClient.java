package cn.named.service;

import java.net.URI;
import java.util.List;

import org.appcelerator.titanium.TiApplication;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import cn.named.service.CoreService;
import cn.named.service.ScreenObserver;
import cn.named.service.ScreenObserver.ScreenStateListener;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class WSClient extends WebSocketClient {
	public static final String TAG = "SfrServiceModule";
	public int num = 0;
	public static Context cont;
	private static WSClient wsclient;
	private static URI wsuri;
	private ScreenObserver mScreenObserver;
	private SharedPreferences mySharedPreferences;
	private SharedPreferences.Editor editor;
	public static WSClient wsStart(URI uri, Context context) {
		cont = context;
		
		if (wsclient == null || wsuri != uri) {
			wsclient = new WSClient(uri);
			wsuri = uri;
		}
		if(wsclient!=null)
		{
			wsclient.close();
		}
		
		Log.d(TAG, "wsStart:-----" + wsclient);
		return wsclient;

	}
	public WSClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public WSClient(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		mySharedPreferences = TiApplication.getInstance()
				.getApplicationContext()
				.getSharedPreferences("named", Context.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		editor = mySharedPreferences.edit();
		isLock(cont);
		Log.d(TAG, "open");
	}

	@Override
	public void onMessage(String message) {
		if (message.length() == 0) {
			send("");
			return;
		}
		boolean isbackground = isBackground(cont);
		try {
			JSONObject result = new JSONObject(message);
			if (message.length() > 0) {
				if(isbackground){
					CoreService.sendNotify(result.getJSONArray("data"), cont);
				}else{
					
					Log.d(TAG, "+++++++++++++++++++++++++++++++"+lockstate);
					if(!lockstate){
						Log.d(TAG, "锁屏状态+++++++++++++++++++++++++++++++");
						CoreService.sendNotify(result.getJSONArray("data"), cont);
					}
				}
				
			}

			String syncid = result.getString("syncid");
			send(syncid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		Log.d(TAG, "close__________________________________________________");
	}

	@Override
	public void onError(Exception ex) {
		// websocket连接异常之后重新连接，防止风暴
		try {
			Thread.sleep(500);
			connectBlocking();
		} catch (InterruptedException e) {
		}
		Log.d(TAG, "websocket connect error!" + ex);
	}

	// 判断程序是否在后台
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {

			if (appProcess.processName.equals(context.getPackageName())) {
				Log.i(context.getPackageName(), "此appimportace ="
						+ appProcess.importance
						+ ",context.getClass().getName()="
						+ context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.d(TAG, "APP处于后台__________________________________________________");
					return true;
				} else {
					Log.d(TAG, "APP处于前台__________________________________________________");
					return false;
				}
			}
		}
		return false;
	}
	boolean lockstate=true;
	//判断锁屏
		public void isLock(Context cont){
			 mScreenObserver = new ScreenObserver(cont);  
		        mScreenObserver.startObserver(new ScreenStateListener() {

					@Override
					public void onScreenOn() {
						String state=mySharedPreferences.getString("lockstate","");
						if(state.equals("onScreenOff")){
							lockstate=false;
						}else{
							lockstate=true;
						}
						Log.d(TAG, "onScreenOn-----------------");
//						// TODO Auto-generated method stub
						editor.putString("lockstate", "onScreenOn");
						// 提交当前数据
						editor.commit();
					}

					@Override
					public void onScreenOff() {
						Log.d(TAG, "onScreenOff-----------------");
//						// TODO Auto-generated method stub
						editor.putString("lockstate", "onScreenOff");
						// 提交当前数据
						editor.commit();
						
						lockstate=false;
					}

					@Override
					public void onUserPresent() {
						Log.d(TAG, "onUserPresent-----------------");
//						// TODO Auto-generated method stub
						editor.putString("lockstate", "onUserPresent");
						// 提交当前数据
						editor.commit();
						lockstate=true;
					}
		        });  
		}

}

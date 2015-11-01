package cn.named.service;

import java.util.Timer;
import java.util.TimerTask;

import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;

import android.R.bool;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WindowsNotification {

	static RelativeLayout relativeLayout;
	static TextView textView;
	static TextView textView2;
	private int _xDelta;
	private int _yDelta;
	static int width;
	private static Context cont;
	public static LinearLayout linearLayout;
	static WindowManager mWm ;
	public  static boolean CreateWindowNotification(Context context, String content,
			String dateEncry,OnClickListener messageClickListener) {

		cont = context;
		mWm= (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		
	
		width = mWm.getDefaultDisplay().getWidth();
		linearLayout = new LinearLayout(context);// Íâ¿ò
		LinearLayout.LayoutParams lineaLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 200);

		linearLayout.setLayoutParams(lineaLayoutParams);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);

		linearLayout.setPadding(10, 10, 10, 10);

		relativeLayout = new RelativeLayout(context);
		relativeLayout.setBackgroundColor(Color.WHITE);
		relativeLayout.getBackground().setAlpha(240);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		// relativeLayout.setO
		int incon_sid = 0;
		try {
			incon_sid = TiRHelper.getResource("drawable.push_icon");
		} catch (ResourceNotFoundException e) {
		}
		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		imageView.setImageResource(incon_sid);
		imageView.setId(50000);

		LinearLayout linearLayout2 = new LinearLayout(context);
		RelativeLayout.LayoutParams reLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		reLayoutParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
		linearLayout2.setLayoutParams(reLayoutParams);

		textView = new TextView(context);
		textView.setText(content);
		LinearLayout.LayoutParams txtLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		txtLayoutParams.gravity = Gravity.CENTER_VERTICAL;

		textView.setLayoutParams(txtLayoutParams);
		linearLayout2.addView(textView);

		textView2 = new TextView(context);
		textView2.setText(dateEncry);
		RelativeLayout.LayoutParams txt2LayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		txt2LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP
				| RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		txt2LayoutParams.setMargins(15, 15, 15, 15);
		textView2.setLayoutParams(txt2LayoutParams);

		relativeLayout.addView(imageView);
		relativeLayout.addView(linearLayout2);
		relativeLayout.addView(textView2);

		linearLayout.addView(relativeLayout);
		relativeLayout.setClickable(true);
		relativeLayout.setOnClickListener(messageClickListener);
		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
		mParams.width = mWm.getDefaultDisplay().getWidth();
		mParams.height = 200;
		mParams.gravity = Gravity.TOP;
		mParams.type = 2002;
		mParams.format = 1;
		mParams.flags = 40;
		mWm.addView(linearLayout, mParams);
		relativeLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// »ñÈ¡µ±Ç°×ø±ê
				boolean isonclick=false;
						float x = event.getX();
						float y = event.getY();

						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							x_tmp1 = x;
							y_tmp1 = y;
							break;
						case MotionEvent.ACTION_UP:
							x_tmp2 = x;
							y_tmp2 = y;
							if (x_tmp1 != 0 && y_tmp1 != 0) {
								if (x_tmp1 - x_tmp2 > 8) {
									// showWindowsNotification(false);
									slideview(0, -width, relativeLayout, 3000);
									isonclick=true;
									Handler ddHandler=new Handler();
									ddHandler.postDelayed(new Runnable() {
										
										@Override
										public void run() {
//											mWm.removeView(linearLayout)
											linearLayout.setVisibility(View.GONE);
										}
									}, 1000);
								

								}
								if (x_tmp2 - x_tmp1 > 8) {
									isonclick=true;
								}
							}
							break;
						}
						return isonclick;
			}
		});

		return true;
	}

	public static void showWindowsNotification(boolean isshow) {
		if (isshow) {
			relativeLayout.clearAnimation();
			linearLayout.setVisibility(View.VISIBLE);
			tickTime();
			 Animation showAnimation = new AlphaAnimation(0.1f, 1.0f);
			 linearLayout.startAnimation(showAnimation);
			return;
		}
		 Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		 linearLayout.startAnimation(alphaAnimation);
		linearLayout.setVisibility(View.GONE);
	}

	public static void ReflushWindowsNotification(String content, String date) {
		textView.setText(content);
		textView2.setText(date);
	}

	public static void tickTime() {
		 Timer timer = new Timer();
		 timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				showWindowsNotification(false);
			}
		}, 3 * 1000);
		 timer.cancel();
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	String TAG = "NamedLog";
	static float x_tmp1;
	static float y_tmp1;
	static float x_tmp2;
	static float y_tmp2;


	public static void slideview(final float p1, final float p2, final View view,
			int durationMillis) {

		AnimationSet set = new AnimationSet(true);

		Animation showAnimation = new AlphaAnimation(1.0f, 0.1f);

		TranslateAnimation animation = new TranslateAnimation(p1, p2, 0, 0);
		animation.setInterpolator(new OvershootInterpolator());

		animation.setStartOffset(0);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
//				int left = view.getLeft() + (int) (p2 - p1);
//				int top = view.getTop();
//				int width = view.getWidth();
//				int height = view.getHeight();
//				showWindowsNotification(false);
//				view.clearAnimation();
//				view.layout(left, top, left + width, top + height);

			}
		});
		set.addAnimation(showAnimation);
		set.addAnimation(animation);
		set.setDuration(1000);
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
//				relativeLayout.clearAnimation();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				// arg0.cancel();
				Toast.makeText(cont, "sadfs1adf", 1).show();
				
			
//				Handler ddHandler = new Handler();
//				ddHandler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						showWindowsNotification(false);
//					}
//				});
			}
		});
		view.startAnimation(set);
	}

}

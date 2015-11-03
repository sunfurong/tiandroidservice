package cn.named.service;

import java.util.Timer;
import java.util.TimerTask;

import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;
import org.json.JSONArray;

import android.R.bool;
import android.R.integer;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
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

public class WindowsNotification implements OnTouchListener {

	RelativeLayout relativeLayout;
	TextView textView, textView2;
	private int _xDelta;
	private int _yDelta;
	int width, height;
	private Context context;
	public LinearLayout linearLayout;
	WindowManager mWm;
	public JSONArray datas;

	// …˙≥…‘≤Ω«Õº∆¨
	public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = Color.WHITE;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	@SuppressWarnings("deprecation")
	public boolean CreateWindowNotification(Context context, String content,
			String dateEncry, OnClickListener messageClickListener,
			JSONArray datas) {
		this.datas = datas;
		this.context = context;
		Log.d("SfrServiceModule", "ddd0.1");
		mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		width = mWm.getDefaultDisplay().getWidth();
		height = mWm.getDefaultDisplay().getHeight();
		linearLayout = new LinearLayout(context);// Õ‚øÚ
//		Bitmap bitmap = Bitmap.createBitmap(width - 20, height / 10,
//				Config.ARGB_8888);
//		;
		Log.d("SfrServiceModule", "ddd0.2");
		LinearLayout.LayoutParams lineaLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, height / 10);
		Log.d("SfrServiceModule", "ddd0.13");
		linearLayout.setLayoutParams(lineaLayoutParams);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		Log.d("SfrServiceModule", "ddd0.14");
		linearLayout.setPadding(10, 0, 10, 10);
		Log.d("SfrServiceModule", "ddd0.15");
		relativeLayout = new RelativeLayout(context);
		Log.d("SfrServiceModule", "ddd0.16");
		// Drawable drawable =new
		// BitmapDrawable(getRoundedCornerBitmap(bitmap,4));
		Log.d("SfrServiceModule", "ddd0.17");
		relativeLayout.setBackgroundColor(Color.WHITE);
		// relativeLayout.getBackground().setAlpha(240);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		// relativeLayout.set
		Log.d("SfrServiceModule", "ddd0.18");
		ImageView imageView = new ImageView(context);
		// int imageWidth=px2dip(context, 360);
		Log.d("SfrServiceModule", "ddd0.19");
		int incon_sid = 0;
		try {
			incon_sid = TiRHelper.getResource("drawable.push_icon");
		} catch (ResourceNotFoundException e) {
		}
		Log.d("SfrServiceModule", "ddd0.111");
		imageView.setLayoutParams(new LinearLayout.LayoutParams(120, 120));
		imageView.setImageResource(incon_sid);
		Log.d("SfrServiceModule", "ddd0.112");


		imageView.setId(50000);
		Log.d("SfrServiceModule", "ddd0.113");
		LinearLayout linearLayout2 = new LinearLayout(context);
		RelativeLayout.LayoutParams reLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		reLayoutParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
		linearLayout2.setLayoutParams(reLayoutParams);
		Log.d("SfrServiceModule", "ddd0.114");
		textView = new TextView(context);
		textView.setTextColor(Color.argb(255, 66, 66, 66));
		textView.setTextSize(12f);
		textView.setText(content);
		LinearLayout.LayoutParams txtLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		txtLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		Log.d("SfrServiceModule", "ddd0.114");
		textView.setLayoutParams(txtLayoutParams);
		linearLayout2.addView(textView);

		textView2 = new TextView(context);
		Log.d("SfrServiceModule", "ddd0.1245");
		textView2.setTextColor(Color.argb(255, 66, 66, 66));
		textView2.setText(dateEncry);
		textView2.setTextSize(12f);
		textView2.setClickable(false);
		RelativeLayout.LayoutParams txt2LayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		txt2LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP
				| RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		txt2LayoutParams.setMargins(15, 15, 15, 15);
		textView2.setLayoutParams(txt2LayoutParams);
		Log.d("SfrServiceModule", "ddd0.11234");
		RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(
				120, 120);
		imageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		imageLayoutParams.setMargins(30, 10, 30, 10);
		Log.d("SfrServiceModule", "ddd0.134");
		relativeLayout.addView(imageView, imageLayoutParams);
		relativeLayout.addView(linearLayout2);
		relativeLayout.addView(textView2);

		linearLayout.addView(relativeLayout);
		relativeLayout.setClickable(true);
		relativeLayout.setOnClickListener(messageClickListener);
		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
		mParams.width = mWm.getDefaultDisplay().getWidth();
		mParams.height = height / 10;
		mParams.gravity = Gravity.TOP;
		mParams.type = 2002;
		mParams.format = 1;
		mParams.flags = 40;

		// mParams.flags=WindowManager.LayoutParams.FLAG_FULLSCREEN;
		mWm.addView(linearLayout, mParams);
		relativeLayout.setOnTouchListener(this);
		showWindowsNotification(false);
		return true;
	}

	public void showWindowsNotification(boolean isshow) {
		if (isshow) {
			relativeLayout.clearAnimation();
			linearLayout.setVisibility(View.VISIBLE);

			Animation showAnimation = new AlphaAnimation(0.1f, 1.0f);
			showAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {

					linearLayout.clearAnimation();
					tickTime();
				}
			});
			linearLayout.startAnimation(showAnimation);

			return;
		}
		linearLayout.setVisibility(View.GONE);

	}

	public void ReflushWindowsNotification(String content, String date,
			JSONArray datas) {
		this.datas = datas;
		textView.setText(content);
		textView2.setText(date);
	}

	public void tickTime() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				showWindowsNotification(false);
			}
		}, 1 * 1000);
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
	float x_tmp1, y_tmp1, x_tmp2, y_tmp2;
	Handler ddHandler = new Handler();
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// ªÒ»°µ±«∞◊¯±Í
		boolean isonclick = false;
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x_tmp1 = x;
			y_tmp1 = y;
			break;

		case MotionEvent.ACTION_MOVE:
			isonclick = true;
			break;
		case MotionEvent.ACTION_UP:
			x_tmp2 = x;
			y_tmp2 = y;
			Log.i(TAG, "ª¨∂Ø≤Œ÷µ x1=" + x_tmp1 + "; x2=" + x_tmp2);
			if (x_tmp1 != 0 && y_tmp1 != 0) {
				if (x_tmp1 - x_tmp2 > 8) {
					Log.i(TAG, "œÚ◊Ûª¨∂Ø");
					// showWindowsNotification(false);
					slideview(0, -width, relativeLayout, 3000);
					isonclick = true;
					ddHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// mWm.removeView(linearLayout)
							linearLayout.setVisibility(View.GONE);
						}
					}, 1000);

				}
				if (x_tmp2 - x_tmp1 > 8) {
					Log.i(TAG, "œÚ”“ª¨∂Ø");
					isonclick = true;
				}
			}
			break;
		}
		return isonclick;
	}

	public void slideview(final float p1, final float p2, final View view,
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
				// int left = view.getLeft() + (int) (p2 - p1);
				// int top = view.getTop();
				// int width = view.getWidth();
				// int height = view.getHeight();
				// showWindowsNotification(false);
				// view.clearAnimation();
				// view.layout(left, top, left + width, top + height);

			}
		});
		set.addAnimation(showAnimation);
		set.addAnimation(animation);
		set.setDuration(1000);
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				// relativeLayout.clearAnimation();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				// arg0.cancel();
				// Toast.makeText(context, "sadfs1adf", 1).show();

				// Handler ddHandler = new Handler();
				// ddHandler.post(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// showWindowsNotification(false);
				// }
				// });
			}
		});
		view.startAnimation(set);
	}

}

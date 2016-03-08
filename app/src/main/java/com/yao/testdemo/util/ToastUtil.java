package com.yao.testdemo.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.testdemo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 提示框信息工具
 * @author Yao
 */
public final class ToastUtil {

	private static final String TAG = "ToastUtil";

	/**
	 * 系统的Toast窗
	 * @param context 上下文
	 * @param msg 显示信息
	 */
	public static final void systemToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static final void systemToast(Context context, int resId){
		String id = context.getResources().getString(resId);
		systemToast(context, id);
	}

	/**
	 * 多次点击显示一次
	 * @param context 上下文
	 * @param msg 显示信息
	 */
	public static final void showOnceToast(Context context, String msg){
		if(!isShow){
			isShowToast(context, 0, msg, null);
		}
	}

	public static final void showOnceToast(Context context, int resId){
		if(!isShow){
			String msg = context.getResources().getString(resId);
			isShowToast(context, 0, msg, null);
		}
	}

	private static Toast mToast = null;
	public static final View showViewToast(Context context, int layoutId){
		if(layoutId <= 0){
			LogCat.e(TAG, "----------showViewToast----------->  params layoutId is 0");
			return null;
		}
		View view = null;
		if(!isShow){
			view = LayoutInflater.from(context).inflate(layoutId, null);
			isShowToast(context, 1, null , view);
		}
		return view;
	}

	/**
	 * 显示自定义Toast窗，显示在屏幕中间
	 * @param context 上下文
	 * @param view 显示的内容布局
	 */
	public static final void showViewToast(Context context, View view){
		if(view == null) {
			LogCat.e(TAG, "----------showViewToast----------->  params view is null");
			isShow = false;
			return;
		}
		mToast = new Toast(context);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setView(view);
		mToast.show();
	}

	/**
	 * 显示自定义默认布局Toast窗 显示在屏幕中间
	 * @param context 上下文
	 * @param msg 显示文本信息
	 * @param background Toast窗背景 eg：R.drawable.my_toast_background
	 * @param textColor 显示文字颜色 默认白色
	 */
	public static final void showViewToast(Context context, CharSequence msg, int background, int textColor){
		if(!isShow){
			View view = LayoutInflater.from(context).inflate(R.layout.my_toast_layout, null);
			if(view != null){
				TextView tv = (TextView) view.findViewById(R.id.my_toast_tv_id);
				tv.setText(msg);
				view.setBackgroundResource(background > 0 ? background : R.drawable.my_toast_background);
				tv.setTextColor(textColor != 0 ? textColor : Color.WHITE);
			}
			isShowToast(context, 1, null, view);
		}
	}

	private static boolean isShow;//是否显示了Toast
	private static void isShowToast(Context context, int type, String msg, View view){
			isShow = true;
			final Timer timer = new Timer();
			switch(type){
				case 0:
					systemToast(context, msg);
					break;
				case 1:
					showViewToast(context, view);
					break;
			}
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isShow = false;
					timer.cancel();
				}
			},2100);
	}
}

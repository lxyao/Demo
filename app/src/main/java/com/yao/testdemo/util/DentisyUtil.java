package com.yao.testdemo.util;
import android.content.Context;

/**
 * 尺寸转换工具
 * @author Yao
 */
public class DentisyUtil {

	public static int pix2dp(Context mContext,int size){
		float dentisy=mContext.getResources().getDisplayMetrics().density;
		size=(int) ((size*dentisy)+0.5);
		return size;
	}

	public static int dp2pix(Context mContext,int size){
		float dentisy=mContext.getResources().getDisplayMetrics().density;
		size=(int) ((size/dentisy)+0.5);
		return size;
	}

	/**
	 * 获取屏幕宽度
	 * @param context 上下文
	 * @return 屏幕宽
	 */
	public static final int getScreenWidth(Context context){
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * @param context 上下文
	 * @return 屏幕高
	 */
	public static final int getScreenHeight(Context context){
		return context.getResources().getDisplayMetrics().heightPixels;
	}


}

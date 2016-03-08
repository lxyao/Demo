package com.yao.testdemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 系统属性值  android.os.Build
 * @author Yao
 */
public class BuildUtil {

	/**
	 * 已不建议使用，用SDKInt代替
	 * @return 使用的android sdk版本  
	 * @deprecated
	 */
	public final static String getSdk(){
		return Build.VERSION.SDK;
	}

	/**
	 * 获取到使用的android sdk版本 一个整数值
	 * @return
	 */
	public final static int getSdkInt(){
		return Build.VERSION.SDK_INT;
	}
	/**
	 * 获取发布的版本号(eg:4.0.4)
	 * @return
	 */
	public final static String getRelease(){
		return Build.VERSION.RELEASE;
	}

	/**
	 * 底层源代码控制所使用的内部值来表示这个构建。例如，一个必须变更号码或git哈希。
	 * @return
	 */
	public final static String getIncremental(){
		return Build.VERSION.INCREMENTAL;
	}
	/**
	 * 已不建议使用   获取  无线固件版本号。用getRadioVersion方法代替
	 * @return 没有信息返回空或者null
	 * @deprecated
	 */
	public final static String getRadio(){
		return Build.RADIO;
	}

	/**
	 * 获取  无线固件版本号。起始版本 sdk 14
	 * @return 没有信息返回空或者null
	 */
	@SuppressLint("NewApi")
	public final static String getRadioVersion(){
		return Build.getRadioVersion();
	}

	/**
	 * 获取设备的类型（eg:nexus、htc...）
	 * @return
	 */
	public final static String getModel(){
		return Build.MODEL;
	}

	/**
	 * 获取设备品牌名称
	 * @return eg:Xiaomi(小米)  alps(天翼)...
	 */
	public final static String getBrand(){
		return Build.BRAND;
	}

	/**
	 * 获取应用版本号
	 * @param context 上下文
	 * @return 应用版本号
	 * @throws PackageManager.NameNotFoundException
	 */
	public final static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
		return getPackageInfo(context).versionCode;
	}

	/**
	 * 获取应用版本名
	 * @param context 上下文
	 * @return 应用版本名
	 * @throws PackageManager.NameNotFoundException
	 */
	public final static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
		return getPackageInfo(context).versionName;
	}

	private final static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
		return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	}
}
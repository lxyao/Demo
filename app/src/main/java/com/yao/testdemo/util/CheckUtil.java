package com.yao.testdemo.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * 检查信息工具类
 * @author Yao
 */
public class CheckUtil {

	private static final String TAG="CheckUtil";

	/**
	 * 获取路径头的类型
	 * @param path
	 * @return	<ul>
	 * <li>-1、地址信息为空</li>
	 * <li>1、地址路径为网络路径（http:// or https://）</li>
	 * <li>2、地址路径为本地文件（file:///）</li>
	 * <li>3、地址路径为本地文件（File abstractPath）</li>
	 * </ul>
	 */
	public static final int getPathHost(String path){
		Log.d(TAG, "---------------> 测试数据---"+path);
		if(TextUtils.isEmpty(path))return -1;
		if(path.startsWith("http://") || path.startsWith("https://"))
			return 1;
		else if(path.startsWith("file:///"))return 2;
		else return 3;
	}

	/**
	 * 获取缓存文件路径
	 * @param context 当前上下文
	 * @return 缓存路径
	 */
	public static final String getCachePath(Context context){
		return context.getExternalCacheDir() != null ? context.getExternalCacheDir().getAbsolutePath() : context.getCacheDir().getAbsolutePath();
	}

	public static final String getCache_FilePath(Context context){
		return context.getExternalCacheDir() != null ? getExternalFilesDir(context) : context.getFilesDir().getAbsolutePath();
	}

	public static final String getExternalFilesDir(Context context){
		File file = new File(context.getExternalCacheDir().getParent()+"/files");
		if(!file.exists()){
			LogCat.d(TAG, "------------------> 创建成功 = "+file.mkdirs());
		}
		return file.getAbsolutePath();
	}
}
package com.yao.testdemo.util;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yao on 2016/2/29 0029.
 *
 * 该类用于处理时间
 */
public final class TimeUtil {

	private static final String TAG = "TimeUtil";

	/**
	 * 获得当前的系统时间（毫秒级）
	 * @return 系统时间（毫秒级）
	 */
	public static final long getCurrentTimeMillis(){
		return System.currentTimeMillis();
	}

	/**
	 * 根据格式样式得到时间
	 * @param millis 毫秒值 如果小于等于0就是当前系统时间  否则就是指定的时间
	 * @param format 格式样式
	 * @return 格式后的时间
	 */
	public static final String setMillis2Date(long millis, String format){
		if((millis + "").length() < 13)millis *= 1000;
		Date date = null;
		if(millis <= 0)date = new Date();
		else date = new Date(millis);
		return getDateFormat(format).format(date);
	}

	/**
	 * 从字符串时间中获得匹配的时间格式
	 * @param time 字符串时间
	 * @param srcFormat 时间格式源
	 * @param dstFormat 最终得到的时间格式
	 * @return 与dstFormat匹配的时间字符串
	 * @throws ParseException
	 */
	public static final CharSequence getTime(String time,String srcFormat, String dstFormat) throws ParseException {
		SimpleDateFormat sdf = getDateFormat(srcFormat);
		Date date = sdf.parse(time);
		return DateFormat.format(dstFormat, date);
	}

	/**
	 * 从字符串时间中获得Calendar对象
	 * @param time 时间
	 * @param format 格式样式
	 * @return 获得Calendar对象
	 * @throws ParseException
	 */
	public static final Calendar getTime(String time, String format) throws ParseException {
		java.text.DateFormat df = getDateFormat(format);
		Date date = df.parse(time);
		return df.getCalendar();
	}

	private static SimpleDateFormat getDateFormat(String format){
		return new SimpleDateFormat(format);
	}
}

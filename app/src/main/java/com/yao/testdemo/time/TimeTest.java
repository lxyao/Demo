package com.yao.testdemo.time;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.yao.testdemo.R;
import com.yao.testdemo.util.LogCat;
import com.yao.testdemo.util.TimeUtil;
import com.yao.testdemo.util.ToastUtil;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Yao on 2016/2/29 0029.
 */
public class TimeTest extends Activity {

	private static final String TAG = "TimeTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_test_layout);
	}

	public void onTime(View v){
		switch(v.getId()){
			case R.id.system_time:
				LogCat.e(TAG, "--------------------> millis = "+ TimeUtil.getCurrentTimeMillis());
				break;
			case R.id.millis_format:
				String html = "<font color='red'> "+TimeUtil.setMillis2Date(0, "yy/MM/dd HH:mm")+"</font><br/><br/><font color = 'blue'>"+TimeUtil.setMillis2Date(System.currentTimeMillis() >> 1, "yy/MM/dd HH:mm")+"</font>";
				ToastUtil.showViewToast(this, Html.fromHtml(html),0 , 0);
				break;
			case R.id.string_format:
				String time = v.getContentDescription().toString();
				try {
					ToastUtil.showViewToast(this, TimeUtil.getTime(time,"yyyy-MM-dd HH:mm", "MM/dd"), 0 , Color.RED);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case R.id.string_calendar:
				String time1 = v.getContentDescription().toString();
				try {
					Calendar calendar = TimeUtil.getTime(time1, "yy-MM-dd HH:mm:ss E");
					ToastUtil.showOnceToast(this, "year = "+calendar.get(Calendar.YEAR)+" month = "+calendar.get(Calendar.MONTH)+" E = "+calendar.get(Calendar.DAY_OF_WEEK));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
		}
	}
}

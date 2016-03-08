package com.yao.testdemo.toast;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yao.testdemo.R;
import com.yao.testdemo.util.ToastUtil;

/**
 * Created by Yao on 2016/2/23 0023.
 */
public class ToastTest extends Activity {

	private static final String TAG = "ToastTest";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_test_layout);
	}

	public void onAction1(View v){
		switch (v.getId()){
			case R.id.system_toast:
				ToastUtil.systemToast(this, "系统的Toast");
				break;
			case R.id.once_toast:
				ToastUtil.showOnceToast(this, "多次点击显示一次的Toast");
				break;
			case R.id.view_toast:
				View view = ToastUtil.showViewToast(this, R.layout.my_toast_layout);
				if(view != null){
					TextView tv = (TextView) view.findViewById(R.id.my_toast_tv_id);
					tv.setText("View Toast");
					tv.setTextColor(Color.RED);
					view.setBackgroundResource(R.drawable.my_toast_background);
				}
				break;
			case R.id.default_view_toast:
				ToastUtil.showViewToast(this, "默认view Toast", 0, 0);
				break;

		}
	}
}

package com.yao.testdemo.testhead;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.yao.testdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试点赞头像控件(基本完善)
 * @author Yao
 */
public class ZambiaActivity extends Activity {

	private static final String TAG="ZambiaActivity";

	private TextView mTVZambia;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_zambia_view_layout);
		mTVZambia=(TextView) findViewById(R.id.zambia);
		//mTVZambia.setZambiaHead();
		List<String> data=new ArrayList<String>();
		data.add("https://www.baidu.com/img/bd_logo1.png");//http://d.hiphotos.baidu.com/image/pic/item/9f2f070828381f301aa29da8ab014c086e06f07c.jpg
		data.add("http://f.hiphotos.baidu.com/image/pic/item/0824ab18972bd407801cccfc79899e510eb309d3.jpg");
		data.add("http://d.hiphotos.baidu.com/image/pic/item/9f2f070828381f301aa29da8ab014c086e06f07c.jpg");
		data.add("https://www.baidu.com/img/bd_logo1.png");//http://f.hiphotos.baidu.com/image/pic/item/0824ab18972bd407801cccfc79899e510eb309d3.jpg
		data.add("http://d.hiphotos.baidu.com/image/pic/item/9f2f070828381f301aa29da8ab014c086e06f07c.jpg");
		data.add("http://f.hiphotos.baidu.com/image/pic/item/0824ab18972bd407801cccfc79899e510eb309d3.jpg");
		data.add("");
		ZambiaUtil.getInstance(this).setHeadData(data, mTVZambia);
	}


}
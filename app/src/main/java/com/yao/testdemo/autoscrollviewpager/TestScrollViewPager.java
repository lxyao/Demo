package com.yao.testdemo.autoscrollviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.yao.testdemo.R;
import com.yao.testdemo.autoscrollviewpager.view.ScrollViewPagerProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试滚动  viewpager(基本测试稳定)
 * @author Yao
 */
public class TestScrollViewPager extends Activity {

	private static final String TAG="TestScrollViewPager";

	private ViewPager mVPAuto,mVPLoop;

	ScrollViewPagerProxy<Integer> proxy=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_scroll_viewpager_layout);
		//mVPAuto = (ViewPager) findViewById(R.id.auto_scroll_viewpager);
		//mVPLoop = (ViewPager) findViewById(R.id.loop_fling_viewpager);
		List<Integer> data = new ArrayList<Integer>();
		for(int i=0; i<4; i++)
			data.add(R.mipmap.home_advert_default);
		proxy =new ScrollViewPagerProxy<Integer>(this ,R.id.auto_scroll_viewpager, R.id.auto_scroll_ll_point, R.layout.introduct_point_layout);
		proxy.setLoopFling(false, 0);
		proxy.setViewPagerData(data);

	}


	@Override
	protected void onStart() {
		super.onStart();
		proxy.startScroll(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		proxy.stopScroll();
	}
}
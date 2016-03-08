package com.yao.testdemo.translateanimation;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yao.testdemo.R;

/**
 * 测试隐藏布局时  scrollview慢慢向上移动(动画效果完整)
 * @author Yao
 */
public class TranslateAnimation extends Activity {

	private static final String TAG="TranslateAnimation";

	private ListView mlist;
	private TextView mTop;

	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_scrollview_slow_roll_up);
		mlist = (ListView) findViewById(R.id.test_scrollview_slow_roll_up_list);
		mTop = (TextView) findViewById(R.id.test_scrollview_slow_roll_up_id);
		final android.view.animation.TranslateAnimation translate=
				new android.view.animation.TranslateAnimation(Animation.RELATIVE_TO_SELF, 0 , Animation.RELATIVE_TO_SELF, 0 ,Animation.ABSOLUTE, 0, Animation.ABSOLUTE , -300f);
		translate.setDuration(500);
		translate.setFillAfter(true);

		final android.view.animation.TranslateAnimation translate1=
				new android.view.animation.TranslateAnimation(Animation.RELATIVE_TO_SELF, 0 , Animation.RELATIVE_TO_SELF, 0 ,Animation.ABSOLUTE, -0, Animation.ABSOLUTE , +300f);
		translate1.setDuration(500);
		translate1.setFillAfter(true);

		//final ScaleAnimation sa = new ScaleAnimation(0, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0);
		final ScaleAnimation sa = new ScaleAnimation(1, 1, 1, 0);
		sa.setDuration(500);
		sa.setFillAfter(true);

		final ScaleAnimation sa1 = new ScaleAnimation(1, 1, 0, 1);
		sa1.setDuration(500);
		sa1.setFillAfter(true);

		//final android.view.animation.TranslateAnimation translate = new android.view.animation.TranslateAnimation(1, 0, 0, -1);
		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.d(TAG, "-------------->  动画执行完成");
				mTop.setVisibility(View.GONE);
				mlist.clearAnimation();
				mTop.clearAnimation();
			}
		});

		translate1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mTop.setVisibility(View.VISIBLE);
				mlist.clearAnimation();
				mTop.clearAnimation();
			}
		});
		sv=(ScrollView) findViewById(R.id.scrollview);

		mTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "--------------> 点击了吗");
//				//mTop.setVisibility(View.INVISIBLE);
//				//mlist.setAnimation(translate);
//				mlist.startAnimation(translate);
//				mTop.startAnimation(sa);
//				//mlist.startAnimation(translate);
//				//mTop.setAnimation(translate);

				//方式一  可以实现缓慢向上滚
				int y = sv.getScrollY();
				if(y < 300){
					sv.smoothScrollBy(0, 300-y);
				}

				//方式二  使用scrollBy如何实现循环滚动
//				sv.post(runnable);

			}
		});

		findViewById(R.id.test_scrollview_slow_roll_up_other).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mlist.startAnimation(translate1);
				mTop.startAnimation(sa1);
				//mTop.setVisibility(View.VISIBLE);
			}
		});

	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			int y = sv.getScrollY();
			Log.d(TAG, "-------->   y="+y);
			if(y < 300){
				sv.scrollBy(0, 30);
				sv.postDelayed(this, 20);
			}else sv.post(null) ;
		}
	};
}
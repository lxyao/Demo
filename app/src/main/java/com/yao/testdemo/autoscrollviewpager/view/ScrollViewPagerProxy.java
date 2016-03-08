package com.yao.testdemo.autoscrollviewpager.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚动的  ViewPager
 * @author Yao
 */
public class ScrollViewPagerProxy<T> extends Handler implements IScrollView {

	private static final String TAG="ScrollViewPagerProxy";

	private Context mContext;
	private ViewPager mViewPager;

	private int mViewPagerHeight;
	private boolean isAuto, isLoop;

	private ScrollViewPagerAdapter<T> mAdapter;

	private ViewGroup mPointViewGroup=null;
	private List<CheckBox> mPointViews;
	private int mPointViewId;

	private int currentSize = 20;

	public ScrollViewPagerProxy(Activity context, int viewPagerId,int pointViewGroup,int pointViewId) {
		this.mContext = context;
		mViewPager = (ViewPager) context.findViewById(viewPagerId);
		mPointViewGroup = (ViewGroup) context.findViewById(pointViewGroup);
		mPointViewId = pointViewId;
		init();
	}

	public ScrollViewPagerProxy(Activity context, ViewPager view,ViewGroup pointViewGroup,int pointViewId) {
		this.mViewPager = view;
		this.mContext = context;
		mPointViewGroup = pointViewGroup;
		mPointViewId = pointViewId;
		init();
	}

	private void init(){
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(this);
		mAdapter = new ScrollViewPagerAdapter<T>(mContext);
		onSizeViewPagerHeight(0);
	}

	public void onSizeViewPagerHeight(int size){
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		if(size<=0 || size == 2)mViewPagerHeight = width >> 1;
		else mViewPagerHeight = width/size;
		mAdapter.setViewHeight(mViewPagerHeight);
		mViewPager.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, mViewPagerHeight));
	}

	@Override
	public void startScroll(boolean isAuto) {
		if(isLoop)mViewPager.setCurrentItem(totalPoint * currentSize, true);
		this.isAuto = isAuto;
		if(mAdapter.getCount() > 0 && isAuto)
			sendMessageDelayed(obtainMessage(0, currentIndex, 0), 2000);
	}

	@Override
	public void stopScroll() {
		removeCallbacksAndMessages(null);
	}

	@Override
	public void setLoopFling(boolean isLoop, int currentSize) {
		this.isLoop = isLoop;
		this.currentSize = currentSize <= 0 ? this.currentSize : currentSize;
		mAdapter.setLoopFling(isLoop);
	}

	public void setViewPagerData(List<T> data){
		if(mPointViewId > 0){
			if(mPointViewGroup == null)throw new RuntimeException("pointViewGroup don't null(引导点不能没有容器)");
			LayoutInflater inflater = LayoutInflater.from(mContext);
			mPointViews = new ArrayList<CheckBox>();
			for(int i =0; i< data.size(); i++){
				CheckBox view = (CheckBox) inflater.inflate(mPointViewId, mPointViewGroup, false);
				mPointViews.add(view);
				mPointViewGroup.addView(view, i);
			}
			if(mPointViews.size() > 0)mPointViews.get(0).setChecked(true);
		}
		totalPoint = data == null ? 0 : data.size();
		mAdapter.setViewData(data);
		mViewPager.setAdapter(mAdapter);
	}

	int currentIndex=1;//当前  引导点 位置
	boolean isLooper=true;//是否循环

	private static int totalPoint;

	@Override
	public void handleMessage(Message msg) {
		if(msg.what == 0){
			mViewPager.setCurrentItem(msg.arg1);
			currentIndex++;
			if(currentIndex >= mAdapter.getCount())currentIndex=0;
			if(isLoop || isAuto)sendMessageDelayed(obtainMessage(0, currentIndex, 0), 2000);
		}else if(msg.what == 2){
			Log.d(TAG, "------------------>  触发了清空");
			mLongClick = true;
			removeCallbacksAndMessages(null);
		}else
			super.handleMessage(msg);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if(isAuto){
			if(arg0 == 1){
				isLooper=false;
				removeCallbacksAndMessages(null);
			}else if(arg0 == 0 && !isLooper){
				isLooper=true;
				sendMessageDelayed(obtainMessage(0, currentIndex, 0), 2000);
			}
		}
		mLongClick=false;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		Log.d(TAG, "------------------> arg0="+arg0);
		currentIndex = arg0;
		for(int i=0;i< totalPoint; i++){
			if(i == (arg0 % totalPoint))
				mPointViews.get(i).setChecked(true);
			else
				mPointViews.get(i).setChecked(false);
		}
	}

	private boolean mLongClick;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				sendEmptyMessageDelayed(2, 200);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				if(mLongClick){
					mLongClick= false;
					sendMessageDelayed(obtainMessage(0, currentIndex, 0), 2000);
				}

				break;
		}
		return false;
	}
}
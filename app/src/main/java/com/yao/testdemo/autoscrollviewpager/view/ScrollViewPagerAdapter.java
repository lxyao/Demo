package com.yao.testdemo.autoscrollviewpager.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.List;

public class ScrollViewPagerAdapter<T> extends PagerAdapter{

	private static final String TAG="ScrollViewPagerAdapter";
	
	//private ScrollViewPagerProxy mProxy;
	private Context mContext;
	
	private List<T> mData=null;
	public void setViewData(List<T> data){
		mData = data;
	}
	
	private boolean isLoop;
	public void setLoopFling(boolean isLoop){
		this.isLoop = isLoop;
	}
	
	private int mViewHeight;
	public void setViewHeight(int height){
		this.mViewHeight = height;
	}
	
	public ScrollViewPagerAdapter(Context context) {//,ScrollViewPagerProxy proxy
//		this.mProxy = proxy;
		this.mContext = context;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if(object instanceof View)
			container.removeView((View)object);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	public Object instantiateItem(ViewGroup container, int position) {
		ImageView iv=new ImageView(mContext);
		iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mViewHeight));
		iv.setScaleType(ScaleType.FIT_XY);
		container.addView(iv);
		T mObject = (T) mData.get(position % mData.size());
		if(mObject instanceof Integer)
			iv.setImageResource((Integer)mObject);
		else if(mObject instanceof String){//��������  ��  uri
		}
		return iv;
	}

	@Override
	public int getCount() {
//		List<?> data = mProxy.getViewPagerData();
		if(mData == null || mData.size() == 0)return 0;
		if(isLoop){
			return Integer.MAX_VALUE>>2;
		}
		return mData.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
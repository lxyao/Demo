package com.yao.testdemo.emoji;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * �������������
 * @author Yao
 *
 */
public class EmojiViewPagerAdapter extends PagerAdapter {

	private static final String TAG="EmojiViewPagerAdapter";
	
	List<View> mData=null;
	
	public EmojiViewPagerAdapter(List<View> data){
		this.mData=data;
	}
	
	@Override
	public int getCount() {
		return mData==null?0:mData.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//super.instantiateItem(container, position);
		container.addView(mData.get(position));
		return mData.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		//super.destroyItem(container, position, object);
		container.removeView(mData.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
	
	
	
}

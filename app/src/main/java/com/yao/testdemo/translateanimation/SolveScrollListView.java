package com.yao.testdemo.translateanimation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SolveScrollListView extends ListView implements OnScrollListener{

	private final static String TAG="SolveScrollListView";
	
	private int screenHeight=0;
	
	public SolveScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SolveScrollListView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		setOnScrollListener(this);
		DisplayMetrics metrics=getResources().getDisplayMetrics();
		screenHeight=metrics.heightPixels;
	}
	
	public SolveScrollListView(Context context) {
		this(context, null);
		
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int temp=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>1, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, temp);
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		if(arg1==SCROLL_STATE_IDLE){
			int y=arg0.getScrollY();
			Log.e(TAG, "=========>  ������λ��   y="+y);
			if(y==screenHeight)scrollBy(0, screenHeight+50);
		}
	}

	
}

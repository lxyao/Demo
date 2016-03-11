package com.yao.testdemo.copyelememenu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Yao on 2016/3/10 0010.
 */
public class ListViewExpanded extends ListView {
	public ListViewExpanded(Context context) {
		super(context);
	}

	public ListViewExpanded(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setDividerHeight(0);
	}

	public ListViewExpanded(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDividerHeight(0);

	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
				Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
	}
}

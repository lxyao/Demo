package com.yao.testdemo.update_pulltorefreshview.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.yao.testdemo.R;
import com.yao.testdemo.update_pulltorefreshview.PullToRefreshBase.Mode;
import com.yao.testdemo.update_pulltorefreshview.PullToRefreshBase.Orientation;

public class CustomHeaderLayout extends LoadingLayout {

	public CustomHeaderLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.loading_data_dot_anim;
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {

	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
	}

	@Override
	protected void pullToRefreshImpl() {
	}

	@Override
	protected void refreshingImpl() {
	}

	@Override
	protected void releaseToRefreshImpl() {
	}

	@Override
	protected void resetImpl() {
	}
}

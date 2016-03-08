package com.yao.testdemo.autoscrollviewpager.view;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View.OnTouchListener;

/**
 * 滚动处理接口
 * @author Yao
 */
public interface IScrollView extends OnPageChangeListener,OnTouchListener{
	/**
	 * 开始滚动
	 */
	void startScroll(boolean isAuto);
	/**
	 * 停止滚动
	 */
	void stopScroll();

	/**
	 * 是否循环滑动  ,并设置当前选中的偏移倍数
	 * @param isLoop 是否循环
	 * @param currentSize 初始化时选中的位置  ，它是实际条数 需要乘以的基数 eg：5*20    默认20
	 */
	void setLoopFling(boolean isLoop,int currentSize);

}

package com.yao.testdemo.webview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Yao on 2016/1/27 0027.
 */
public class XWebView extends WebView {

	private static final String TAG= "XWebView";

	public XWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public XWebView(Context context, AttributeSet attrs) {
		this(context, attrs, attrs != null ? attrs.getStyleAttribute() : 0);
	}

	public XWebView(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		int sc = canvas.saveLayer(0,0,getWidth(),getHeight(), null, Canvas.ALL_SAVE_FLAG);
//		LogCat.d(TAG, "---------------------> sc="+sc);
//		Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.RGB_565);
//		Canvas canvas = new Canvas(bitmap);
////		super.onDraw(canvas);
//		//int sc = canvas.save();
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
//		p.setColor(Color.RED);
//		canvas.drawRect(0, 0, getWidth(), getHeight(), p);
		p.setColor(Color.GREEN);
		p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawCircle(100, 100, 50, p);
////		canvas.restoreToCount(sc);
//		canvas1.drawBitmap(bitmap, 0, 0, null);
	}



}

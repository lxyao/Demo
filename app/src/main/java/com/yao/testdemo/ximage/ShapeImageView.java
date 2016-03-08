package com.yao.testdemo.ximage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import com.yao.testdemo.R;
import com.yao.testdemo.util.CheckUtil;

public class ShapeImageView extends AbstractXImageView {

	private static final String TAG = "ShapeImageView";
	
	public ShapeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(attrs != null){
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
			array.recycle();
		}
	}

	public ShapeImageView(Context context) {
		super(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(changed && mBitmap == null)
			mBitmap = getDrawable() != null ? ((BitmapDrawable)getDrawable()).getBitmap() : null;
	}

	@Override
	protected Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	protected void setImageUrl(String url) {
		switch(CheckUtil.getPathHost(url)){
		case 1:
		case 3:
			//LoadImage.getInstance(mContext).setImageUrl(this, url);
			break;
		case 2:
			setImageURI(Uri.parse(url));
			break;
			default:
				Log.e(TAG, "---------url is null or \"\"---------->");
		}
	}

	@Override
	protected void startDraw(Canvas canvas, Paint p) {
		if(mBitmap != null && p != null){
			p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(mBitmap, mCenterX, mCenterY, p);
		}
	}

}

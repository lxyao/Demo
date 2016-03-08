package com.yao.testdemo.cleartext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

import com.yao.testdemo.R;

public class TestClearActivity extends Activity {

	private XEditText Show,Auto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_clear_edit_layout);
//		setContentView(new ShapesView(this));

		
//		Auto=(XEditText) findViewById(R.id.auto_show_clear);
//		Show=(XEditText) findViewById(R.id.show_clear);
//		Drawable mClearButton=getResources().getDrawable(android.R.drawable.ic_notification_clear_all);
//		Rect rect = new Rect(DentisyUtil.pix2dp(this, 3), 0, DentisyUtil.pix2dp(this, 25), DentisyUtil.pix2dp(this, 22));
//		mClearButton.setBounds(rect);
//		Auto.setShowClearType(1);
//		Auto.setCompoundDrawables(null, null, mClearButton, null);
//		Show.setShowClearType(2);
//		//Show.setCompoundDrawables(null, null, mClearButton, null);
//		Show.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_notification_clear_all, 0);
	}
	

	    static class ShapesView extends View {
	        private final Paint mNormalPaint;
	        private final Paint mStrokePaint;
	        private final Paint mFillPaint;
	        private final RectF mRect;
	        private final RectF mOval;
	        private final RectF mArc;
	        private final Path mTriangle;

	        ShapesView(Context c) {
	            super(c);

	            mRect = new RectF(0.0f, 0.0f, 160.0f, 90.0f);

	            mNormalPaint = new Paint();
	            mNormalPaint.setAntiAlias(true);
	            mNormalPaint.setColor(0xff0000ff);
	            mNormalPaint.setStrokeWidth(6.0f);
	            mNormalPaint.setStyle(Paint.Style.FILL_AND_STROKE);

	            mStrokePaint = new Paint();
	            mStrokePaint.setAntiAlias(true);
	            mStrokePaint.setColor(0xff0000ff);
	            mStrokePaint.setStrokeWidth(6.0f);
	            mStrokePaint.setStyle(Paint.Style.STROKE);
	            
	            mFillPaint = new Paint();
	            mFillPaint.setAntiAlias(true);
	            mFillPaint.setColor(0xff0000ff);
	            mFillPaint.setStyle(Paint.Style.FILL);

	            mOval = new RectF(0.0f, 0.0f, 80.0f, 45.0f);
	            mArc = new RectF(0.0f, 0.0f, 100.0f, 120.0f);

	            mTriangle = new Path();
	            mTriangle.moveTo(0.0f, 90.0f);
	            mTriangle.lineTo(45.0f, 0.0f);
	            mTriangle.lineTo(90.0f, 90.0f);
	            mTriangle.close();
	        }

	        @Override
	        protected void onDraw(Canvas canvas) {
	            super.onDraw(canvas);

	            canvas.save();
	            canvas.translate(0.0f, 0.0f);
	            canvas.drawRoundRect(mRect, 20.0f, 20.0f, mStrokePaint);

	            canvas.translate(50.0f, 50.0f);
	            canvas.drawRoundRect(mRect, 6.0f, 6.0f, mNormalPaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawRoundRect(mRect, 6.0f, 6.0f, mFillPaint);
	            canvas.restore();

	         /*	canvas.save();
	            canvas.translate(250.0f, 50.0f);
	            canvas.drawCircle(80.0f, 45.0f, 45.0f, mNormalPaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawCircle(80.0f, 45.0f, 45.0f, mStrokePaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawCircle(80.0f, 45.0f, 45.0f, mFillPaint);
	            canvas.restore();

	            canvas.save();
	            canvas.translate(450.0f, 50.0f);
	            canvas.drawOval(mOval, mNormalPaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawOval(mOval, mStrokePaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawOval(mOval, mFillPaint);
	            canvas.restore();

	            canvas.save();
	            canvas.translate(625.0f, 50.0f);
	            canvas.drawRect(0.0f, 0.0f, 160.0f, 90.0f, mNormalPaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawRect(0.0f, 0.0f, 160.0f, 90.0f, mStrokePaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawRect(0.0f, 0.0f, 160.0f, 90.0f, mFillPaint);
	            canvas.restore();

	            canvas.save();
	            canvas.translate(825.0f, 50.0f);
	            canvas.drawArc(mArc, -30.0f, 70.0f, true, mNormalPaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawArc(mArc, -30.0f, 70.0f, true, mStrokePaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawArc(mArc, -30.0f, 70.0f, true, mFillPaint);
	            canvas.restore();
	            
	            canvas.save();
	            canvas.translate(950.0f, 50.0f);
	            canvas.drawArc(mArc, 30.0f, 100.0f, false, mNormalPaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawArc(mArc, 30.0f, 100.0f, false, mStrokePaint);

	            canvas.translate(0.0f, 110.0f);
	            canvas.drawArc(mArc, 30.0f, 100.0f, false, mFillPaint);
	            canvas.restore();

	            canvas.save();
	            canvas.translate(50.0f, 400.0f);
	            canvas.drawPath(mTriangle, mNormalPaint);

	            canvas.translate(110.0f, 0.0f);
	            canvas.drawPath(mTriangle, mStrokePaint);

	            canvas.translate(110.0f, 0.0f);
	            canvas.drawPath(mTriangle, mFillPaint);
	            canvas.restore();*/
	        }
	    }
}

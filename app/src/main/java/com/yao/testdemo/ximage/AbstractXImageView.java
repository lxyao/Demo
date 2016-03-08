package com.yao.testdemo.ximage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yao.testdemo.R;

public abstract class AbstractXImageView extends ImageView {

	private static final String TAG = "AbstractXImageView";
	private final Context mContext;

	protected int mSpecialStyle=NONE;//定义控件样式  默认0  即没有样式
	private float mBorderWidth=5;//设置画笔的宽度  默认为5
	private int mBorderColor=Color.LTGRAY;//默认边框颜色
	private float mRoundRectRadius=10f;//默认圆角矩形的 圆角半径
	private Paint mStylePaint;//默认画笔样式
	protected int mWidth,mHeight;//创建画布时的大小

	private float offsetX,offsetY;//为圆形图片时 x y的偏移量
	protected Bitmap mBitmap;

	public AbstractXImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init(attrs, defStyle);
	}

	public AbstractXImageView(Context context, AttributeSet attrs) {
		this(context, attrs, attrs != null ? attrs.getStyleAttribute() : 0);
	}

	public AbstractXImageView(Context context) {
		this(context, null);
	}

	private void init(AttributeSet attrs, int defStyleRes){
		if(attrs != null){
			TypedArray a=mContext.obtainStyledAttributes(attrs, R.styleable.AbstractXImageView, defStyleRes, defStyleRes);
			//使用它存在一个弊端，就是如果将它设置到了style了就拿不到src值了，所以直接使用getDrawable获取图片
			//src使用的优先级  代码最高  其次布局文件  样式低于布局
			//src=attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
			mSpecialStyle=a.getInt(R.styleable.AbstractXImageView_specialStyle, 0);
			if(mSpecialStyle != 0){
				mBorderWidth = a.getDimension(R.styleable.AbstractXImageView_borderWidth, 5);
				mBorderColor = a.getColor(R.styleable.AbstractXImageView_borderColor, Color.LTGRAY);
				mRoundRectRadius = a.getDimension(R.styleable.AbstractXImageView_roundRectRadius, 10);
			}
			a.recycle();
		}
		setStylePaint();
	}

	//设置画笔的样式
	private void setStylePaint() {
		if (mSpecialStyle > 0) {
			mStylePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mStylePaint.setFilterBitmap(true);
			mStylePaint.setStyle(Style.STROKE);
			mStylePaint.setColor(mBorderColor);
			mStylePaint.setStrokeWidth(mBorderWidth);
		}
	}

	protected int mCenterX,mCenterY;
	//设置控件的高宽
	protected void setWidgetSize() {
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		if (mSpecialStyle != 0) {
			switch (mSpecialStyle) {
				case CIRCLE:
					mHeight = mWidth = Math.min(mWidth, mHeight);
					break;
				case ROUNDRECT:
					mWidth = (mWidth == 0 ? (getSuggestedMinimumWidth() == 0 ? 40 : getSuggestedMinimumWidth()) : mWidth);
					mHeight = (mHeight == 0 ? (getSuggestedMinimumHeight() == 0 ? 40 : getSuggestedMinimumHeight()) : mHeight);
					break;
			}
			if(mBitmap != null){
				int tempWidth = mBitmap.getWidth();
				int tempHeight= mBitmap.getHeight();
				mCenterX = (tempWidth < mWidth) ? ((mWidth - tempWidth)>>1) : 0;
				mCenterY = (tempHeight < mHeight) ? ((mHeight - tempHeight)>>1) : 0;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setWidgetSize();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(mSpecialStyle == CIRCLE){
			if(mWidth < getMeasuredWidth()){//画布宽度  小于  控件宽度
				offsetX=(getMeasuredWidth()-mWidth)>>1;
				offsetY=0;
			}else{
				offsetY=(getMeasuredHeight()-mHeight)>>1;
				offsetX=0;
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas1) {
		if(mSpecialStyle != NONE && mBitmap != null && mBitmap.getWidth() > mWidth ){//默认图片样式不处理
			mBitmap=Bitmap.createScaledBitmap(mBitmap, mWidth, mHeight, true);
		}
		switch(mSpecialStyle){
			case NONE:
				super.onDraw(canvas1);
				startDraw(canvas1, null);
				break;
			case CIRCLE:
				Bitmap bit = createPublicCanvas();
				if(bit != null)
					canvas1.drawBitmap(bit, offsetX, offsetY, null);
				break;
			case ROUNDRECT:
				Bitmap bit1 = createPublicCanvas();
				if(bit1 != null)
					canvas1.drawBitmap(bit1, 0, 0, null);
				break;
		}
		invalidate();
	}
	//创建公共的画布图片
	private Bitmap createPublicCanvas(){
		Bitmap b = null;
		if(mWidth <= 0 && mHeight <= 0) return b;
		b =	Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas=new Canvas(b);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		switch(mSpecialStyle){
			case CIRCLE:
				createCircleBitmap(canvas, mWidth>>1);
				break;
			case ROUNDRECT:
				createRoundRectBitmap(canvas, mRoundRectRadius);
				break;
		}
		return b;
	}
	//创建圆形图片
	private void createCircleBitmap(Canvas canvas, int radius){
		if(mBorderWidth>0)canvas.drawCircle(radius, radius, (radius-(mBorderWidth/2)), mStylePaint);
		Bitmap b=Bitmap.createBitmap(radius<<1, radius<<1, Config.ARGB_8888);
		Canvas canvas1=new Canvas(b);
		canvas1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint p1=new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.WHITE);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		canvas1.drawCircle(radius, radius, (radius-mBorderWidth), p1);
		startDraw(canvas1, p1);
		canvas.drawBitmap(b, 0, 0, null);
	}
	//创建圆角矩形图片
	private void createRoundRectBitmap(Canvas canvas, float radius){
		float tempRadius=mBorderWidth/2;
		RectF rect=new RectF(tempRadius, tempRadius, mWidth-tempRadius, mHeight-tempRadius);
		canvas.drawRoundRect(rect, radius, radius, mStylePaint);
		Bitmap b=Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas1=new Canvas(b);
		canvas1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint p1=new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.WHITE);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		RectF rect1=new RectF(tempRadius+1,tempRadius+1, mWidth-tempRadius-1, mHeight-tempRadius-1);
		canvas1.drawRoundRect(rect1, radius, radius, p1);
		startDraw(canvas1, p1);
		canvas.drawBitmap(b, 0, 0, null);
	}



	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if(mSpecialStyle>0){
			Drawable d=getDrawable();
			if(d!=null)mBitmap=((BitmapDrawable) d).getBitmap();
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if(mSpecialStyle>0){
			Drawable d=getDrawable();
			if(d!=null)mBitmap=((BitmapDrawable) d).getBitmap();
		}
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		if(mSpecialStyle>0 && drawable !=null)mBitmap=((BitmapDrawable)drawable).getBitmap();
		else super.setImageDrawable(drawable);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if(mSpecialStyle>0 && bm!=null)mBitmap=bm;
		else super.setImageBitmap(bm);
	}



	/**
	 * 用于设置图片的地址
	 * @param url <ol>
	 * <li>可以是网络地址 http:// 或者  https://</li>
	 * <li>可以是本地文件 file:///</li>
	 * <li>可以是本地文件绝对路径  File abstractPath</li>
	 * </ol>
	 */
	protected abstract void setImageUrl(String url);

	/**
	 * 用于提供绘制图像
	 * @return 显示的图像
	 */
	protected abstract Bitmap getBitmap();

	protected abstract void startDraw(Canvas canvas, Paint p);

	/**圆形 */
	public final static int CIRCLE=1;
	/**圆角矩形*/
	public final static int ROUNDRECT=2;
	/**无*/
	public final static int NONE=0;
}
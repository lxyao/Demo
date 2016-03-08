package com.yao.testdemo.ximage.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yao.testdemo.R;
import com.yao.testdemo.util.DentisyUtil;

/**
 * 该类用于显示特殊样式的图片，支持圆、圆角矩形、矩形、系统默认形式<br/>
 * 使用setImageUrl设置图片路径，详细使用见方法说明<br/><br/>
 * <font color = 'red'><b>注意：</b>控件功能基本完善</font>
 * @author Yao
 */
public class ShapeImageView1 extends ImageView {

	private static final String TAG="SpecialImageView";

	private Context mContext;//上下文
	/**显示图片*/
	protected Bitmap mBitmap=null;
	/**定义控件样式  默认0  即没有样式 */
	protected int mSpecialStyle = NONE;
	/**设置画笔的宽度  默认为5*/
	private float mBorderWidth = 5;
	/**默认边框颜色 灰色*/
	private int mBorderColor = Color.LTGRAY;
	private Paint mStylePaint;//默认画笔样式
	/**形状背景  默认白色*/
	private int mShapeBackground = Color.WHITE;
	/**默认圆角矩形的圆角半径 默认10px*/
	private float mRoundRectRadius = 10f;

	private float offsetX,offsetY;//为圆形图片时 x y的偏移量
	protected int mWidth,mHeight;//创建画布时的大小
	private int mCenterX,mCenterY;

	public ShapeImageView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init(attrs , defStyle);
	}

	public ShapeImageView1(Context context, AttributeSet attrs) {
		this(context, attrs, attrs != null ? attrs.getStyleAttribute() : 0);
	}

	public ShapeImageView1(Context context) {
		this(context, null);
	}

	//private int src=0;
	private void init(AttributeSet attrs , int defStyleRes){
		if(attrs != null){
			TypedArray a=mContext.obtainStyledAttributes(attrs, R.styleable.ShapeImageView, defStyleRes, 0);
			//使用它存在一个弊端，就是如果将它设置到了style了就拿不到src值了，所以直接使用getDrawable获取图片
			//src使用的优先级  代码最高  其次布局文件  样式低于布局
			//src=attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
			mSpecialStyle=a.getInt(R.styleable.ShapeImageView_specialStyle, 0);
			if(mSpecialStyle!=0){
				mBorderWidth=a.getDimension(R.styleable.ShapeImageView_borderWidth, 5);
				mBorderColor=a.getColor(R.styleable.ShapeImageView_borderColor, Color.LTGRAY);
				mRoundRectRadius=a.getDimension(R.styleable.ShapeImageView_roundRectRadius, 10);
				mShapeBackground = a.getColor(R.styleable.ShapeImageView_shapeBackground, Color.WHITE);
			}
			a.recycle();
		}
		setStylePaint();
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setWidgetSize();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(changed && mBitmap == null)
			mBitmap = getDrawable() != null ? ((BitmapDrawable) getDrawable()).getBitmap() : null;
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
		if(mSpecialStyle > 0){
			Drawable d=getDrawable();
			if(d!=null)mBitmap=((BitmapDrawable) d).getBitmap();
		}
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if(mSpecialStyle>0 && drawable !=null)mBitmap=((BitmapDrawable)drawable).getBitmap();
	}

	@Override
	protected void onDraw(Canvas canvas1) {
		if(mBitmap != null && mBitmap.getWidth() > mWidth){
			mBitmap=Bitmap.createScaledBitmap(mBitmap, mWidth, mHeight, true);
		}
		switch(mSpecialStyle){
			case NONE:
				super.onDraw(canvas1);
				startDraw(canvas1, null);
				break;
			case CIRCLE:
				canvas1.drawBitmap(createPublicCanvas(), offsetX, offsetY, null);
				break;
			case ROUNDRECT:
				canvas1.drawBitmap(createPublicCanvas(), 0, 0, null);
				break;
		}
		invalidate();
	}
	//创建公共的画布图片
	private Bitmap createPublicCanvas(){
		Bitmap b=Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas=new Canvas(b);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		switch(mSpecialStyle){
			case CIRCLE:
				createCircleBitmap(canvas, mWidth >> 1);
				break;
			case ROUNDRECT:
				createRoundRectBitmap(canvas, mRoundRectRadius);
				break;
		}
		return b;
	}
	//创建圆形图片
	private void createCircleBitmap(Canvas canvas, int radius){//Paint p,
		if(mBorderWidth>0)canvas.drawCircle(radius, radius, (radius-(mBorderWidth/2)), mStylePaint);
		Bitmap b=Bitmap.createBitmap(radius<<1, radius<<1, Config.ARGB_8888);
		Canvas canvas1=new Canvas(b);
		canvas1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint p1=new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(mShapeBackground);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		canvas1.drawCircle(radius, radius, (radius - mBorderWidth), p1);
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
		p1.setColor(mShapeBackground);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		RectF rect1=new RectF(mBorderWidth,mBorderWidth, mWidth-mBorderWidth, mHeight-mBorderWidth);
		canvas1.drawRoundRect(rect1, radius, radius, p1);
		startDraw(canvas1, p1);
		canvas.drawBitmap(b, 0, 0, null);
	}

	protected void startDraw(Canvas canvas, Paint p){
		if(mBitmap != null && p != null){
			p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(mBitmap, mCenterX, mCenterY, p);
		}
	}

	//设置画笔的样式
	private void setStylePaint(){
		if(mSpecialStyle > 0){
			mStylePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mStylePaint.setFilterBitmap(true);
			mStylePaint.setStyle(Style.STROKE);
			mStylePaint.setColor(mBorderColor);
			mStylePaint.setStrokeWidth(mBorderWidth);
		}
	}
	//设置控件的高宽
	protected void setWidgetSize(){
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		if(mSpecialStyle!=0){
			switch(mSpecialStyle){
				case CIRCLE:
					mHeight = mWidth = Math.min(mWidth, mHeight);
					break;
				case ROUNDRECT:
					mHeight=(mHeight == 0 ? getSuggestedMinimumHeight() : mHeight);
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

	/**
	 * 设置控件样式
	 * @param style 控件样式
	 */
	public void setSpecialStyle(int style){
		if(style > ROUNDRECT || style < 0){
			mSpecialStyle = NONE;
		}else{
			this.mSpecialStyle = style;
			setStylePaint();
			setWidgetSize();
		}
	}

	/**
	 * 得到控件的样式
	 * @return 样式值
	 */
	public int getSpecialStyle(){
		return mSpecialStyle;
	}
	/**
	 * 设置边框的宽度
	 * @param size 尺寸大小
	 */
	public void setBorderWidth(int size){
		mBorderWidth= DentisyUtil.dp2pix(mContext, size);
		mStylePaint.setStrokeWidth(mBorderWidth);
	}
	/**
	 * 设置边框颜色
	 * @param color 颜色值
	 */
	public void setBorderColor(int color){
		if(mSpecialStyle > 0)mStylePaint.setColor(color);
	}
	/**
	 * 设置圆角矩形  圆角半径大小
	 * @param size 尺寸大小
	 */
	public void setRoundRectRadius(int size){
		mRoundRectRadius=DentisyUtil.dp2pix(mContext, size);
	}
	/**
	 * 设置图形的背景色
	 * @param color 颜色值
	 */
	public void setShapeBackground(int color){
		this.mShapeBackground = color;
	}
	/**
	 * 设置图片的路径
	 * @param url
	 * <ol><li>网络地址url </li>
	 * <li>本地文件地址（也就是文件的绝对路劲）</li>
	 * <li>uri（统一资源标识符）</li></ol>
	 */
	public void setImageUrl(String url){
//		if(CheckUtil.isEmploy(url))return;
//		LoadImage.getInstance(mContext).setImageUrl(this, url);
	}

	/**
	 * 圆形
	 */
	public final static int CIRCLE = 1;
	/**
	 * 圆角矩形
	 */
	public final static int ROUNDRECT = 2;
	/**
	 * 无
	 */
	public final static int NONE = 0;
}

package com.yao.testdemo.chat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
 * <ul>
 * <li>该类能实现不同样式的图片控件，继承自ImageView。</li>
 * <li>可以根据属性   设置控件样式，可以是圆形（CIRCLE） 矩形（ROUNDRECT） 无（NONE）</li>
 * </ul>
 * <font color ='#ff0000'>2016-1-13 发现在将控件设置match时在ScrollView里和LinearLayout里显示不一样，待修改</font>
 * @author Yao
 */
public class SpecialImageView extends ImageView {

	private static final String TAG="SpecialImageView";

	private Context mContext;//上下文
	private Bitmap mBtimap=null;//显示图片

	private int mSpecialStyle=SpecialStyle.NONE;//定义控件样式  默认0  即没有样式
	private float mBorderWidth=5;//设置画笔的宽度  默认为5
	private int mBorderColor=Color.LTGRAY;//默认边框颜色
	private Paint mStylePaint;//默认画笔样式

	private float mRoundRectRadius=10f;//默认圆角矩形的 圆角半径

	private float offsetX,offsetY;//为圆形图片时 x y的偏移量
	private int mWidth,mHeight;//创建画布时的大小

	public SpecialImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init(attrs , defStyle);
	}

	public SpecialImageView(Context context, AttributeSet attrs) {
		this(context, attrs, attrs != null ? attrs.getStyleAttribute() : 0);
	}

	public SpecialImageView(Context context) {
		this(context, null);
	}

	private int src=0;
	private void init(AttributeSet attrs , int defStyleRes){
		if(attrs != null){
			TypedArray a=mContext.obtainStyledAttributes(attrs, R.styleable.SpecialImageView, defStyleRes, defStyleRes);
			//使用它存在一个弊端，就是如果将它设置到了style了就拿不到src值了，所以直接使用getDrawable获取图片
			//src使用的优先级  代码最高  其次布局文件  样式低于布局
			src=attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
			mSpecialStyle=a.getInt(R.styleable.SpecialImageView_specialStyle, 0);
			if(mSpecialStyle!=0){
				mBorderWidth=a.getDimension(R.styleable.SpecialImageView_borderWidth, 5);
				mBorderColor=a.getColor(R.styleable.SpecialImageView_borderColor, Color.LTGRAY);
				mRoundRectRadius=a.getDimension(R.styleable.SpecialImageView_roundRectRadius, 10);
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
		if(changed && mBtimap==null){
			try{
				//mBtimap = getDrawable() != null ? ((BitmapDrawable) getDrawable()).getBitmap() : null;
				mBtimap=BitmapFactory.decodeResource(getResources(), src);
			}catch (OutOfMemoryError e){
				e.printStackTrace();
				System.gc();
				BitmapFactory.Options opt=new BitmapFactory.Options();
				opt.inDither=true;
				opt.inInputShareable=true;
				opt.inJustDecodeBounds=true;
				opt.inPreferredConfig=Config.RGB_565;
				mBtimap=BitmapFactory.decodeResource(getResources(), src, opt);
				if(opt.outHeight>mHeight||opt.outWidth>mWidth){
					int tempSize=Math.max(opt.outWidth, opt.outHeight);
					int temp=Math.max(mWidth, mHeight);
					int te=(int)Math.ceil(tempSize/temp);
					opt.inSampleSize=te>2 ? (te-1) : 1;
				}
				opt.inJustDecodeBounds=false;
				mBtimap=BitmapFactory.decodeResource(getResources(), src, opt);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(mSpecialStyle==SpecialStyle.CIRCLE){
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
			if(d!=null)mBtimap=((BitmapDrawable) d).getBitmap();
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if(mSpecialStyle>0){
			Drawable d=getDrawable();
			if(d!=null)mBtimap=((BitmapDrawable) d).getBitmap();
		}
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		if(mSpecialStyle>0 && drawable !=null)mBtimap=((BitmapDrawable)drawable).getBitmap();
		else super.setImageDrawable(drawable);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if(mSpecialStyle>0 && bm!=null)this.mBtimap=bm;
		else super.setImageBitmap(bm);
	}

	@Override
	protected void onDraw(Canvas canvas1) {
		if(mBtimap!=null && mBtimap.getWidth()>mWidth){
			mBtimap=Bitmap.createScaledBitmap(mBtimap, mWidth, mHeight, true);
		}
		switch(mSpecialStyle){
			case SpecialStyle.NONE:
				super.onDraw(canvas1);
				break;
			case SpecialStyle.CIRCLE:
				canvas1.drawBitmap(createPublicCanvas(), offsetX, offsetY, null);
				break;
			case SpecialStyle.ROUNDRECT:
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
			case SpecialStyle.CIRCLE:
				createCircleBitmap(canvas, mWidth>>1);
				break;
			case SpecialStyle.ROUNDRECT:
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
		p1.setColor(Color.GREEN);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		canvas1.drawCircle(radius, radius, (radius-mBorderWidth), p1);
		if(mBtimap!=null){
			p1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas1.drawBitmap(mBtimap, 0, 0, p1);
		}
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
		RectF rect1=new RectF(mBorderWidth,mBorderWidth, mWidth-mBorderWidth, mHeight-mBorderWidth);
		canvas1.drawRoundRect(rect1, radius, radius, p1);
		if(mBtimap!=null){
			p1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas1.drawBitmap(mBtimap, 0, 0, p1);
		}

		canvas.drawBitmap(b, 0, 0, null);
	}

	//设置画笔的样式
	private void setStylePaint(){
		if(mSpecialStyle>0){
			mStylePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
			mStylePaint.setFilterBitmap(true);
			mStylePaint.setStyle(Style.STROKE);
			mStylePaint.setColor(mBorderColor);
			mStylePaint.setStrokeWidth(mBorderWidth);
		}
	}
	//设置控件的高宽
	private void setWidgetSize(){
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		if(mSpecialStyle!=0){
			switch(mSpecialStyle){
				case SpecialStyle.CIRCLE:
					mHeight=mWidth=Math.min(mWidth, mHeight);
					break;
				case SpecialStyle.ROUNDRECT:
					mHeight=(mHeight==0?getSuggestedMinimumHeight():mHeight);
					break;
			}
		}
	}

	/**
	 * 设置控件样式
	 * @param style 控件样式
	 */
	public void setSpecialStyle(int style){
		if(style>SpecialStyle.ROUNDRECT||style<0){
			mSpecialStyle=SpecialStyle.NONE;
		}else{
			this.mSpecialStyle=style;
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
		mStylePaint.setColor(color);
	}
	/**
	 * 设置圆角矩形  圆角半径大小
	 * @param size 尺寸大小
	 */
	public void setRoundRectRadius(int size){
		mRoundRectRadius=DentisyUtil.dp2pix(mContext, size);
	}
	/**
	 * 设置图片的路径  路径可以是
	 * <ol><li>网络地址url </li>
	 * <li>本地文件地址（也就是文件的绝对路劲）</li>
	 * <li>uri（统一资源标识符）</li></ol>
	 * @param url
	 */
	public void setImageUrl(String url){
//		if(CheckUtil.isEmploy(url))return;
//		LoadImage.getInstance(mContext).setImageUrl(this, url);
	}

	/**
	 * 定义控件的样式
	 * 1表示 圆形  2表示圆角矩形  0表示没有样式
	 * @author Yao
	 */
	public static final class SpecialStyle{
		/**
		 * 圆形
		 */
		public final static int CIRCLE=1;
		/**
		 * 圆角矩形
		 */
		public final static int ROUNDRECT=2;
		/**
		 * 无
		 */
		public final static int NONE=0;
	}
}
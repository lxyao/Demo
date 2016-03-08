package com.yao.testdemo.cleartext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.yao.testdemo.R;
import com.yao.testdemo.util.DentisyUtil;
//import yao.library.util.LogCat;

/**
 * 扩展的编辑文本框
 * 
 * 未优化显示：
 * 
 * 1、光标颜色
 * 
 * 2、字体颜色
 * 
 * 3、是否显示光标效果
 * 
 * 4、是否明文显示输入密码
 * 
 * 5、以上或者以前功能没有实现代码设置的
 * @author Yao
 */
public class XEditText extends EditText {

	private static final String TAG="XEditText";
	/**
	 * 当前上下文
	 */
	private static Context mContext;
	
	/**
	 * 显示清除按钮方式
	 * <ul>
	 * <li>0  默认系统样式   默认值</li>
	 * <li>1  自动显示隐藏  清除按钮</li>
	 * <li>2  始终显示</li>
	 * </ul>
	 */
	private int mShowClearType=0;
	/**
	 * 是否显示密码边框
	 */
	private boolean mIsBorderPassword;
	/**
	 * 设置密码框  线宽  默认 3px
	 */
	private int mBorderWidth;
	/*
	 * 设置密码框  形状 默认值1
	 * <ul>
	 * <li>1  矩形</li>
	 * <li>2  圆角矩形</li></ul>
	 */
	//private int mBorderType=1;
	/**
	 * 设置边框圆角弧度
	 */
	private int mBorderRadius=0;
	
	private int mBorderColor=Color.BLACK;
	private int mCursorColor=mBorderColor;
	private String mShowChar="•";
	
	private int mWidth,mHeight;
	/**
	 * 边框密码长度 默认6位  最大12位
	 */
	private int mBorderLength=6;
	/**
	 * 边框画笔
	 */
	private Paint mBorderPaint;
	//创建边框画笔
	private void createBorderPaint(){
		mBorderPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		mBorderPaint.setStyle(Style.STROKE);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		mBorderPaint.setDither(true);
		mBorderPaint.setFilterBitmap(true);
		mBorderPaint.setColor(mBorderColor);
	}
	
//	private float mSizeWidth,mY;
	private Paint mVerticalLinePaint;
	private void createVerticalLinePaint(){
		mVerticalLinePaint = new Paint(mBorderPaint);
		mVerticalLinePaint.setStyle(Style.FILL);
		//mVerticalLinePaint.setStrokeWidth(mBorderWidth>>1);
//		
////		mVerticalLinePaint.setTextSize(getTextSize());
////		mVerticalLinePaint.setFakeBoldText(true);
////		FontMetrics fm = mVerticalLinePaint.getFontMetrics();
////		mY =(int) (mHeight - fm.bottom - fm.top)/2;
////		mSizeWidth = (fm.descent - fm.ascent);
	}
	
	private int mItemBorderWidth;
	private RectF rect=null;
	
	private void createItemBorder(){
		rect = new RectF(mBorderWidth>>1, mBorderWidth>>1, (mWidth-(mBorderWidth>>1)), (mHeight-(mBorderWidth>>1)));
		mItemBorderWidth=mWidth/mBorderLength;
	}
	
	
	public XEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init(attrs);
	}
	
	public XEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}
	
	public XEditText(Context context) {
		this(context, null);
	}
	
	private void init(AttributeSet attrs){
		if(attrs != null){
			TypedArray array=mContext.obtainStyledAttributes(attrs, R.styleable.XEditText);
			if(array.hasValue(R.styleable.XEditText_showClearType))
				mShowClearType=array.getInt(R.styleable.XEditText_showClearType, 0);
			if(mShowClearType > 0)initClearText(R.mipmap.shanchu);
			mIsBorderPassword=array.getBoolean(R.styleable.XEditText_borderPassword, false);
			if(mIsBorderPassword){
				mBorderLength=attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLength", 6);
				mBorderWidth=array.getDimensionPixelSize(R.styleable.XEditText_borderWidth, 3);
				//mBorderType=array.getInt(R.styleable.XEditText_borderType, 1);
				mBorderRadius=array.getDimensionPixelSize(R.styleable.XEditText_borderRadius, 0);
				
				mBorderColor = array.getColor(R.styleable.XEditText_borderColor, Color.BLACK);
				mCursorColor = array.getColor(R.styleable.XEditText_cursorColor, mBorderColor);
				mShowChar = array.hasValue(R.styleable.XEditText_showChar) ? array.getString(R.styleable.XEditText_showChar) : "•";
				
				createBorderPaint();
				createVerticalLinePaint();
			}
			//Log.d(TAG, "--------------->mIsBorderPassword= "+mIsBorderPassword+"      mBorderWidth= "+mBorderWidth+"   mBorderType= "+mBorderType+"    mBorderRadius="+mBorderRadius+"   mBorderLength="+mBorderLength);
			array.recycle();
		}
	}

	/**
	 * 清除按钮图标
	 */
	private Drawable mClearButton;
	/**
	 * 从配置文件里初始化清除控件
	 */
	private void initClearText(int clearDrawableId){
		mClearButton=getResources().getDrawable(clearDrawableId);
		Rect rect = new Rect(DentisyUtil.pix2dp(getContext(), 3), 0, DentisyUtil.pix2dp(getContext(), 25), DentisyUtil.pix2dp(getContext(), 22));
		if(mClearButton != null){
//			LogCat.D(TAG, "----------initClearText---> mClearButton="+mClearButton);
			mClearButton.setBounds(rect);
			isAutoShowClear();
		}else setCompoundDrawables(null, null, null, null);
	}
	/**
	 * 是否自动显示清除图标
	 */
	private void isAutoShowClear(){
//		LogCat.D(TAG, "--------isAutoShowClear--------> ");
		if(mShowClearType == 1){
			addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
//					LogCat.D(TAG, "----------afterTextChanged---> arg0="+arg0.toString());
					if(arg0.length() > 0)setCompoundDrawables(null, null, mClearButton, null);
					else setCompoundDrawables(null, null, null, null);
				}
			});
		}else if(mShowClearType == 2){
			if(mClearButton != null)setCompoundDrawables(null, null, mClearButton, null);
		}else setCompoundDrawables(null, null, null, null);
	}
	/**
	 * 使用代码  设置清除文本  样式
	 * @param type
	 * <ul>
	 * <li>0  默认系统样式   默认值</li>
	 * <li>1  自动显示隐藏  清除按钮</li>
	 * <li>2  始终显示</li></ul>
	 */
	public void setShowClearType(int type){
		this.mShowClearType=type;
		isAutoShowClear();
	}
	
	/**
	 * 通过代码设置清除按钮图标
	 * 配合 setShowClearType(int type)方法使用  否则默认调用系统的
	 */
	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if(mShowClearType > 0 && right != null){
			mClearButton=right;
			if(mShowClearType == 1 && !TextUtils.isEmpty(getText().toString())){
				super.setCompoundDrawables(left, top, right, bottom);
			}else if(mShowClearType == 2)super.setCompoundDrawables(left, top, right, bottom);
		}else
			super.setCompoundDrawables(left, top, right, bottom);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth=getMeasuredWidth();
		mHeight=getMeasuredHeight();
		createItemBorder();
	}
	
	private IPasswordChange mPasswordChange;
	/**
	 * 设置密码改变监听事件
	 * @param callback
	 */
	public void setOnPasswordChange(IPasswordChange callback){
		this.mPasswordChange = callback;
	}
	
	private String end = "";
	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		if(mIsBorderPassword){
			if(!TextUtils.isEmpty(text))end = text.length() < mBorderLength ? text.toString() : text.subSequence(0, mBorderLength).toString();
			else end = "";
			if(lengthAfter == mBorderLength){
				if(mPasswordChange != null)mPasswordChange.onMaxLength(lengthAfter, text.toString());
				else setText(text);
			}
		}else
			super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mIsBorderPassword){
			int saveCount = canvas.save();
			canvas.drawRoundRect(rect, mBorderRadius, mBorderRadius, mBorderPaint);
			for(int i=1 ;i < mBorderLength; i++){
				canvas.drawLine(i*(mItemBorderWidth), 0, i*(mItemBorderWidth), i*mHeight, mVerticalLinePaint);
			}
			mVerticalLinePaint.setTextSize(getTextSize());
			mVerticalLinePaint.setFakeBoldText(true);
			float size = mVerticalLinePaint.measureText(mShowChar);
			FontMetrics fm = mVerticalLinePaint.getFontMetrics();
			int y =(int) (mHeight - fm.bottom - fm.top)/2;
			int tempx =(int) (mItemBorderWidth - size)/2 ;
			for(int i = 0; i< end.length(); i++){
				int x = (i * mItemBorderWidth) + tempx;
				canvas.drawText(mShowChar, x, y, mVerticalLinePaint);
			}
			if(!isBeat){
				isBeat = true;
			}else {
				if(isFocused())cursorBeat(canvas, (end.length() * mItemBorderWidth) + tempx , y);
			}
			canvas.restoreToCount(saveCount);
		}else super.onDraw(canvas);
	}
	
	boolean isBeat = true;
	private void cursorBeat(Canvas canvas, float x, float y){
		float tempX = x + DentisyUtil.pix2dp(getContext(), 5);
		float tempY = (mHeight - y) - DentisyUtil.pix2dp(getContext(), 5) ;/// 2
		//Log.d(TAG, "---------------------> tempX="+tempX+"       tempY="+tempY);
		canvas.drawLine(tempX, tempY, tempX, mHeight-tempY, mVerticalLinePaint);
		isBeat = !isBeat;
	}
	
//	private Runnable cursorBeat = new Runnable() {
//		
//		@Override
//		public void run() {
//			isBeat = !isBeat;
//			getHandler().removeCallbacks(this);
//			postInvalidate();
//		}
//	};
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
	/** 记录按钮的坐标*/
	private float downX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			downX=event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if(event.getX()<downX)downX=0;
			break;
		case MotionEvent.ACTION_UP:
			if(mShowClearType > 0 && mShowClearType < 3 && mClearButton != null){  
				Rect rect=mClearButton.getBounds();
				if(downX != 0 && (getRight()-(rect.width()<<1))<=downX){
					setText("");
					downX=0;
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			downX=0;
			break;
		}
		performClick();
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getHandler().removeCallbacksAndMessages(null);
	}
}

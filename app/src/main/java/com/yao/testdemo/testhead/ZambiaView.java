package com.yao.testdemo.testhead;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.testdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 点赞头像显示控件
 * @author Yao
 */
public class ZambiaView extends TextView {

	private static final String TAG="ZambiaView";
	/*
	 * 需要处理的一些细节
	 * 
	 * 1、拿到显示的图片  可能是网络的 可能是本地的  可能是内存的
	 * 
	 * 2、将图片处理成圆形
	 * 
	 * 3、将图片显示在这个控件上
	 */

	private Context mContext;
	public ZambiaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
	}

	public ZambiaView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZambiaView(Context context) {
		super(context);
		mContext=context;
	}

	private void init(){

	}

	/**
	 * 点赞头像的宽度 默认60
	 */
	private int mHeadWidth=50;
	/**
	 * 点赞头像的宽度 默认60
	 */
	private int mHeadHeight=50;

	public void setWidgetSize(int width, int height){
		this.mHeadWidth=width;
		this.mHeadHeight=height;
	}



	private Bitmap createCircleBitmap(Bitmap bit){
		int radius=mHeadWidth>>1;
		Bitmap b=Bitmap.createBitmap(mHeadWidth, mHeadWidth, Config.ARGB_8888);
		Canvas canvas1=new Canvas(b);
		canvas1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint p1=new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.WHITE);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		//canvas1.drawCircle(radius, radius, (radius-mBorderWidth), p1);
		canvas1.drawCircle(radius, radius, radius, p1);
		if(bit!=null){
			p1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas1.drawBitmap(bit, -((bit.getWidth()>>1)-radius), -((bit.getHeight()>>1)-radius), p1);
		}
		return b;
	}

	public void setZambiaHead(){
		String text="我是测试数据www.baidu.com  ";
		SpannableStringBuilder ssb=new SpannableStringBuilder();
		this.setMovementMethod(LinkMovementMethod.getInstance());
		for(int i=0;i<5;i++){
			final int d=i;
			SpannableStringBuilder ssb1=new SpannableStringBuilder(text);
			Log.e(TAG, "=================>  长度==ssb.length="+ssb.length());
			ssb1.setSpan(getImageSpan(R.mipmap.ic_launcher), 5, (ssb1.length()-2), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			ssb.append(ssb1);
			ssb.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {
					Log.e(TAG, "===============>  触发了事件吗   i="+d );
					Toast.makeText(mContext, "点击的是  i="+d, Toast.LENGTH_SHORT).show();
				}
			}, ssb.length()-7, (ssb.length()-2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		setText(ssb);
	}

	private ImageSpan getImageSpan(int drawableId){//
		Drawable d=getResources().getDrawable(drawableId);
		//Log.e(TAG, "=============>  width="+d.getIntrinsicWidth()+"   height="+d.getIntrinsicHeight());
		Bitmap b=createCircleBitmap(((BitmapDrawable)d).getBitmap());
		//Bitmap b=createCircleBitmap(bit);
		/*int number=Math.max(mHeadWidth, mHeadHeight)>>1;
		number=DentisyUtil.pix2dp(mContext, number-5);
		d.setBounds(0, 0, number,number);*/
		ImageSpan is=new MyImageSpan(mContext, b);//, ImageSpan.ALIGN_BASELINE
		Rect rect=is.getDrawable().getBounds();
		Log.e(TAG, "============>  rect.top="+rect.top+"   rect.right="+rect.right+"      rect.buttom="+rect.bottom+"  rect.left="+rect.left);
		return is;
	}

	private ImageSpan getImageSpan(String path){
		/*
		 * 要处理的东西
		 * 1、判断路径的来源
		 * 
		 * 2、读取对应的地址
		 * 
		 * 3、显示成imagespan
		 */

		return null;
	}

	private void getReadBitmap(String path){
		new Thread(){
			public void run() {
				try {
					URL url=new URL("http://img0.bdstatic.com/img/image/6446027056db8afa73b23eaf953dadde1410240902.jpg");
					URLConnection conn=url.openConnection();
					conn.connect();
					conn.setReadTimeout(20000);
					conn.setConnectTimeout(20000);
					InputStream is=conn.getInputStream();
					Bitmap b=BitmapFactory.decodeStream(is);
					if(b!=null){
						//getImageSpan(b);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}


	class MyImageSpan extends ImageSpan{

		public MyImageSpan(Context context, Bitmap b) {
			super(context, b);
		}

		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
			return super.getSize(paint, text, start, end, fm);
		}

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
						 Paint paint) {
			Log.e(TAG, "===============>  start="+start+"      end="+end+"          x="+x+"          top="+top+"      y="+y+"        bottom="+bottom);
			super.draw(canvas, text, start, end, x, top, y, bottom, paint);
		}
	}
}
package com.yao.testdemo.ximage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import com.yao.testdemo.R;
import com.yao.testdemo.util.CheckUtil;
import com.yao.testdemo.util.DentisyUtil;
import com.yao.testdemo.util.LogCat;
import com.yao.testdemo.util.MD5;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 该类用于播放gif格式的图片
 *
 * 注意：在指定的路径为平常图片时（即movie为null时）不能显示特殊样式
 * 在大小适中  movie不能显示，设置第一帧图片时  会报错误提示，必须重写onMeasure方法
 * @author Yao
 */
public class MovieImageView extends AbstractXImageView {

	private static final String TAG ="MovieImageView";

	private static final String ASSET_SCHEME = "android_asset";
	private Movie mMovie;
	private int mScreenWidth;
	public MovieImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScreenWidth = DentisyUtil.getScreenWidth(context);
		if(attrs != null){
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MovieImageView);
			if(array.hasValue(R.styleable.MovieImageView_gif)){
				int gifId = array.getResourceId(R.styleable.MovieImageView_gif, 0);
				setMovie(gifId);
			}else if(array.hasValue(R.styleable.MovieImageView_assetsPath)){
				String assetsPath = "file:///" + ASSET_SCHEME + array.getString(R.styleable.MovieImageView_assetsPath);
				setImageUrl(assetsPath);
			}
			array.recycle();
		}
	}

	public MovieImageView(Context context) {
		this(context, null);
	}

	public void setMovie(int gifId){
		if(gifId > 0){
			mMovie = getContext().getResources().getMovie(gifId);
		}else mMovie = null;
	}

	private int width,height;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		if(mSpecialStyle == AbstractXImageView.NONE){
		if(mMovie != null){
			mMovie.setTime(0);
			width = mMovie.width();
			height = mMovie.height();
			int mode = MeasureSpec.getMode(widthMeasureSpec);
			int size = MeasureSpec.getSize(widthMeasureSpec);
			if(width > size && size < mScreenWidth && mSpecialStyle == AbstractXImageView.NONE){//解决占位小于实际大小的问题
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
			}else{
				if(mode == MeasureSpec.AT_MOST){
					widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED);
				}
			}
			mode = MeasureSpec.getMode(heightMeasureSpec);
			size = MeasureSpec.getSize(heightMeasureSpec);
//				if(height > size){//解决占位小于实际大小的问题
//					heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);
//				}else{
			if(mode == MeasureSpec.AT_MOST){
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);
			}else if(mode == MeasureSpec.UNSPECIFIED || height > size){//处理在ScrollView里控件显示不出来的问题
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);
			}
//				}
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
			setWidgetSize();
//				return;
		}else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
//		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if(mSpecialStyle != AbstractXImageView.CIRCLE){
			if(mWidth > getWidth())mWidth = 0;
			else mWidth = (getWidth() > 0 ? ((getWidth() - mWidth)>>1) : mWidth);

			if(mHeight > getHeight())mHeight = 0;
			else mHeight = (getHeight() > 0 ? ((getHeight() - mHeight)>>1) : mHeight);
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected Bitmap getBitmap() {
		return mBitmap;
	}

	String mFileName,mUrl;
	@Override
	protected void setImageUrl(String url) {
		switch(CheckUtil.getPathHost(url)){
			case 1:
				String tempName = MD5.getMD5(url) + ".gif";//构造  文件名
				String filePath = CheckUtil.getCachePath(getContext()) +"/gif" ;
				File file = new File(filePath);
				if(!file.exists())file.mkdirs();
				file = new File(file.getAbsolutePath()+"/"+tempName);
				mFileName = file.getAbsolutePath();
				if(file.exists()){
					try {
						getInputStream(new BufferedInputStream(new FileInputStream(file), 3*1024));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}else{
					this.mUrl = url;
					new Thread(mRunnable).start();
				}
				break;
			case 3:
				url = "file://" + url;
			case 2:
				LogCat.e(TAG, "---------------------> url="+url);
				InputStream is = null;
				int index = url.indexOf(ASSET_SCHEME);
				if ( index > -1){
					String name = url.substring(index + ASSET_SCHEME.length() + 1);
					try {
						is = getResources().getAssets().open(name);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					try {
						is = getContext().getContentResolver().openInputStream(Uri.parse(url));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				getInputStream(new BufferedInputStream(is, 3 * 1024));
				break;
			default:
				Log.e(TAG, "---------url is null or \"\"---------->");
		}
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			//网络下载
			FileOutputStream fos = null;
			BufferedInputStream bis = null;
			try {
				URL hostUrl = new URL(mUrl);
				URLConnection conn = hostUrl.openConnection();
				conn.setReadTimeout(20000);
				conn.setConnectTimeout(20000);
				conn.connect();
				InputStream is = conn.getInputStream();
				if(is != null){
						File file = new File(mFileName);
						fos = new FileOutputStream(file);
						int index = -1;
						byte[] temp = new byte[1024*100];
						bis = new BufferedInputStream(is);
						while((index = bis.read(temp)) != -1){
							fos.write(temp, 0, index);
						}
						getInputStream(new BufferedInputStream(new FileInputStream(file), 3*1024));
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try{
					if(bis != null){
						bis.close();
						bis = null;
					}
					if(fos != null){
						fos.flush();
						fos.close();
						fos = null;
					}

				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	};
	private void getInputStream(InputStream is){
		if(is == null)return;
		is.mark(3*1024);
		try {
			mMovie = Movie.decodeStream(is);
		}catch (OutOfMemoryError e){
			e.printStackTrace();
		}
		mMovie = null;
		if(mMovie != null){
			post(new Runnable() {
				@Override
				public void run() {
					mMovie.setTime(0);
					if(mMovie.width() == 0 || mMovie.height() == 0){
						mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.d_aini_2x);
						setImageBitmap(mBitmap);
						mWidth = mBitmap != null ? mBitmap.getWidth() : 0;//在大小适中的时候  会包错误提示，必须重写onMeasure方法
						mHeight = mBitmap != null ? mBitmap.getHeight() : 0;
					}else{
						requestLayout();
						invalidate();
					}
				}
			});
		}else{
			mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.d_aini_2x);
			LogCat.e(TAG, "-----------44----------------> mbitmap="+mBitmap);
			if(mSpecialStyle == AbstractXImageView.NONE){
				post(new Runnable() {
					@Override
					public void run() {
						setImageBitmap(mBitmap);
					}
				});
			}
		}
	}

	@Override
	protected void startDraw(Canvas canvas, Paint p) {
		if(mMovie != null){//是动态图片
			if(p != null){//绘制不同样式
				p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			}
			drawEachFrame(canvas, p);
		}else{//不是动态图片，或者解析动态图片失败了
			if(p != null && mBitmap != null){
				p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(mBitmap, 0, 0, p);
			}
		}
	}

	long mMovieStart = 0;
	private void drawEachFrame(Canvas canvas,Paint p){
		long now = android.os.SystemClock.uptimeMillis();
		if (mMovieStart == 0) { // first time
			mMovieStart = now;
		}
		int dur = mMovie.duration();
		if (dur == 0) {
			dur = 1000;
		}
		int relTime = (int)((now - mMovieStart) % dur);
		mMovie.setTime(relTime);
		if(mSpecialStyle == AbstractXImageView.CIRCLE){
			mMovie.draw(canvas, -mCenterX, -mCenterY, p);
		}else
			mMovie.draw(canvas, mCenterX, mCenterY, p);
	}
}
package com.yao.testdemo.ximage.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.text.TextUtils;
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
 * 该类支持显示部分gif图片,使用google提供的Movie类实现,支持圆形、圆角矩形、矩形、默认样式显示<br/>
 * 使用setImageUrl方法加载图片,详细使用见方法说明<br/>
 * 如果设置地址或者文件不是.gif格式的结尾的将显示用户设置的特殊样式图片（如是否圆形、边框大小、边框颜色）<br/><br/>
 * <font color = 'red'><b>注意：</b>控件功能基本完善</font>
 * @author Yao
 */
public class MovieImageView1 extends ShapeImageView1 {

	private static final String TAG = "MovieImageView1";

	public static final String ASSET_SCHEME = "file:///android_asset";//定义文件来之assets目录的头
	private Movie mMovie;
	private int mScreenWidth;
	private String mFileName,mUrl;

	private int mWidth1,mHeight1;

	public MovieImageView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = DentisyUtil.getScreenWidth(context);
		if(attrs != null){
			TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MovieImageView, defStyle, 0);
			if(array.hasValue(R.styleable.MovieImageView_gif)){
				int gifId = array.getResourceId(R.styleable.MovieImageView_gif, 0);
				setMovie(gifId);
			}else if(array.hasValue(R.styleable.MovieImageView_assetsPath)){
				String assetsPath = ASSET_SCHEME + array.getString(R.styleable.MovieImageView_assetsPath);
				setImageUrl(assetsPath);
			}
			array.recycle();
		}
	}

	/**
	 * 设置gif的资源目录
	 * @param gifId 资源id
	 */
	public void setMovie(int gifId){
		if(gifId > 0){
			mMovie = getContext().getResources().getMovie(gifId);
		}else mMovie = null;
	}

	public MovieImageView1(Context context, AttributeSet attrs) {
		this(context, attrs, attrs != null ? attrs.getStyleAttribute() : 0);
	}
	
	public MovieImageView1(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(mMovie != null && mBitmap == null){
			mMovie.setTime(0);
			mWidth1 = mMovie.width();
			mHeight1 = mMovie.height();
			int mode = MeasureSpec.getMode(widthMeasureSpec);
			int size = MeasureSpec.getSize(widthMeasureSpec);
			if(mWidth1 > size && size < mScreenWidth && mSpecialStyle == NONE){//解决占位小于实际大小的问题
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth1, MeasureSpec.EXACTLY);
			}else{
				if(mode == MeasureSpec.AT_MOST){
					widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth1, MeasureSpec.UNSPECIFIED);
				}
			}
			mode = MeasureSpec.getMode(heightMeasureSpec);
			size = MeasureSpec.getSize(heightMeasureSpec);
			if(mode == MeasureSpec.EXACTLY){//不做处理
			}else if(mode == MeasureSpec.AT_MOST){
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight1, MeasureSpec.UNSPECIFIED);
			}else if(mode == MeasureSpec.UNSPECIFIED || mHeight1 > size){//处理在ScrollView里控件显示不出来的问题
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight1, MeasureSpec.UNSPECIFIED);
			}
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
			setWidgetSize();
		}else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(mSpecialStyle == CIRCLE){
			mWidth1 = ((mWidth1 >> 1) - (mWidth >> 1));
			mHeight1 =((mHeight1 >> 1) - (mHeight >> 1));
		}else{
			if(mWidth1 > getWidth())mWidth1 = 0;
			else mWidth1 = (getWidth() > 0 ? ((getWidth() - mWidth1)>>1) : mWidth1);
			if(mHeight1 > getHeight())mHeight1 = 0;
			else mHeight1 = (getHeight() > 0 ? ((getHeight() - mHeight1)>>1) : mHeight1);
		}
	}

	@Override
	protected void startDraw(Canvas canvas, Paint p) {
		if(mMovie != null && mBitmap == null){//是动态图片
			if(p != null){//绘制不同样式
				p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			}
			drawEachFrame(canvas, p);
		}else{//不是动态图片，或者解析动态图片失败了
			super.startDraw(canvas, p);
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
		if(mSpecialStyle == CIRCLE){
			mMovie.draw(canvas, -mWidth1, -mHeight1, p);
		}else
			mMovie.draw(canvas, mWidth1, mHeight1, p);
	}

	/**
	 *设置图片的路径
	 * @param url
	 * <ol>
	 *     <li>设置网络地址 http:// | https://</li>
	 *     <li>设置统一资源标识符(Uri) file:///</li>
	 *     <li>设置文件绝对路径</li>
	 *     <li>设置assets文件里的路径（eg：file:///android_asset/gif/xx.gif,也可以用ASSET_SCHEME+"/gif/xx.gif"）</li>
	 * </ol>
	 * 注意：如果路径后缀为.gif可以显示动态图片，反之则显示用户设置的样式
	 */
	@Override
	public void setImageUrl(String url) {
		if(TextUtils.isEmpty(url))return;
		if(url.endsWith(".gif")){//判断是什么格式的图片
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
					LogCat.e(TAG, "---------------------> url=" + url);
					InputStream is = null;
					int index = url.indexOf(ASSET_SCHEME);
					if ( index > -1){
						String name = url.substring(ASSET_SCHEME.length() + 1);
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
		}else super.setImageUrl(url);
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {//网络下载
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
		post(new Runnable() {
			@Override
			public void run() {
				if(mMovie != null){
					mMovie.setTime(0);
					if(mMovie.width() == 0 || mMovie.height() == 0){
						mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.d_aini_2x);
						setImageBitmap(mBitmap);
					}else{
						requestLayout();
						invalidate();
					}
				}else{
					mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.d_aini_2x);
					LogCat.e(TAG, "-----------44----------------> mbitmap="+mBitmap);
					setImageBitmap(mBitmap);
				}
			}
		});
	}
}

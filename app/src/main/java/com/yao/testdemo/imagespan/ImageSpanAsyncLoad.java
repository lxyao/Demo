package com.yao.testdemo.imagespan;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.yao.testdemo.R;
import com.yao.testdemo.util.DentisyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现图文混排的效果（基本完整）
 * @author Yao
 */
public class ImageSpanAsyncLoad {
	Context mContext;
	Drawable mDrawable = null;//默认显示图片
	final LruCache<String,Drawable> drawCache = new LruCache<String, Drawable>((int)(Runtime.getRuntime().maxMemory()>>3));
	int width=0;

	public ImageSpanAsyncLoad(Context context) {
		mContext = context.getApplicationContext();
		width= DentisyUtil.pix2dp(mContext, 100);
		Resources resources = mContext.getResources();
		mDrawable = resources.getDrawable(R.mipmap.d_aini_2x);
		mDrawable.setCallback(null);
		mDrawable.setBounds(10, 5, width+10, width);//mDrawable.getIntrinsicHeight()   mDrawable.getIntrinsicWidth()
	}
	public void displayImage(final String path,final SpannableStringBuilder builder,TextView textView,final List<String> data){
		Drawable drawable = drawCache.get(path);
		boolean isNeedDown = false;
		if(drawable == null){//缓存无图片
			isNeedDown = true;
			drawable = mDrawable;
		}
		ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
		SpannableString spannableString = new SpannableString(path);
		spannableString.setSpan(imageSpan,0,spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				Intent intent=new Intent(mContext.getApplicationContext(), TestImageSpan.class);//TestImageSpan.class 换成你想现实的下级界面
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("image_list", (ArrayList<String>)data);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		}, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		builder.append(spannableString);

		if(isNeedDown){
			int startIndex = builder.getSpanStart(imageSpan);
			int endIndex = builder.getSpanEnd(imageSpan);
			new AsyncTask<Object,Void,Drawable>(){

				String mPath;//下载地址
				SpannableStringBuilder builder;//需要更新的builder
				WeakReference<TextView> weakReference ; //textview对象
				int[] indexs;//imagespan所在的索引位置

				@Override
				protected Drawable doInBackground(Object... params) {//http 执行下载
					mPath = (String)params[0];
					URL url=null;
					Bitmap b=null;
					InputStream is=null;
					URLConnection conn=null;
					try {
						url = new URL(mPath);
						conn=url.openConnection();
						conn.setConnectTimeout(20000);
						conn.setReadTimeout(20000);
						conn.connect();
						is=conn.getInputStream();
						getInputStreamOption(is, 0, 0);
						//b=LoadImage.getInputStreamOption(mPath, is, width, width);//下载方法或自己实现
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (OutOfMemoryError e){
						System.gc();
						b=getInputStreamOption(is, width, width);
					} finally{
						try{
							if(is!=null){
								is.close();
								is=null;
							}
						}catch(IOException e){
							e.printStackTrace();
						}
					}
					builder = (SpannableStringBuilder)params[1];
					weakReference = new WeakReference<TextView>((TextView)params[2]);
					indexs = (int[])params[3];//imagespan 的索引位置
					BitmapDrawable drawable =new BitmapDrawable(b);
					drawable.setTileModeY(Shader.TileMode.CLAMP);
					//drawable.setGravity(Gravity.);
					drawable.setBounds(10, 5, (width+10), width);
					drawable.setCallback(null);
					if(drawable != null)drawCache.put(mPath,drawable);
					return drawable;
				}
				@Override
				protected void onPostExecute(Drawable drawable) {//更新到ui
					if(drawable == null)return;
					TextView textView = weakReference.get();
					//如果不为null与当前的textview对象是等于需要更新的textview对象则进行更新
					if(textView != null && (textView.getTag(R.id.test_imagespan_id) == builder)){//R.id.test_imagespan_id 换成你自己需要更新的textview的id
						ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
						SpannableString spannableString = new SpannableString(mPath);
						spannableString.setSpan(imageSpan, 0, spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						builder.replace(indexs[0],indexs[1],spannableString);//直接替换之前位置的imagespan
						textView.setText(builder);
					}
				}
			}.execute(path,builder,textView,new int[]{startIndex,endIndex});
		}else
			textView.setText(builder);
	}

	private Bitmap getInputStreamOption(InputStream is,int dstWidth,int dstHeight){
		Bitmap b=null;
		if(is!=null){
			try{
				BitmapFactory.Options opt=new BitmapFactory.Options();
				opt.inJustDecodeBounds=true;
				b=BitmapFactory.decodeStream(is, null, opt);
				int be=complateSampleSize(opt.outWidth, opt.outHeight, dstWidth, dstHeight);
				opt.inJustDecodeBounds=false;
				opt.inSampleSize=be;
				b=BitmapFactory.decodeStream(is, null, opt);

			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					if(is!=null){
						is.close();
						is=null;
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return b;
	}

	private static int complateSampleSize(int bWidth,int bHeight,int dstWidth,int dstHeight){
		int be=1;
		if(bWidth>=bHeight&&bWidth>dstWidth)
			be=bWidth/dstWidth;
		else if(bWidth<=bHeight&&bHeight>dstHeight)
			be=bHeight/dstHeight;
		return be;
	}
}
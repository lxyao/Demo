package com.yao.testdemo.testhead;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.yao.testdemo.util.MD5;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 点赞工具处理
 * @author Yao
 */
public final class ZambiaUtil {

	private static final String TAG="ZambiaUtil";

	private static ZambiaUtil util=null;
	private final Context mContext;
	LruCache<String , Bitmap> mLruCache=null;
	Map<String, SoftReference<Bitmap>> mSoftMap=null;

	private ZambiaUtil(Context context){
		mContext=context;
		mSoftMap=new HashMap<String , SoftReference<Bitmap>>();
		mLruCache=new LruCache<String , Bitmap>((int)(Runtime.getRuntime().maxMemory()>>3)){
			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
				super.entryRemoved(evicted, key, oldValue, newValue);
				if(evicted)mSoftMap.put(key, new SoftReference<Bitmap>(oldValue));
			}

			@Override
			protected int sizeOf(String key, Bitmap value) {
				super.sizeOf(key, value);
				return value.getRowBytes()*value.getHeight();
			}
		};
	}

	public static ZambiaUtil getInstance(Context context){
		return util==null?new ZambiaUtil(context):util;
	}

	SpannableStringBuilder sdata=null;
	TextView show=null;
	public void setHeadData(List<String> data,TextView text){
		sdata=null;
		show=null;
		if(data!=null&&data.size()>0&&text!=null){
			this.show=text;
			SpannableStringBuilder ssb=new SpannableStringBuilder();
			text.setMovementMethod(LinkMovementMethod.getInstance());
			for(int i=0;i<data.size();i++){
				String msg=data.get(i);
				int msgLength=msg.length();
				SpannableString ss=new SpannableString(msg+"  ");
				Bitmap b=getCacheBitmap(msg);
				ImageSpan is=new ImageSpan(mContext, createCircleBitmap(b), ImageSpan.ALIGN_BASELINE);
				ss.setSpan(is, 0, msgLength, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				ssb.append(ss);

				if(b==null){
					int start =ssb.getSpanStart(is);
					int end =ssb.getSpanEnd(is);
					sdata=ssb;
					Log.e(TAG, "=============> 缓存里没有东西");
					//getLongTimeBitmap(msg, start, end);
					new LoadPathBitmap().execute(msg,new int[]{start,end});
				}


				int ssbLength=ssb.length();
				final int d=i;
				ssb.setSpan(new ClickableSpan() {
					@Override
					public void onClick(View widget) {
						Toast.makeText(mContext, "点击的位置  i="+d, Toast.LENGTH_SHORT).show();
					}
				}, (ssbLength-msgLength-2), ssbLength-2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//==0?0:(ssbLength-msgLength-1)
			}
			text.setText(ssb , BufferType.SPANNABLE);
		}
	}

	private int mHeadWidth=50;
	private Bitmap createCircleBitmap(Bitmap bit){
		int radius=mHeadWidth>>1;
		Bitmap b=Bitmap.createBitmap(mHeadWidth, mHeadWidth, Config.ARGB_8888);
		Canvas canvas1=new Canvas(b);
		canvas1.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		Paint p1=new Paint(Paint.ANTI_ALIAS_FLAG);
		p1.setColor(Color.WHITE);
		p1.setDither(true);
		p1.setFilterBitmap(true);
		canvas1.drawCircle(radius, radius, radius, p1);
		if(bit!=null){
			bit=Bitmap.createScaledBitmap(bit, mHeadWidth, mHeadWidth, true);
			p1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas1.drawBitmap(bit, -((bit.getWidth()>>1)-radius), -((bit.getHeight()>>1)-radius), p1);
		}
		return b;
	}

	private Bitmap getCacheBitmap(String path){
		Bitmap b=null;
		if(mLruCache!=null){
			b=mLruCache.get(path);
			if(b==null){
				if(mSoftMap!=null&&mSoftMap.size()>0){
					SoftReference<Bitmap> soft=mSoftMap.get(path);
					if(soft!=null){
						b=soft.get();
						if(b==null)mSoftMap.remove(path);
					}
				}
			}
		}
		return b;
	}

	class LoadPathBitmap extends AsyncTask<Object, Void, Bitmap>{

		private int[] index;
		private String path=null;
		@Override
		protected Bitmap doInBackground(Object... params) {
			path=(String) params[0];
			index=(int[]) params[1];
			String external=mContext.getExternalCacheDir()==null?null:mContext.getExternalCacheDir().getAbsolutePath();
			if(external==null||(external!=null&&external.trim().equals("")))
				external=mContext.getCacheDir().getAbsolutePath();
			//Log.e(TAG, "=============>  原来的 external="+external);
			String other= MD5.getMD5(path);
			external=external+"/"+other+".jpg";
			//Log.e(TAG, "=============>  现在的 external="+external);
			Bitmap b=null;
			File f=new File(external);
			if(f.exists()){
				b=BitmapFactory.decodeFile(external);
			}else{
				InputStream is=null;
				try {
					URL url=new URL(path);
					URLConnection conn=url.openConnection();
					conn.setReadTimeout(2000);
					conn.setConnectTimeout(2000);
					conn.connect();
					is=conn.getInputStream();
					b=BitmapFactory.decodeStream(is);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
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
			}

			return b;
		}

		@Override
		protected void onPostExecute(Bitmap b) {
			//super.onPostExecute(result);
			if(b!=null){
				mLruCache.put(path, b);
				SpannableStringBuilder ss=new SpannableStringBuilder(path);
				ImageSpan is=new ImageSpan(mContext, createCircleBitmap(b), ImageSpan.ALIGN_BASELINE);
				ss.setSpan(is, 0, path.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				sdata.replace(index[0], index[1], ss);
				show.setText(sdata , BufferType.SPANNABLE);
			}
		}

	}
}
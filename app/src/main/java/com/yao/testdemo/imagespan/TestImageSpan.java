package com.yao.testdemo.imagespan;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.widget.TextView;

import com.yao.testdemo.R;

/**
 * 尝试让  imagespan里的图片不被压缩，通过重写 imagespan不能起到想要的效果
 * <br/><br/>
 * <font color="#418AFF">找到问题关键很重要，原先在处理的时候一直感觉是imagespan的问题，但是不管怎么改都不成功。因为它是对drawable 或  image 的封装，所以最简单的方法就是，直接对得到接图片做处理</font>
 * <br/><font size="+3" color="#FF3C80">注意:</font><font size="3" color="#FF3C80">
 * <ol>
 * <li>通过drawable的源码了解到，他是可以对图片进行操着的（缩放，裁剪等 通过设置BitmapDrawable.setGravity, 默认的是图片填充控件大小</li>
 * <li>如果你设置了drawable的width、height小于图片快高，
 * 可能图片在显示的时候就会出现挤压的情况，出现这种情况可以是使用BitmapDrawable.setTileModexx()方法来设置图片在X轴或者Y轴上的显示行为  平铺或者重复）</li>
 * </font>
 * </ol>
 * <br/>
 * @author Yao
 */
public class TestImageSpan extends Activity {

	private static final String TAG="TestImageSpan";

	private TextView mTVImageSpan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_imagespan_layout);
		mTVImageSpan = (TextView) findViewById(R.id.test_imagespan_id);
		Drawable d = getResources().getDrawable(R.mipmap.a20151120_564e8f35aac9c);
//1		d.setBounds(0, 0, 150, 150);
//		ClipDrawable cd = new ClipDrawable(d, Gravity.TOP, ClipDrawable.VERTICAL);
//		cd.setBounds(0, 0, 150, 150);
//		cd.setLevel(90*100);

//2
//		ClipDrawable cd = new ClipDrawable(d);
//		cd.setBounds(0, 0, 150, 150);
//		ImageSpan iss = new ImageSpan(cd);

//3
		d.setBounds(0, 0, 150, 150);
		((BitmapDrawable)d).setGravity(Gravity.START);
		ImageSpan iss = new ImageSpan(d);

		SpannableString ss = new  SpannableString("dfdsffsdfsdfsdsfsdsfd滚蛋");
		ss.setSpan(iss, 0, 10, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mTVImageSpan.setText(ss);
	}

	class ClipDrawable extends Drawable{

		private Drawable d;
		private Bitmap b, temp;
		public ClipDrawable(Drawable d) {
			this.d=d;
			temp = Bitmap.createBitmap(150, 150, Config.ARGB_8888);
			if(d != null)b =((BitmapDrawable)d).getBitmap();
		}
		@Override
		public void draw(Canvas canvas) {
			Canvas c = new Canvas(temp);
			//b = Bitmap.createBitmap(b, 0, 0, 150, 150);
			c.drawBitmap(b, 0, 0, new Paint());
			Gravity.apply(Gravity.CENTER, 0, 150, getBounds(), getBounds());
			canvas.drawBitmap(temp, 0, 0, new Paint());
		}

		@Override
		public void setAlpha(int alpha) {

		}

		@Override
		public void setColorFilter(ColorFilter cf) {

		}

		@Override
		public int getOpacity() {
			return 0;
		}

	}
}
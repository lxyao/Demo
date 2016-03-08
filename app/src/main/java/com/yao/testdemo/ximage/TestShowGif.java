package com.yao.testdemo.ximage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.yao.testdemo.R;
import com.yao.testdemo.ximage.other.MovieImageView1;
import com.yao.testdemo.ximage.other.ShapeImageView1;

/**
 * 测试gif图片
 * @author Yao
 */
public class TestShowGif extends Activity {

	private static final String TAG = "TestShowGif";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_ximage_layout);

//		ShapeImageView1 shape1 = (ShapeImageView1) findViewById(R.id.shape1);
//		shape1.setImageResource(R.mipmap.d_yinxian_2x);
//
//		ShapeImageView1 shape2 = (ShapeImageView1) findViewById(R.id.shape2);
//		shape2.setImageResource(R.mipmap.a20151120_564e8f35aac9c);
//
//		ShapeImageView1 shape3 = (ShapeImageView1) findViewById(R.id.shape3);
//		shape3.setImageDrawable(getResources().getDrawable(R.mipmap.a20151120_564e8f35aac9c));

//		String cacheFilePath = getExternalCacheDir() != null ? getExternalCacheDir().getAbsolutePath() : "";
//		if(TextUtils.isEmpty(cacheFilePath)){
//			 cacheFilePath = getCacheDir().getAbsolutePath();
//		}

		MovieImageView1 movie2 = (MovieImageView1) findViewById(R.id.movie2);
		movie2.setImageUrl("http://7xl17c.com2.z0.glb.qiniucdn.com/2016-01-12_56947a7d31ca5.gif");
		movie2.setSpecialStyle(ShapeImageView1.CIRCLE);
		movie2.setBorderColor(Color.RED);
		movie2.setShapeBackground(Color.GREEN);

		MovieImageView1 movie4 = (MovieImageView1) findViewById(R.id.movie4);
		movie4.setImageUrl("http://7xl17c.com2.z0.glb.qiniucdn.com/2016-01-12_56947a7d31ca5.gif");
		movie4.setSpecialStyle(ShapeImageView1.ROUNDRECT);
		movie4.setShapeBackground(Color.GREEN);
		movie4.setBorderColor(Color.RED);


		//file:///mnt/sdcard2/DCIM/mmexport1432437037010.gif  魂斗罗
		//file:///android_asset/gif/60d1e88fa04d59a34e17e8de74da8abe.gif  紫薇眼瞎
		//file:///mnt/sdcard/Android/data/com.yao.testdemo/cache/gif/6ab2cf3886ca8977b5b103b01bc65dd3.gif sdcard文件
		//http://s1.dwstatic.com/group1/M00/80/6C/60d1e88fa04d59a34e17e8de74da8abe.gif  紫薇眼瞎
		//http://s1.dwstatic.com/group1/M00/3E/BD/767a2ffb59b04d8ddba07a86650601bf.gif  科比闲暇
		//http://7xl17c.com2.z0.glb.qiniucdn.com/2016-01-12_56947a7d31ca5.gif  网站qiniu
		//https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3773397243,3085354379&fm=58 jpg格式

//		MovieImageView url3 = (MovieImageView) findViewById(R.id.url3);
//		url3.setImageUrl("/data/data/com.yao.testdemo/cache/gif/6ab2cf3886ca8977b5b103b01bc65dd3.gif");

//		MovieImageView url4 = (MovieImageView) findViewById(R.id.url4);
//		url4.setImageUrl("file:///android_asset/gif/60d1e88fa04d59a34e17e8de74da8abe.gif");//,

//		MovieImageView url5 = (MovieImageView) findViewById(R.id.url5);
//		url5.setImageUrl("http://7xl17c.com2.z0.glb.qiniucdn.com/2016-01-12_56947a7d31ca5.gif");
	}
}
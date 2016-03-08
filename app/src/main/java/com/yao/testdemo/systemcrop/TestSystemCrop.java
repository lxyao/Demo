package com.yao.testdemo.systemcrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yao.testdemo.R;
import com.yao.testdemo.util.LogCat;

public class TestSystemCrop extends Activity {

	private static final String TAG="TestSystemCrop";

	private ImageView mIVPicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_system_crop_error_layout);
		mIVPicture = (ImageView) findViewById(R.id.system_crop_iv_id);
	}

	public void onAction(View v){
		Intent intent = new Intent(getApplicationContext(), HeadSelectPageActivity.class);
		switch(v.getId()){
			case R.id.system_crop_crop_id:
				intent.putExtra(HeadSelectPageActivity.CROP, true);
				intent.putExtra("aspectX", 280);
				intent.putExtra("aspectY", 180);
				break;
			case R.id.system_crop_photo_id:
				intent.putExtra(HeadSelectPageActivity.CROP, false);
				break;
		}
		startActivityForResult(intent, 3);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			String path = data.getStringExtra(HeadSelectPageActivity.KEY_PHOTO_PATH);
			LogCat.e(TAG, "-------111------返回的数据地址--> path = "+ path+" uri = "+ Uri.parse("file://"+path));
			//mIVPicture.setImageURI(Uri.parse("file://"+path));
			final Bitmap bitmap = BitmapFactory.decodeFile(path);
			if(bitmap == null)LogCat.d(TAG, "---------------------> bitmap is null");
			else LogCat.e(TAG, "---------------------> bitmap width="+bitmap.getWidth()+"    heigth="+bitmap.getHeight());
			mIVPicture.postDelayed(new Runnable() {
				@Override
				public void run() {
					mIVPicture.setImageBitmap(bitmap);
				}
			}, 200);
		}
		
	}
}

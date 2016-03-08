package com.yao.testdemo.systemcrop;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yao.testdemo.R;
import com.yao.testdemo.util.CheckUtil;
import com.yao.testdemo.util.LogCat;
import com.yao.testdemo.util.MD5;
import com.yao.testdemo.util.TimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 头像选择方式界面
 *
 * @author Yao
 */
public class HeadSelectPageActivity extends Activity {

	private static final String TAG = "HeadSelectPageActivity";

	private LinearLayout mLLPanl;
	private int outputX, outputY;
	private int aspectX, aspectY;
	private boolean isCrop = false;

	public static final String CROP = "crop";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_upload_selecte_layout);
		mLLPanl = (LinearLayout) findViewById(R.id.head_upload_select_ll_panl);
		lastIntent = getIntent();

		String fileName = TimeUtil.setMillis2Date(0, "yyyy-MM-dd")+"_"+ MD5.getMD5(TimeUtil.getCurrentTimeMillis()+"");
		imageUri = Uri.fromFile(new File(CheckUtil.getCache_FilePath(this) + "/" + fileName + ".jpg"));
		LogCat.d(TAG, "------------------------> imageUri="+imageUri);
		isCrop = lastIntent.getBooleanExtra(CROP, false);
		if(isCrop){
			outputX = lastIntent.getIntExtra("outputX", 0);
			outputY = lastIntent.getIntExtra("outputY", 0);
			aspectX = lastIntent.getIntExtra("aspectX", 480);
			aspectY = lastIntent.getIntExtra("aspectY", 800);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				int[] temp = new int[2];
				mLLPanl.getLocationInWindow(temp);
				int y = (int) event.getY();
				if (temp[1] > y)
					finish();
				break;
		}
		return false;
	}

	/**
	 * 点击事件
	 *
	 * @param v 点击对象
	 */
	public void onImageSelect(View v) {
		switch (v.getId()) {
			case R.id.head_upload_select_tv_clear://关闭界面
				finish();
				break;
			case R.id.head_upload_select_tv_img://从相册里选
//				pickPhoto(aspectX, aspectY, outputX, outputY);
				Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoIntent.setType("image/*");//匹配出图片
				startCamera(photoIntent, imageUri, aspectX, aspectY, outputX, outputY, SELECT_PICTURE_BY_PICK_PHOTO);
				break;
			case R.id.head_upload_select_tv_take://拍照裁剪
				takePhoto();
				break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
		   /*
    	    * 这里说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中，
    	    * 这个方法的好处就是获取的图片是拍照后的原图，如果不是使用ContentValues
    	    * 存放照片路径的话，拍照后获取的图片为缩略图不清晰
    	    */
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启动系统自带的查看图片功能;
//			ContentValues values = new ContentValues();
//			tempPhotoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, SELECT_PICTURE_BY_TAKE_PHONE);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 拍照后对拍摄的照片进行截图操作
	 */
	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 从相册中获取图片
	 */
	private void pickPhoto(int aspectX, int aspectY, int outputX, int outputY) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);//ACTION_GET_CONTENT
		//intent.setType("image/*");//匹配出图片
		intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		intent.putExtra("crop", "true");
		if (aspectX > 0 && aspectY > 0) {
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
		}
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, SELECT_PICTURE_BY_PICK_PHOTO);
	}

	private void startCamera(Intent intent, Uri uri, int aspectX, int aspectY, int outputX, int outputY, int requestCode) {
		if (intent == null) {
			throw new RuntimeException("intent is null");
		} else {
			if(isCrop){
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", aspectX);
				intent.putExtra("aspectY", aspectY);
				if (outputX > 0 && outputY > 0) {
					intent.putExtra("outputX", outputX);
					intent.putExtra("outputY", outputY);
				}
				intent.putExtra("scale", true);
				intent.putExtra("return-data", false);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				intent.putExtra("noFaceDetection", false); // no face detection
			}
			startActivityForResult(intent, requestCode);//SELECT_PICTURE_BY_PICK_PHOTO
		}
	}

	/**
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PICTURE_BY_TAKE_PHONE = 1;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("requestCode=" + requestCode + "\t resultCode=" + resultCode + "\t data=" + data);
		//LogCat.D(TAG, "------------->  buffer="+buffer.toString());
		//String temppath=getPath(this, data !=null ? data.getData() : Uri.parse("file:///sdcard/temp.png"));
		LogCat.e(TAG, "--------------->  data="+data+"       data.getData="+(data!=null ? data.getData() :null));
//			setWriteLog((u == null ? (buffer.toString()) : (buffer.append("\t uri=" + u.toString()).toString())));
		if (resultCode == RESULT_OK) {
			Uri u = null;
			if (data != null) u = data.getData();
			String path = getPath(this, u);
			LogCat.e(TAG, "--------------> 返回路径: path="+path);
			switch (requestCode) {
				case SELECT_PICTURE_BY_TAKE_PHONE://从拍照中选择要截图的对象
					//cropImageUri(imageUri, 150, 150, SELECT_PICTURE_BY_CORP_PHOTO);
					if(isCrop){
						Intent cameraIntent = new Intent("com.android.camera.action.CROP");
						cameraIntent.setDataAndType(imageUri, "image/*");
						startCamera(cameraIntent, imageUri, aspectX, aspectY, outputX, outputY, SELECT_PICTURE_BY_PICK_PHOTO);
					}else{
						if (lastIntent == null) lastIntent = new Intent();
						lastIntent.putExtra(KEY_PHOTO_PATH, imageUri.getPath());//返回的类容
						setResult(Activity.RESULT_OK, lastIntent);
						finish();
					}
					break;
				case SELECT_PICTURE_BY_PICK_PHOTO://从相册中选择要截图的对象
					if (lastIntent == null) lastIntent = new Intent();
					lastIntent.putExtra(KEY_PHOTO_PATH, isCrop ? imageUri.getPath() : path);
					setResult(Activity.RESULT_OK, lastIntent);//返回的类容
					finish();
					break;
//				case SELECT_PICTURE_BY_CORP_PHOTO://选择截图后返回的值
//					Log.e(TAG, "111 SELECT_PICTURE_BY_CORP_PHOTO       uri1==  " + data.getData());
//					if (lastIntent == null) lastIntent = new Intent();
//					lastIntent.putExtra(KEY_PHOTO_PATH, imageUri.getPath());//返回的类容
//					setResult(Activity.RESULT_OK, lastIntent);
//					finish();
//					break;
			}
		}else{
//			if (tempPhotoUri != null) {
//				int id = getContentResolver().delete(tempPhotoUri, null, null);
//				if (id > 0) tempPhotoUri = null;
//				LogCat.e(TAG, "删除的数量--------->" + id + "         imageurl=" + imageUri);
//			}
			finish();
		}
//				if (u != null && DocumentsContract.isDocumentUri(getApplicationContext(), u)) {
//					String wholeID = DocumentsContract.getDocumentId(u);
//					String id = wholeID.split(":")[1];//split分隔符，分为两个数组
//					String[] column = {Images.Media.DATA};
//					String sel = Images.Media._ID + " = ?";
//					Cursor cursor = this.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, column,
//							sel, new String[]{id}, null);
//					int columnIndex = cursor.getColumnIndex(column[0]);
//					Uri uripath = null;
//					if (cursor.moveToFirst()) {
//						String path = cursor.getString(columnIndex);
//						uripath = Uri.fromFile(new File(path));
//					}
//					Intent cameraIntent = new Intent("com.android.camera.action.CROP");
//					cameraIntent.setDataAndType(uripath, "image/*");
//					startCamera(cameraIntent, imageUri, aspectX, aspectY, outputX, outputY, SELECT_PICTURE_BY_PICK_PHOTO);
//					return;
//				}
	}

	/**
	 * 使用相册中的图片
	 */
	public static final int SELECT_PICTURE_BY_PICK_PHOTO = 2;

	/*
	 * 使用相册中的截图
	 */
//	public static final int SELECT_PICTURE_BY_CORP_PHOTO = 3;

	/**
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";

	private Uri imageUri;//拍照后的照片的统一资源标识符
	//private Uri tempPhotoUri;//拍照临时图片唯一地址，不管用户是否截图返回时都删除
	private Intent lastIntent;

	public void setWriteLog(String msg) {
		File f = getLogFile();
		InputStream is = null;
		OutputStream os = null;
		StringBuffer buffer = new StringBuffer();
		byte[] temp = new byte[10 * 1024];
		int index = -1;
		try {
			is = new FileInputStream(f);
			while ((index = is.read(temp)) != -1) {
				buffer.append(new String(temp, 0, index));
			}
			Log.e(TAG, "       buffer=" + buffer.toString());
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss E");
			String str = sdf.format(date);
			buffer.append(str + "：\n");
			os = new FileOutputStream(f);
			os.write(buffer.toString().getBytes());
			os.write(msg.getBytes());
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
				if (os != null) {
					os.close();
					os = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//return os; 
	}

	public File getLogFile() {
		String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
		/*if(!CheckUtil.getInstance().isEmploy(cacheDir))
			cacheDir=ApplicationContext.getInstance().getCacheDir().getAbsolutePath().toString();*/
		File f = new File(cacheDir + "/Log.txt");
		try {
			if (!f.exists()) f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}


	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		if(uri == null )return null;
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
}

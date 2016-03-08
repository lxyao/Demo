package com.yao.testdemo.imageselected;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

import com.yao.testdemo.R;
import com.yao.testdemo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片多选界面
 * @author Yao
 */
public class ImageSelectedActivity extends Activity {

	private static final String TAG="ImageSelectedActivity";
	
	private GridView mGridView;
	private int pageTotal=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_image_activity_layout);
		mGridView = (GridView) findViewById(R.id.pick_image_activity_gv);
		//mGridView.setOnScrollListener(mOnScrollListener);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Media.EXTERNAL_CONTENT_URI));
		Cursor c=getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		if(c!=null){
			int count=c.getCount();
			pageTotal=count%60==0?(count/60):(count/60+1);
			c.close();
		}else return;
		queryImg(id);
	}
	
	private void queryImg(final int id){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e(TAG, "===========>   id="+id+"        pageTotal="+pageTotal);
				if(id <= pageTotal){
					Cursor c=getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null , "date_added desc limit "+(60*(id-1))+",60");
					if(c!=null){
						List<PickImgInfo> data=new ArrayList<PickImgInfo>();
						while(c.moveToNext()){
							PickImgInfo pi=new PickImgInfo(c.getString(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("_data")), c.getString(c.getColumnIndex("_display_name")), c.getInt(c.getColumnIndex("_size")));
							data.add(pi);
						}
						c.close();
						handler.sendMessage(handler.obtainMessage(1, data));
					}
				}else handler.sendMessage(handler.obtainMessage(0, "没有查询到数据"));
			}
		}).start();
	}
	
	int id=1;
	private OnScrollListener mOnScrollListener=new OnScrollListener() {

		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			if(arg1!=0&&(arg1+arg2==arg3)&&id<=pageTotal){
				queryImg(id);
				++id;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
		}
	};
	
	SelectImgAdapter adapter=null;
	List<PickImgInfo> mData=null;
	private Handler handler=new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				List<PickImgInfo> pis=(List<PickImgInfo>) msg.obj;
				if(pis!=null&&pis.size()>0){
					if(mData!=null){
						mData.addAll(pis);
						adapter.notifyDataSetChanged();
					}else {
						mData=pis;
						adapter=new SelectImgAdapter(ImageSelectedActivity.this, mGridView, pis);
						mGridView.setAdapter(adapter);
					}
				}
				break;
			case 0:
				ToastUtil.systemToast(getApplicationContext(), (String) msg.obj);
				break;
			}
			return true;
		}
	});
}

package com.yao.testdemo.update_pulltorefreshview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.testdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改开源刷新控件library为自己想要的样式测试
 * <ol>
 *     <li>修改成自己需要的头部和底部</li>
 *     <li>滚动到底部自动加载数据</li>
 * </ol>
 * @author Yao
 */
public class TestUpdatePullToRefreshViewActivity extends Activity {

	private static final String TAG="TestUpdatePullToRefreshViewActivity";
	boolean isLoading = false;
	List<String> data=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_update_pulltorefreshview);
		final PullToRefreshListView listview=(PullToRefreshListView) findViewById(R.id.test_update_pulltorefresh_view_id);
		
		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				
				new Thread(){
					@Override
					public void run() {
						super.run();
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								listview.onRefreshComplete();
							}
						});
					}
					
				}.start();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				
			}
		});
		final String[] temp=getResources().getStringArray(R.array.emoji);
		for (int i = 0; i < temp.length; i++) {
			data.add(temp[i]);
		}

		//实现底部自动加载的代码
		listview.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(TestUpdatePullToRefreshViewActivity.this, "最后一条", Toast.LENGTH_SHORT).show();
				listview.setShowFooterLoadingView(true);
				if(!isLoading){
					isLoading = true;
					listview.postDelayed(new Runnable() {
					
						@Override
						public void run() {
							isLoading = false;
							listview.setShowFooterLoadingView(false);
							//listview.onRefreshComplete();
							data.add("底部"+listview.getRefreshableView().getLastVisiblePosition());
						}
					}, 3000);
				}
				
			}
		});
		listview.setAdapter(new MyAdapter());
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = null;
			if(convertView == null){
				tv = new TextView(getApplicationContext());
				tv.setPadding(0, 10, 0, 10);
				convertView = tv;
				convertView.setTag(tv);
			}else{
				tv = (TextView) convertView.getTag();
			}
			tv.setText(data.get(position));
			return convertView;
		}
		
	}
}

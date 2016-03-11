package com.yao.testdemo.copyelememenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.testdemo.R;
import com.yao.testdemo.util.LogCat;

import java.util.ArrayList;
import java.util.List;

/**
 * 模仿--饿了么--菜单界面效果 左右联动效果（基本实现）
 * <ol>
 *     <li>右边菜单滚动顶部固定该类名称</li>
 *     <li>滚动到隐藏的item自动显示</li>
 *     <li>点击显示不全的item也会自动显示</li>
 * </ol>
 * @author Yao
 */
public class ElemeMenuActivity extends Activity implements SectionIndexer {

	private static final String TAG = "MainActivity";

	private ListView menuListView;
	private ListView sideMenuListView;
	private MenuItemAdapter adapter;
	private SideMenuAdapter sideAdapter;

	private LinearLayout titleLayout;
	private TextView title;
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	private List<MenuItemBean> menuList;
	/**
	 * 上次选中的左侧菜单
	 */
	private View lastView;
	private ScrollView mScrollView;

	/**左边菜单在屏幕上的起始位置*/
	private int mLeftMenuTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eleme_menu_list);
		initViews();
	}

	private void initViews() {
		mScrollView = (ScrollView) findViewById(R.id.mscrollview);
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		title = (TextView) this.findViewById(R.id.title_layout_catalog);
		menuListView = (ListView) findViewById(R.id.menu_lvmenu);
		// 右侧点击事件
		menuListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Toast.makeText(
						getApplicationContext(),
						((MenuItemBean) adapter.getItem(position))
								.getMenuName(), Toast.LENGTH_SHORT).show();
			}
		});
		sideMenuListView = (ListView) findViewById(R.id.side_menu_lv);
		// 左侧点击事件
		sideMenuListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (lastView != null) {
					// 上次选中的view变回灰色
					lastView.setBackgroundColor(getResources().getColor(
							R.color.eleme_unfocused));
				}
				// 设置选中颜色为白色
				view.setBackgroundColor(getResources().getColor(R.color.eleme_white));
				TextView section_tv = (TextView) view
						.findViewById(R.id.section_tv);
				int location = adapter.getPositionForSection((Integer
						.parseInt(section_tv.getText().toString())));
				if (location != -1) {
					menuListView.setSelection(location);
				}
				lastView = view;
			}
		});
		binddata();
	}

	private void binddata() {
		try {
			menuList = getListMenu();
			adapter = new MenuItemAdapter(getApplicationContext(), menuList);
			sideAdapter = new SideMenuAdapter(getApplicationContext(), menuList);
			menuListView.setAdapter(adapter);
			sideMenuListView.setAdapter(sideAdapter);
			menuListView.setOnScrollListener(mOnScrollListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MenuItemBean> getListMenu() {
		List<MenuItemBean> mList = new ArrayList<MenuItemBean>();
		String menuTag = "";
		String menuName = "";
		for (int i = 0; i < 15; i++) {
			menuTag = "很牛逼的样子" + i;// 菜品分类
			for (int j = 0; j < i; j++) {
				menuName = "小傻逼" + (i + j);// 菜名
				mList.add(new MenuItemBean(menuName, menuTag, i));
			}
		}
		return mList;
	}

	/**
	 * 右侧菜品滚动
	 */
	private OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		/** 记录上次左边选中的item位置 */
		private int mLastSelection = 0;
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
							 int visibleItemCount, int totalItemCount) {
			int section = getSectionForPosition(firstVisibleItem);// 获取屏幕第一个item的section
			if (firstVisibleItem != lastFirstVisibleItem) {
				MarginLayoutParams params = (MarginLayoutParams) titleLayout
						.getLayoutParams();
				params.topMargin = 0;
				titleLayout.setLayoutParams(params);
				title.setText(menuList.get(getPositionForSection(section))
						.getMenuTag());
				if (lastView != null) {
					lastView.setBackgroundColor(getResources().getColor(R.color.eleme_unfocused));
				}
				lastView = sideMenuListView.getChildAt(section - 1);
				lastView.setBackgroundColor(getResources().getColor(R.color.eleme_white));


//				lastView.getLocationOnScreen(point);
// 				lastView.getLocationInWindow(point);两个值一样，都包含状态栏

				if(mLastSelection != section){//判断当前显示位置和上次记录的是否一致
					LogCat.d(TAG, "---------------------> section="+section);
					if(firstVisibleItem == 0){//当前是否是第一条数据，是就测量左边菜单在屏幕上的起始位置
						int[] temp1 = new int[2];
						view.getLocationInWindow(temp1);//获得屏幕上的位置
						mLeftMenuTop = temp1[1];
					}
					mLastSelection = section;
					int[] point = new int[2];
					lastView.getLocationInWindow(point);//获取当前view在屏幕的位置
					int bottom = lastView.getBottom();
					int height = view.getHeight();
					int scroll = bottom - height;
					if (height < bottom) {//高度超出屏幕
						mScrollView.scrollTo(0, Math.max(scroll, mScrollView.getScrollY()));//使用最大值是防止选中项滚动到底部
					} else {
						if (point[1] - mLeftMenuTop > 0) {//左边菜单即将选中的item的位置是否超出了屏幕
//							int temp = (point[1] + lastView.getHeight());
//							if (temp > 0 && temp < height) {}
						} else {
							mScrollView.scrollTo(0, lastView.getTop());
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		}
	};

	public int getSectionForPosition(int position) {
		return menuList.get(position).getMenuSection();
	};

	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < menuList.size(); i++) {
			int section = menuList.get(i).getMenuSection();
			if (section==sectionIndex) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
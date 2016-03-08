package com.yao.testdemo.emoji;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.yao.testdemo.R;
import com.yao.testdemo.util.DentisyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 显示  发送  表情信息的控件（基本完善  没有对表情做缓存）
 * @author Yao
 */
public class EmojiActivity extends FragmentActivity {


	private static final String TAG="MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emoji_activity);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {

		}

		private ViewPager mViewPager;
		private EditText mETText;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			initEmoji();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,false);
			mViewPager=(ViewPager) rootView.findViewById(R.id.testdemo_vp);
			mETText=(EditText) rootView.findViewById(R.id.testdemo_id);
			mViewPager.setAdapter(new EmojiViewPagerAdapter(pageViews));
			initTextView("你自己看看   [aotm]  [bish]   [chij]");
			return rootView;
		}


		private void initTextView(String str){
			SpannableString ssb=new SpannableString(str);
			String zhengze="\\[[^\\]]+\\]";
			Pattern p=Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
			Matcher matcher=p.matcher(ssb);
			while(matcher.find()){
				String key=matcher.group();
				int drawableId=0;
				for(EmojiInfo ei:emoji){
					if(key.equals(ei.emojiValueOne)||key.equals(ei.emojiValueTwo)){
						drawableId=ei.emojiId;
						break;
					}
				}
				if(drawableId>0){
					ssb.setSpan(getImageSpan(drawableId), matcher.start(), (matcher.start()+key.length()), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
			mETText.setText(ssb);
		}

		private ImageSpan getImageSpan(int drawableId){
			Drawable d=getResources().getDrawable(drawableId);
			int number=Math.max(d.getIntrinsicWidth(), d.getIntrinsicHeight())>>1;
			number= DentisyUtil.pix2dp(getActivity(), number - 5);
			Log.e(TAG, "==============>  number="+number);
			d.setBounds(0, 0, number,number);
			ImageSpan is=new ImageSpan(d);
			return is;
		}

		List<EmojiInfo> emoji=null;//所有数据
		List<List<EmojiInfo>> pageData=null;//分页数据
		List<View> pageViews=null;
		List<EmojiGridAdapter> mEmojiAdapter=null;

		private void initEmoji(){
			String[] emojis=getResources().getStringArray(R.array.emoji);
			int emojiSize=emojis.length;
			Log.d(TAG, "=================>   emoji.length="+emojiSize+"   str="+emojis[0]);
			if(emojiSize > 0){
				emoji = new ArrayList<EmojiInfo>();
				pageData = new ArrayList<List<EmojiInfo>>();
				for(int i=0;i<emojiSize;i++){
					String[] str=emojis[i].split(",");
					int resId=getResources().getIdentifier(str[0], "mipmap", getActivity().getPackageName());
					EmojiInfo ei=new EmojiInfo(resId,str[1],str[2]);
					emoji.add(ei);
				}
				int pageCount = emojiSize % 28 == 0 ? emojiSize/28 : (emojiSize/28)+1;
				for(int i=0;i<pageCount;i++){
					pageData.add(getSubList(i));
				}
				initGridView(pageCount);
				Log.d(TAG, "=============>  pagedata.size="+pageData.size()+"     ");
			}
		}

		private List<EmojiInfo> getSubList(int page){
			List<EmojiInfo> mList=new ArrayList<EmojiInfo>();
			int startIndex=page*28;
			int endIndex=startIndex+28;
			if (endIndex > emoji.size()) {
				endIndex = emoji.size();
			}
			mList.addAll(emoji.subList(startIndex, endIndex));
			return mList;
		}


		private void initGridView(int pageCount){
			pageViews=new ArrayList<View>();
			mEmojiAdapter=new ArrayList<EmojiGridAdapter>();
			for(int i=0;i<pageCount;i++){
				GridView gv=new GridView(getActivity());
				gv.setNumColumns(7);
				gv.setBackgroundColor(Color.TRANSPARENT);
				int size5=DentisyUtil.pix2dp(getActivity(), 5);
				gv.setHorizontalSpacing(size5);
				int size20=DentisyUtil.pix2dp(getActivity(), 20);
				gv.setVerticalSpacing(size20);
				gv.setVerticalScrollBarEnabled(false);
				gv.setHorizontalScrollBarEnabled(false);
				gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
				gv.setCacheColorHint(Color.TRANSPARENT);
				gv.setFadingEdgeLength(0);
				gv.setScrollingCacheEnabled(false);
				int size15=DentisyUtil.pix2dp(getActivity(), 15);
				gv.setPadding(size5, size15, size5, size15);
				gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
				gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				gv.setGravity(Gravity.CENTER);
				pageViews.add(gv);
				gv.setOnItemClickListener(mOnItemClickListener);
				EmojiGridAdapter ega=new EmojiGridAdapter(getActivity(), pageData.get(i));
				gv.setAdapter(ega);
				mEmojiAdapter.add(ega);
			}
		}

		private OnItemClickListener mOnItemClickListener=new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				EmojiInfo ei=(EmojiInfo) parent.getAdapter().getItem(position);
				int startId=mETText.getSelectionStart();
				int drawableId=ei.emojiId;
				if(drawableId>0){
					String value=ei.emojiValueOne;
					SpannableStringBuilder ssb=new SpannableStringBuilder(value);
					ssb.setSpan(getImageSpan(drawableId), 0, value.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					mETText.getText().insert(startId, ssb);
				}
			}
		};

	}
}
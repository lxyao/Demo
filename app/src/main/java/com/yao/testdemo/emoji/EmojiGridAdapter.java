
package com.yao.testdemo.emoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.yao.testdemo.util.DentisyUtil;

import java.util.List;

/**
 * �������������
 * 
 * @author Yao
 */
public class EmojiGridAdapter extends BaseAdapter {

	private static final String TAG="EmojiGridAdapter";
	
	private List<EmojiInfo> mData=null;
	private final Context mContext;
	
	public EmojiGridAdapter(Context context,List<EmojiInfo> data){
		this.mContext=context;
		this.mData=data;
	}
	
	@Override
	public int getCount() {
		return mData==null?0:mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView mIV;
		if(convertView==null){
			mIV=new ImageView(mContext);
			int size= DentisyUtil.pix2dp(mContext, 30);
			mIV.setLayoutParams(new AbsListView.LayoutParams(size, size));
			mIV.setScaleType(ScaleType.FIT_XY);
			convertView=mIV;
			convertView.setTag(mIV);
		}else mIV=(ImageView) convertView.getTag();
		EmojiInfo ei=mData.get(position);
		mIV.setImageResource(ei.emojiId);
		return convertView;
	}
	
}

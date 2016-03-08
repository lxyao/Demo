package com.yao.testdemo.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yao.testdemo.R;

import java.util.List;

/**
 * 发送消息  数据适配器
 * @author Yao
 */
public class SendMessageAdapter extends BaseAdapter {

	private static final String TAG="SendMessageAdapter";

	private final Context mContext;
	private List<String> mData;
	public SendMessageAdapter(Context context, List<String> data){
		this.mContext = context;
		this.mData = data;
	}
	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
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
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if(type == 0){
			if(convertView == null || !(convertView.getTag() instanceof LeftViewHolder)){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.send_message_item_left_layout, parent, false);
				holder = new LeftViewHolder();
				holder.mHead = (SpecialImageView) convertView.findViewById(R.id.send_message_item_left_riv_head);
				holder.mTVContent = (TextView) convertView.findViewById(R.id.send_message_item_left_tv_content);
				holder.mTVTime = (TextView) convertView.findViewById(R.id.send_message_item_left_tv_time);
				convertView.setTag(holder);
			}else holder = (LeftViewHolder) convertView.getTag();
		}else if(type == 1){
			if(convertView == null || !(convertView.getTag() instanceof RightViewHolder)){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.send_message_item_right_layout, parent, false);
				holder = new RightViewHolder();
				holder.mHead = (SpecialImageView) convertView.findViewById(R.id.send_message_item_right_riv_head);
				holder.mTVContent = (TextView) convertView.findViewById(R.id.send_message_item_right_tv_content);
				holder.mTVTime = (TextView) convertView.findViewById(R.id.send_message_item_right_tv_time);
				convertView.setTag(holder);
			}else holder = (RightViewHolder) convertView.getTag();
		}
		if(position != 0)holder.mTVTime.setVisibility(View.GONE);
		else holder.mTVTime.setVisibility(View.VISIBLE);

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return mData.get(position).equals("1") ? 0 : 1;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean hasStableIds() {
		super.hasStableIds();
		return true;
	}



	private class ViewHolder{
		private TextView mTVTime, mTVContent;
		private SpecialImageView mHead;
	}

	private final class LeftViewHolder extends ViewHolder{
	}

	private final class RightViewHolder extends ViewHolder{
	}
}
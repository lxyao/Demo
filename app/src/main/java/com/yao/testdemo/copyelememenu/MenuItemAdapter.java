package com.yao.testdemo.copyelememenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yao.testdemo.R;

import java.util.List;
import java.util.Map;

public class MenuItemAdapter extends BaseAdapter implements SectionIndexer {
	private List<MenuItemBean> list = null;
	private Context mContext;
	public static Map<String, Integer> maps;

	public MenuItemAdapter(Context mContext, List<MenuItemBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<MenuItemBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		final MenuItemBean mContent = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.eleme_menu_item, parent, false);
			viewHolder.tvTag = (TextView) convertView
					.findViewById(R.id.catalog);
			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			viewHolder.tvTag.setVisibility(View.VISIBLE);
			viewHolder.tvTag.setText(mContent.getMenuTag());
		} else {
			viewHolder.tvTag.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(mContent.getMenuName());
		return convertView;
	}

	final static class ViewHolder {
		TextView tvTag;
		TextView tvTitle;
	}

	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getMenuSection();
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			int section = list.get(i).getMenuSection();
			if (section == sectionIndex) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		// TODO �Զ���ɵķ������
		return null;
	}
}

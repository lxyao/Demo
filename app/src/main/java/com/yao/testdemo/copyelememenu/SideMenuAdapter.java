package com.yao.testdemo.copyelememenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yao.testdemo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class SideMenuAdapter extends BaseAdapter {
	private List<MenuTagSection> list = null;
	private Context mContext;	
	
	public SideMenuAdapter(Context mContext,List<MenuItemBean> list) {
		this.mContext = mContext;
		this.list = getTagSections(list);		
	}
	public void updateListView(List<MenuItemBean> list){
		this.list = getTagSections(list);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {		
		return this.list.size();
	}
	@Override
	public Object getItem(int position) {		
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {		
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		ViewHolder viewHolder=null;
		final MenuTagSection mContent=list.get(position);
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.eleme_side_menu_item,parent,false);
			viewHolder.tvTag=(TextView)convertView.findViewById(R.id.menu_tag_tv);
			viewHolder.tvSection=(TextView)convertView.findViewById(R.id.section_tv);
			convertView.setTag(viewHolder);
			
		}else {
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.tvSection.setText(String.valueOf(mContent.getMenuSection()));
		viewHolder.tvTag.setText(mContent.getMenuTag());
		return convertView;
	}
	final static class ViewHolder{
		TextView tvTag;
		TextView tvSection;
	}
	
	
	final static class MenuTagSection{
		String menuTag;
		int menuSection;
		public String getMenuTag(){
			return menuTag;
		}
		public int getMenuSection(){
			return menuSection;
		}
		public MenuTagSection(String menuTag,int menuSection) {
			this.menuTag=menuTag;
			this.menuSection=menuSection;
		}
		@Override
		public boolean equals(Object o) {
			MenuTagSection mts=(MenuTagSection)o;
			return menuTag.equals(mts.menuTag)&&(menuSection==mts.menuSection);			
		}
		@Override
		public int hashCode() {
			String in=menuSection+menuTag;
			return in.hashCode();
		}
	}
	
	private List<MenuTagSection> getTagSections(List<MenuItemBean> list){
		List<MenuTagSection> mtsList=new ArrayList<MenuTagSection>();
		for (int i = 0; i < list.size(); i++) {
			if (!mtsList.contains(new MenuTagSection(list.get(i).getMenuTag(), list.get(i).getMenuSection()))) {
				mtsList.add(new MenuTagSection(list.get(i).getMenuTag(), list.get(i).getMenuSection()));
			}			
		}
		//ȥ���ظ�
		Set<MenuTagSection> ts=new HashSet<MenuTagSection>();
		List<MenuTagSection> newMtsList=new ArrayList<MenuTagSection>();
		for(Iterator<MenuTagSection> iter=mtsList.iterator();iter.hasNext();){
			MenuTagSection ele=iter.next();
			if (ts.add(ele)) {
				newMtsList.add(ele);
			}
		}		
		return newMtsList;
	}
}


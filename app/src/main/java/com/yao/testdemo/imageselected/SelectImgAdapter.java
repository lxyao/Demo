package com.yao.testdemo.imageselected;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.yao.testdemo.R;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * �̰�Ȧ  ͼƬ ѡ��������
 * @author Yao
 */
public class SelectImgAdapter extends BaseAdapter implements OnCheckedChangeListener {

	private static final String TAG="SelectImgAdapter";
	
	private final Context mContext;
	private List<PickImgInfo> mData=null;
//	private AsyncImageLoader loader = null;
	private GridView mGridView;
	
	public SelectImgAdapter(Context context, GridView grid,List<PickImgInfo> data){
		this.mContext=context;
		this.mData=data;
		this.mGridView = grid;
//		loader = new AsyncImageLoader(context, R.drawable.ic_launcher);
//		mGridView.setOnScrollListener(new PauseOnScrollListener(loader.getImageLoader(), true, true));
	}
	
	List<PickImgInfo> mSelected;
	public List<PickImgInfo> getSelected(){
		return mSelected;
	}
	
	@Override
	public int getCount() {
		return mData==null?0:mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder=null;
		if(arg1==null){
			arg1=LayoutInflater.from(mContext).inflate(R.layout.pick_image_item_layout, arg2, false);
			holder=new ViewHolder();
			holder.mCheck=(CheckBox) arg1.findViewById(R.id.pick_image_item_cb);
			holder.mImg=(ImageView) arg1.findViewById(R.id.pick_image_item_iv);
			holder.mCheck.setOnCheckedChangeListener(this);
			arg1.setTag(holder);
		}else holder=(ViewHolder) arg1.getTag();
			PickImgInfo pi=mData.get(arg0);
			Log.e(TAG, "====================>.  arg0="+arg0+"      path="+pi.path);
			holder.mCheck.setTag(pi);
//			loader.load("file://" + pi.path, holder.mImg);//LoadImage.setImageView3(mContext, pi.path, holder.mImg, 150, 150);
			holder.mCheck.setChecked(pi.isCheck);
		return arg1;
	}

	private final class ViewHolder{
		private ImageView mImg;
		private CheckBox mCheck;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		PickImgInfo pi=(PickImgInfo)arg0.getTag();
		Log.e(TAG, "==========>>   id="+pi.id);
		pi.isCheck=arg1;
		if(arg1){
			//LogCat.E(TAG, "================>  pi.path="+pi.path);
			if(mSelected!=null){
				if(mSelected.size()>8){
					arg0.toggle();
					pi.isCheck=false;
					Toast.makeText(mContext, "发布图片数量过多", Toast.LENGTH_SHORT).show();
					return;
				}
				Iterator<PickImgInfo> ites=mSelected.iterator();
				while(ites.hasNext()){
					PickImgInfo temp=ites.next();
					if(temp.id.equals(pi.id)){
						ites.remove();
						break;
					}
				}
				mSelected.add(pi);
			}else{
				mSelected=new LinkedList<PickImgInfo>();
				mSelected.add(pi);
			}
		}else{
			if(mSelected!=null){
				Iterator<PickImgInfo> ites=mSelected.iterator();
				while(ites.hasNext()){
					PickImgInfo temp=ites.next();
					if(temp.id.equals(pi.id)){
						ites.remove();
						break;
					}
				}
			}
		}
		//notifyDataSetChanged();
	}
}

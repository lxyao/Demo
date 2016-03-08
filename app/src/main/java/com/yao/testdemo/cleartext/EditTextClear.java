package com.yao.testdemo.cleartext;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yao.testdemo.R;

/**
 * ��������EditText�ؼ�
 * @author Yao
 */
public class EditTextClear extends FrameLayout {//implements TextWatcher

	private static final String TAG=EditTextClear.class.getSimpleName();
	private Context mContext;
	
	public EditTextClear(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		//addTextChangedListener(this);
		init(attrs);
	}

	public EditTextClear(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init(attrs);
	}
	
	public EditTextClear(Context context) {
		this(context,null);
	}
	
	//Drawable mDelete;
	Bitmap mDelete;
	private ImageView mIVDelete;
	private EditText mEdit;
	private void init(AttributeSet attrs){
		View v=LayoutInflater.from(mContext).inflate(R.layout.edittext_clear_layout, null);
		mEdit=(EditText) v.findViewById(R.id.edittext_clear_et);
		mIVDelete=(ImageView) v.findViewById(R.id.edittext_clear_iv);
	}
	
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//		
//	}
//
//	@Override
//	public void afterTextChanged(Editable s) {
//		
//	}
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		
//	}
}

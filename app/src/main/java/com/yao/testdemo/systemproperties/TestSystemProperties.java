package com.yao.testdemo.systemproperties;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.yao.testdemo.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试系统属性(buildutil功能基本完善)
 * @author Yao
 */
public class TestSystemProperties extends Activity {

	private static final String TAG="TestSystemProperties";

	private TextView mTVSystemProperties,mTVTest;

	private String[] fields;
	Field[] f = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_system_properties_layout);
		mTVSystemProperties = (TextView) findViewById(R.id.test_system_properties_id);
		mTVTest = (TextView) findViewById(R.id.test_system_properties_id_tv);
		try {
			Class<?> size = Class.forName("android.os.Build");
			f = size.getDeclaredFields();
			fields = new String[f.length];
			StringBuffer sb = new StringBuffer();
			for(int i=0; i < f.length; i++){
				fields[i] = f[i].getName();
				try {
					sb.append(fields[i] +"------"+f[i].get(fields[i])+ "\n\n");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch ( IllegalArgumentException e){
					e.printStackTrace();
				}
			}
			f = null;
			Class<?> two = Class.forName("android.os.Build$VERSION");
			f = two.getDeclaredFields();
			fields = new String[f.length];
			for(int i=0; i < f.length; i++){
				fields[i] = f[i].getName();
				try {
					sb.append(fields[i] +"------"+f[i].get(fields[i])+ (i == (f.length-1) ? "" : "\n\n"));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch ( IllegalArgumentException e){
					e.printStackTrace();
				}
			}

			mTVSystemProperties.setText(sb.toString());

			Class<?> three = Class.forName("com.yy.systemproperties.BuildUtil");
			Method[] methods = three.getDeclaredMethods();
			StringBuffer sb1 = new StringBuffer();
			sb1.append("buildutil test\n\n\n\n");
			for(int i=0; i<methods.length; i++){
				sb1.append(methods[i].getName()+"-------------"+methods[i].invoke(three, new Object[]{})+(i == (methods.length-1) ? "" : "\n\n"));
			}
			mTVTest.setText(sb1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
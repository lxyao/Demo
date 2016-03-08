package com.yao.testdemo.chat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.yao.testdemo.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 布局类型
 * @author Yao
 */
public class TestChat extends Activity {

	private static final String TAG ="TestChat";

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_chat_layout);

		mListView = (ListView) findViewById(R.id.test_chat_list);
		List<String> data = new ArrayList<String>();
		for(int i=1; i<= 10; i++){
			if(i%2 == 0)
				for(int j=1;j<= i;j++)data.add("2");
			data.add("1");
		}
		mListView.setAdapter(new SendMessageAdapter(this, data));
	}

}
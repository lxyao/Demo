package com.yao.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yao.testdemo.util.LogCat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> data = new ArrayList<String>();
        data.add("time.TimeTest");
        data.add("toast.ToastTest");
        data.add("update_pulltorefreshview.TestUpdatePullToRefreshViewActivity");
        data.add("cleartext.TestClearActivity");
        data.add("webview.TestAssetsWebView");
        data.add("systemproperties.TestSystemProperties");
        data.add("systemcrop.TestSystemCrop");
        data.add("imagespan.TestImageSpan");
        data.add("emoji.EmojiActivity");
        data.add("testhead.ZambiaActivity");
        data.add("translateanimation.TranslateAnimation");
        data.add("autoscrollviewpager.TestScrollViewPager");
        data.add("chat.TestChat");
        data.add("ximage.TestShowGif");
        ListView listView = (ListView)findViewById(R.id.my_test_demo_id);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) adapterView.getAdapter().getItem(i);
                LogCat.d(TAG, "-------------------------->  点击界面  =" + id);
                try {
                    Class cls = Class.forName(getPackageName() +"."+ id);
                    startActivity(new Intent(getBaseContext(), cls));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

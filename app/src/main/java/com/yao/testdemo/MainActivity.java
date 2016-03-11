package com.yao.testdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yao.testdemo.systemcrop.TestSystemCrop;
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
        data.add("copyelememenu.ElemeMenuActivity");
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
                    Class cls = Class.forName(getPackageName() + "." + id);
                    startActivity(new Intent(getBaseContext(), cls));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        //setShortcutIntent();
        //setLongShortcutIntent();
    }

    public void setShortcutIntent(){
        Intent luancherIntent = new Intent(this, MainActivity.class);
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");// 创建快捷方式系统提供的action 需要添加权限<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
        intent.putExtra("duplicate", false);//不允许重复创建
        String title = getResources().getString(R.string.app_name);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, luancherIntent);
        sendBroadcast(intent);//使用sendBroadcast把广播发送到创建快捷方式的系统处理事件中
    }

    public void setLongShortcutIntent(){
        LogCat.d(TAG, "-------------------->  setLongShortcutIntent");
        Intent longShortcut = getIntent();
        if(longShortcut.getAction().equals(Intent.ACTION_CREATE_SHORTCUT)){
            LogCat.d(TAG, "-------------------->  setLongShortcutIntent  ACTION_CREATE_SHORTCUT ");
            Intent intent = new Intent(this, TestSystemCrop.class);
            Intent temp = new Intent();
            String title = getResources().getString(R.string.app_name);
            Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
            temp.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            temp.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
            temp.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            setResult(RESULT_OK, temp);
        }
    }
}

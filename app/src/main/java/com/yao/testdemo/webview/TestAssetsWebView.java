package com.yao.testdemo.webview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

import com.yao.testdemo.R;

/**
 * webview使用b strong标签不能加粗文字  可以使用头标签（h1-h7）
 *
 * webview设置背景透明setBackgroundColor(Color.TRANSPARENT);也是不可用  可以在body标签里使用bgcolor设置和背景一样的颜色
 * @author Yao
 */
public class TestAssetsWebView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_assets_html_layout);
		WebView view = (WebView) findViewById(R.id.test_assets_html);
		view.getSettings().setJavaScriptEnabled(true);
		view.setBackgroundColor(Color.TRANSPARENT);
		//view.loadUrl("http://7demo.github.io/");
		//view.loadUrl("file:///android_asset/html/online_explain.html");
		view.loadUrl("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");//file:///android_asset/gif/60d1e88fa04d59a34e17e8de74da8abe.gif
	}
}

/**
 * 
 */
package com.tleaf.tiary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.tleaf.tiary.R;
import com.tleaf.tiary.core.ViewMapper;
import com.tleaf.tiary.util.ShackWebViewJsInterface;

/**
 * Created with Eclipse IDE
 * Author : RichardJ 
 * Date   : Nov 17, 2014 3:23:40 PM
 * Description : 
 */
public class ShackWebViewActivity extends Activity{
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.activity_shack_webview));
	    setWebView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}
	
    @SuppressLint("JavascriptInterface") 
    public void setWebView(){
        mWebView = (WebView) findViewById(R.id.shacklogin_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new ShackWebViewJsInterface(this), "TLeafLogin");
        mWebView.loadUrl("http://14.63.171.66:8081/tleafstructure/oauth?appId=41bfdd372e1d60c37baba4cdec003b0f");
        //mWebView.loadUrl("http://172.16.101.117:8080/tleaf/oauth?appId=2283c4e5d44f301f151eaf73c10354e6");
    }
}

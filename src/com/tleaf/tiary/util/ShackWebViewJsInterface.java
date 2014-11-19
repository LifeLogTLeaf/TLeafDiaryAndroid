/**
 * 
 */
package com.tleaf.tiary.util;

import com.google.gson.Gson;
import com.tleaf.tiary.core.AppContext;
import com.tleaf.tiary.network.TLeafSession;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created with Eclipse IDE
 * Author : RichardJ 
 * Date   : Nov 17, 2014 3:30:39 PM
 * Description : 
 */
public class ShackWebViewJsInterface {
	private static final String TAG = "JAVASCIRPT INTERFACE";
    Activity mActivity;

    public ShackWebViewJsInterface(Activity activity){
        mActivity = activity;
    }

    @JavascriptInterface
    public void callbackAndroid(final String str){
        Log.i(TAG, str);
        TLeafSession session = new Gson().fromJson(str, TLeafSession.class);
        AppContext.setTlSession(session);
        AppContext.getPreference().setBooleanPref("isLogin", true);
        mActivity.finish();
    }
}

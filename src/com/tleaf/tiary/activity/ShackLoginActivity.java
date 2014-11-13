package com.tleaf.tiary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tleaf.tiary.R;
import com.tleaf.tiary.core.AppContext;
import com.tleaf.tiary.core.DeclareView;
import com.tleaf.tiary.core.ViewMapper;
import com.tleaf.tiary.network.HttpMethod;
import com.tleaf.tiary.network.Request;
import com.tleaf.tiary.network.Response;
import com.tleaf.tiary.network.TLeafSession;
import com.tleaf.tiary.network.UserInfo;

/**
 * Created by jangyoungjin on 11/12/14.
 */
public class ShackLoginActivity extends Activity implements View.OnClickListener, Request.Callback{
    private static final String TAG = ShackLoginActivity.class.getSimpleName();

    @DeclareView(id = R.id.tleaflogin_btn_login, click = "this")
    TextView tleaflogin_btn_login;

    @DeclareView(id = R.id.tleaflogin_btn_cancel, click = "this")
    TextView tleaflogin_btn_cancel;

    @DeclareView(id = R.id.tleaflogin_etx_id)
    EditText tleaflogin_etx_id;

    @DeclareView(id = R.id.tleaflogin_etx_password)
    EditText tleaflogin_etx_password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.activity_shack_login));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tleaflogin_btn_login:
                if(checkInputFild()){
                    perfomLogin();
                }
                break;
            case R.id.tleaflogin_btn_cancel:
                finish();
                break;
            default:
                break;
        }

    }

    private boolean checkInputFild(){
        if(tleaflogin_etx_id.getText().toString().isEmpty() || tleaflogin_etx_password.getText().toString().isEmpty()){
        	popupAlertbox("모두 입력해주세요..");
            return false;
        }
        return true;
    }

    private void perfomLogin(){
        new Request("/user/login", HttpMethod.POST, this, new UserInfo(tleaflogin_etx_id.getText().toString(), tleaflogin_etx_password.getText().toString())).execute();
    }

    @Override
    public void onRecieve(Response response) {
        Log.i(TAG, response.getJsonStringData());
        if(response.getStatus() == 200 ){
            AppContext.setTlSession(new Gson().fromJson(response.getJsonStringData(), TLeafSession.class));
            AppContext.getPreference().setBooleanPref("isLogin", true);
            finish();
        }else {
        	popupAlertbox("아이디 또는 비번이 틀려요..");
        }
    }
    
    private void popupAlertbox(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ShackLoginActivity.this)
                        .setTitle(message)
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).show();
            }
        });
    }


}

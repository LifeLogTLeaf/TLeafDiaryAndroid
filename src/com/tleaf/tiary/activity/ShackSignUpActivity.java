package com.tleaf.tiary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tleaf.tiary.R;
import com.tleaf.tiary.core.DeclareView;
import com.tleaf.tiary.core.ViewMapper;
import com.tleaf.tiary.network.HttpMethod;
import com.tleaf.tiary.network.Request;
import com.tleaf.tiary.network.Response;
import com.tleaf.tiary.network.UserInfo;

/**
 * Created by jangyoungjin on 11/12/14.
 */
public class ShackSignUpActivity extends Activity implements View.OnClickListener, Request.Callback {
    private static final String TAG = ShackSignUpActivity.class.getSimpleName();

    @DeclareView(id = R.id.tleafsignup_btn_signup, click = "this")
    TextView tleafsignup_btn_signup;

    @DeclareView(id = R.id.tleafsignup_btn_cancel, click = "this")
    TextView tleafsignup_btn_cancel;

    @DeclareView(id = R.id.tleafsignup_etx_id)
    EditText tleafsignup_etx_id;

    @DeclareView(id = R.id.tleafsignup_etx_password)
    EditText tleafsignup_etx_password;

    @DeclareView(id = R.id.tleafsignup_etx_password_check)
    EditText tleafsignup_etx_password_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.activity_shack_signup));
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
            case R.id.tleafsignup_btn_signup:
                if(checkInputFild()){
                    perfomLogin();
                }
                break;
            case R.id.tleafsignup_btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean checkInputFild(){
        if(tleafsignup_etx_id.getText().toString().isEmpty() || tleafsignup_etx_password.getText().toString().isEmpty()){
        	popupAlertbox("모두 기입해주세요... ", false);
            return false;
        }
        return true;
    }

    private void perfomLogin(){
        new Request("/user/signup", HttpMethod.POST, this, new UserInfo(tleafsignup_etx_id.getText().toString(), tleafsignup_etx_password.getText().toString())).execute();
    }

    @Override
    public void onRecieve(Response response) {
        Log.i(TAG, response.getJsonStringData());
        if(response.getStatus() == 200 ){
        	popupAlertbox("회원가입이 성공했어요..", true);
        }else {
        	popupAlertbox("아이디가 중복되요...", false);
        }
    }
    
    private void popupAlertbox(final String message, final boolean success){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ShackSignUpActivity.this)
                        .setTitle(message)
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            	if(success) finish();
                            }
                        }).show();
            }
        });
    }
}

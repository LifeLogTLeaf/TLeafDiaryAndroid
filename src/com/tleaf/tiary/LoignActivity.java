package com.tleaf.tiary;

import com.tleaf.tiary.activity.ShackLoginActivity;
import com.tleaf.tiary.activity.ShackSignUpActivity;
import com.tleaf.tiary.core.AppContext;
import com.tleaf.tiary.core.DeclareView;
import com.tleaf.tiary.core.ViewMapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class LoignActivity extends Activity implements OnClickListener{
	
	@DeclareView( id = R.id.txt_singup, click = "this")
	TextView txt_singup;
	
	@DeclareView( id = R.id.txt_login, click = "this")
	TextView txt_login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(ViewMapper.inflateLayout(getBaseContext(), this, R.layout.activity_login));
	    
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation arg) {
				Handler handle = new Handler();
				handle.postDelayed(new Runnable() {
					@Override
					public void run() {
					}
				}, 300);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		animation.setDuration(500);
		findViewById(R.id.layout_splash).setAnimation(animation);
	}
	
	@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_singup: 
            	startActivity(new Intent(LoignActivity.this, ShackSignUpActivity.class));
                break;
            case R.id.txt_login:
            	startActivity(new Intent(LoignActivity.this, ShackLoginActivity.class));
                break;
            default:
                break;
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		if( AppContext.getTlSession() != null && AppContext.getPreference().getBooleanPref("isLogin")){
			startActivity(new Intent(LoignActivity.this, MainActivity.class));
			finish();
		}
	}
	
	

}

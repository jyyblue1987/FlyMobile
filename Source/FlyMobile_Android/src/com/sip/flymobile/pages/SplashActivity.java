package com.sip.flymobile.pages;

import com.sip.flymobile.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import common.library.utils.AlgorithmUtils;
import common.library.utils.CheckUtils;
import common.library.utils.DataUtils;
import common.manager.activity.ActivityManager;
import common.network.utils.LogicResult;
import common.network.utils.ResultCallBack;

public class SplashActivity extends BaseActivity {
	ImageView m_imgSplash = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityManager.getInstance().popAllActivity();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		
		loadComponents();		
	}
	
	protected void findViews()
	{
		m_imgSplash = (ImageView) findViewById(R.id.img_splash);
	}
	
	protected void initData()
	{
	
		startAlphaAnimation();
	}
	
	private void autoLogin()
	{

	}
	
	private void startAlphaAnimation()
	{
		AlphaAnimation face_in_out_anim = new AlphaAnimation(0.1f, 1.0f);
		face_in_out_anim.setDuration(1000);     
		face_in_out_anim.setRepeatMode(Animation.REVERSE);
		
		if (m_imgSplash != null){
			m_imgSplash.setAnimation(face_in_out_anim);
		}
		face_in_out_anim.start(); 		
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onFinishAnimation();
			}
		}, 1500);
	}
	
	private void onFinishAnimation()
	{
//		gotoMainPage();
	}
	
	private void gotoMainPage()
	{
		Bundle bundle = new Bundle();
//		ActivityManager.changeActivity(this, SignUpActivity.class, bundle, true, null );		
	}
}

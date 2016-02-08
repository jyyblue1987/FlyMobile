package com.sip.flymobile.pages;

import com.sip.flymobile.R;
import com.sip.flymobile.mvp.BaseView;
import com.sip.flymobile.mvp.BasePageDecorator;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import common.manager.activity.ActivityManager;


public class SplashPage extends BasePageDecorator {
	ImageView m_imgSplash = null;
	
	public SplashPage(BaseView view)
	{
		super(view);
	}
	public void findViews()
	{
		super.findViews();
		
		m_imgSplash = (ImageView) getContext().findViewById(R.id.img_splash);		
	}
		
	public void initData()
	{
		super.initData();
		
		startAlphaAnimation();
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
		gotoMainPage();
	}
	
	private void gotoMainPage()
	{
		Bundle bundle = new Bundle();
		ActivityManager.changeActivity(getContext(), MainActivity.class, bundle, true, null );		
	}
}

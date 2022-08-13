package com.maid.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.aplha);
        LottieAnimationView lview;
        lview = findViewById(R.id.lotte);
        lview.setAnimation(R.raw.lotte);
        lview.startAnimation(animation);
        lview.loop(true);
        lview.playAnimation();
        Log.d("main thread", Thread.currentThread().getName());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Diff thread", Thread.currentThread().getName());
                try {
                    Intent i = new Intent(splashActivity.this, MainActivity.class);
                    Thread.sleep(4000);
                    startActivity(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();


    }

}
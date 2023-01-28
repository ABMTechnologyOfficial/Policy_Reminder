package com.policyreminder.policyreminder.Activities;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private  SplashActivity activity ;
   private Session session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = this;
        session = new Session(activity);


        Thread thread  = new Thread(() -> {
            try {
                sleep(2000);
             if(session.sharedPreferences.contains("user_id")){
                 startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                 finish();
             }else{
                 startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                 finish();
             }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();



    }
}
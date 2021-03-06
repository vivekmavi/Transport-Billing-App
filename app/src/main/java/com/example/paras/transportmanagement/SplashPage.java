package com.example.paras.transportmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class SplashPage extends AppCompatActivity
{
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();

    }
    private void doWork()
    {
        for (int progress=0; progress<100; progress+=25) {
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void startApp()
    {
        Intent intent = new Intent(SplashPage.this, LoginActivity.class);
        startActivity(intent);
    }
}

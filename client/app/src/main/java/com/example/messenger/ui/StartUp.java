package com.example.messenger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;

public class StartUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Thực hiện chuyển đổi màn hình sau 3 giây
                Intent intent = new Intent(StartUp.this, StartUpNext.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}
package com.example.messenger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;

public class LookAvatar extends AppCompatActivity {
    private ImageView IMG_Avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_avatar);
        IMG_Avatar = findViewById(R.id.IMG_Avatar);
        IMG_Avatar.setImageBitmap(BitmapFactory.decodeByteArray(InfoMyAccount.getAvata(),0,InfoMyAccount.getAvata().length));
    }
}
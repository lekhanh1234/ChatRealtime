package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTFileMP4;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPreferencePost extends AppCompatActivity {
    private ListView LV_AllFileMP4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent a = getIntent();
        int type = a.getIntExtra("type",-1);
        setContentView(R.layout.activity_add_preference_post);
        LV_AllFileMP4 = findViewById(R.id.LV_AllFileMP4);
        ADTFileMP4 adtFileMP4 = new ADTFileMP4(AddPreferencePost.this,getVideoList(),type);
        LV_AllFileMP4.setAdapter(adtFileMP4);
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                 adtFileMP4.setMP4PathList(getVideoList());
                 adtFileMP4.notifyDataSetChanged();

            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                finish();
            }
        };
        TedPermission.create().setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


    }
    private List<String> getVideoList() {
        List<String> list = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
            cursor.close();
        }
        Log.e("all path",list.toString());
        return list;
    }
}
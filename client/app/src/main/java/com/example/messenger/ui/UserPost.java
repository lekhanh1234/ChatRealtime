package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.io.File;

public class UserPost extends AppCompatActivity {
    private String path;
    private int type;
    private int userId;

    public int getUserId() {
        return userId;
    }

    private String outputPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent a = getIntent();
        path = a.getStringExtra("postPath");
        Log.d("path la :", ":"+path);
        type = a.getIntExtra("type",-1);
        userId = a.getIntExtra("userId",-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // lấy ra type . neu 1 => từ profile. 2 là post 24h
        // lay user id
        setContentView(R.layout.activity_post_to_user_friend);
        VideoView videoView = (VideoView) findViewById(R.id.video);
        if(!CheckInternet.isNetworkAvailable(this)) return;
        // chay mot luong len server lay file ve va luu no vao trong file cua he thong de hien thi ra
        new OperationServer().savePostFromServer(path);
        outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/aipzusrygzpziyopaie2os.mp4";
        try {
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(outputPath);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       if(userId == InfoMyAccount.getAccountId())  getMenuInflater().inflate(R.menu.menu_operation_my_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_post){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Tin").setMessage("Bạn muốn xóa Post này")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // chạy một luồng lên server
                            if(CheckInternet.isNetworkAvailable(UserPost.this) == false){
                                Toast.makeText(UserPost.this,"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                new OperationServer().deletePost(path);
                                setResult(200);
                                finish();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
        else if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        new File(outputPath).delete();
        super.onDestroy();
    }
}
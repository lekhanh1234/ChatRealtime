package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

public class UserPictureDetail extends AppCompatActivity {
    public static byte[] picture;
    public static int pictureId;
    public static int userId;
    private ImageView IMG_UserPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_picture_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IMG_UserPicture = findViewById(R.id.IMG_UserPicture);
        IMG_UserPicture.setImageBitmap(BitmapFactory.decodeByteArray(picture,0,picture.length));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(userId == InfoMyAccount.getAccountId()) getMenuInflater().inflate(R.menu.menu_operation_mypicture, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_picture){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Image").setMessage("Bạn muốn xóa ảnh này")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // chạy một luồng lên server
                                    if(CheckInternet.isNetworkAvailable(UserPictureDetail.this) == false){
                                        Toast.makeText(UserPictureDetail.this,"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        new OperationServer().deletePicture(pictureId);
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
        picture = null;
        pictureId = 0;
        userId = 0;
        super.onDestroy();
    }
}
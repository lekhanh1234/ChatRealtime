package com.example.messenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.adapter.ADTAllGallaryPicture;
import com.example.messenger.operationGallary.OperationGallary;
import com.example.messenger.operationServer.CheckInternet;

import java.util.Base64;
import java.util.List;

public class PickPictureToSendMessage extends AppCompatActivity {

    private RecyclerView RCV_GallarePiture;
    private Button BT_sendPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity nay se chon mot buc anh va gui no len may chu
        setContentView(R.layout.activity_send_picture_to_users);
        RCV_GallarePiture = findViewById(R.id.RCV_GallarePiture);
        BT_sendPicture = findViewById(R.id.BT_sendPicture);
        List<String> list = new OperationGallary().getAllUrlPictureGellary(this);
        TextView TV_pictureFromGallary = findViewById(R.id.TV_pictureFromGallary);
        if(list.size() == 0)  TV_pictureFromGallary.setText("Ảnh không có sẵn");
        ADTAllGallaryPicture ADT = new ADTAllGallaryPicture(this,list);
        RCV_GallarePiture.setAdapter(ADT);
        RCV_GallarePiture.setLayoutManager(new GridLayoutManager(this,3));
        BT_sendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chung ta chac chan rang khi BT nay hien len thi luon co anh de gui ve
                if(!CheckInternet.isNetworkAvailable(PickPictureToSendMessage.this)) {
                    Toast.makeText(PickPictureToSendMessage.this,"Không có kết nối internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                imageRetrieval(ADT);
            }
        });


    }
    private void imageRetrieval(ADTAllGallaryPicture ADT){
        // lay ra cac hinh anh de gui ve cho activity truoc do
        List<byte[]> list = ADT.getPictureQueue();
        Log.d("so image", String.valueOf(list.size()));
        // chuyen cac hinh anh image view do ve dang byte -> string va tra ve cho activity truoc do
        List<String> listForUserChat = UserChat.getSelectGallaryPicture();
        for(byte[] x : list){
            String s = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                s = Base64.getEncoder().encodeToString(x);
            }
            else s = android.util.Base64.encodeToString(x, android.util.Base64.DEFAULT);
            listForUserChat.add(s);
        }
        setResult(200,new Intent());
        finish();
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
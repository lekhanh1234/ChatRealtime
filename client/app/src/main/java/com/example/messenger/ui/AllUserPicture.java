package com.example.messenger.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTUserPicture;
import com.example.messenger.model.UserPicture;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

import java.util.ArrayList;
import java.util.List;

public class AllUserPicture extends AppCompatActivity {
    private Button BTN_AddPicture;
    private RecyclerView RCV_AllUserPiture;
    private ADTUserPicture adtMyPicture;
    private int userId;

    public int getUserId() {
        return userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_picture);
        Intent a = getIntent();
        userId = a.getIntExtra("userId",-1);
        RCV_AllUserPiture = findViewById(R.id.RCV_AllUserPiture);
        List<UserPicture> resultFromServer = new OperationServer().getAllPictureByUserId(userId);
        adtMyPicture = new ADTUserPicture(this,(ArrayList<UserPicture>) resultFromServer,userId);
        RCV_AllUserPiture.setAdapter(adtMyPicture);
        RCV_AllUserPiture.setLayoutManager(new GridLayoutManager(this,3));
        BTN_AddPicture = findViewById(R.id.BTN_AddPicture);
        if(userId != InfoMyAccount.getAccountId()) BTN_AddPicture.setVisibility(View.GONE);
        BTN_AddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent a = new Intent(AllUserPicture.this, PickMyProfilePicture.class);
                  a.putExtra("userId",userId);
                  startActivityForResult(a,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if((requestCode == 1 || resultCode == 2) && resultCode == 200){
            if(!CheckInternet.isNetworkAvailable(this)) return;
            List<UserPicture> resultFromServer = new OperationServer().getAllPictureByUserId(userId);
            adtMyPicture.setListPicture((ArrayList<UserPicture>) resultFromServer);
            adtMyPicture.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
       if( new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),userId) == -1) finish();
        super.onResume();
    }
}
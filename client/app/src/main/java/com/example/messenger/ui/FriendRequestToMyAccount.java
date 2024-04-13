package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTRequestFriend;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.OperationServer;

import java.util.List;

public class FriendRequestToMyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_to_my_account);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lời mời kết bạn");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        ListView LV_RequestFriend = findViewById(R.id.LV_RequestFriend);
        List<GeneralUserInfomation> list = new OperationServer().getFriendRequest(InfoMyAccount.getAccountId(),0); // chi chay va lay thong tin mot lan trong khi hàm oncreate được chạy, 0 là tham số yêu cầu kết bạn được nhận
        ADTRequestFriend adtRequestFriend = new ADTRequestFriend(list,this);
        LV_RequestFriend.setAdapter(adtRequestFriend);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTRequestFriendSend;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.OperationServer;

import java.util.List;

public class FriendRequestSent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // vào activity này là internet đã kết nối
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_sent);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lời mời đã gửi");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
        List<GeneralUserInfomation> list = new OperationServer().getFriendRequest(InfoMyAccount.getAccountId(),1); // mã code bằng 1 là yêu cầu gửi đi để xem các lời mời đã gửi
        ListView LV_RequestFriendSend = findViewById(R.id.LV_RequestFriendSend);
        ADTRequestFriendSend adtRequestFriendSend = new ADTRequestFriendSend(list,this);
        LV_RequestFriendSend.setAdapter(adtRequestFriendSend);
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
package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTBlockUser;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.OperationServer;

import java.util.List;

public class BlockUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);
        ListView LV_BlockUser = findViewById(R.id.LV_BlockUser);
        // chạy một luồng lên server để lấy thông tin của user bị block
        List<GeneralUserInfomation> list = new OperationServer().getUserBlocked(InfoMyAccount.getAccountId());
        ADTBlockUser adtBlockUser = new ADTBlockUser(list,this);
        LV_BlockUser.setAdapter(adtBlockUser);
        getSupportActionBar().setTitle("Block User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
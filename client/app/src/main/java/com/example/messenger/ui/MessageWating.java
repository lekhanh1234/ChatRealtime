package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.messenger.adapter.ADTMessageWating;
import com.example.messenger.model.Messenger;

import android.view.MenuItem;
import android.widget.ListView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

import java.util.List;

public class MessageWating extends AppCompatActivity {
    private ADTMessageWating adtMessageWating;
    private ListView LV_MessageWating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_wating);
        LV_MessageWating = findViewById(R.id.LV_MessageWating);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Tin nhắn đang chờ");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        requestInternetToResult();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    public void requestInternetToResult(){
        if(!CheckInternet.isNetworkAvailable(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("").setMessage("Internet là không có sẵn ! kiểm tra Internet của bạn")
                    .setPositiveButton("đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            requestInternetToResult();
                        }
                    }).create().show();
        }
        else{
            List<Messenger> messengerList = new OperationServer().getMessengerWating(InfoMyAccount.getAccountId());
            adtMessageWating = new ADTMessageWating(messengerList,this);
            LV_MessageWating.setAdapter(adtMessageWating);
            adtMessageWating.notifyDataSetChanged();
        }
    }

}
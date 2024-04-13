package com.example.messenger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

public class ShowState extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_state);
        Switch switch1 = findViewById(R.id.switch1);
        // chay mot luong tu server ve de kiem tra state va luu no lai
        int result = new OperationServer().getShowStates(InfoMyAccount.getAccountId());
        switch1.setChecked(result == 1);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!CheckInternet.isNetworkAvailable(ShowState.this)){
                    Toast.makeText(ShowState.this,"Thao tac khong the thuc hien, kiem tra Internet cua ban",Toast.LENGTH_LONG).show();
                    switch1.setChecked(!isChecked);
                    return;
                }
                int state = 1;
                if(!isChecked) state = 0;
                // thuc hien thay doi tren server
                new OperationServer().changeShowStates(InfoMyAccount.getAccountId(),state);
                Toast.makeText(ShowState.this,"Da thay doi trang thai voi nguoi dung",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
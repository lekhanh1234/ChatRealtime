package com.example.messenger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

public class SetUserCanFind extends AppCompatActivity {
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private Button BTN_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_can_find);
        if(!CheckInternet.isNetworkAvailable(this)){
            Toast.makeText(this,"Khong co ket noi internet",Toast.LENGTH_LONG).show();
            finish();
        }
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        BTN_submit = findViewById(R.id.BTN_submit);
        // chạy một luồng lên server và để check box nào sẽ được check
        int result = new OperationServer().getStatesInteractiveUser(InfoMyAccount.getAccountId());
        if(result == 1) checkBox1.setChecked(true);
        if(result == 2) checkBox2.setChecked(true);
        if(result == 3) checkBox3.setChecked(true);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                }
            }
        });
        BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int states = 0;
                if(checkBox1.isChecked()) states = 1;
                if(checkBox2.isChecked()) states = 2;
                if(checkBox3.isChecked()) states = 3;

                if(!CheckInternet.isNetworkAvailable(SetUserCanFind.this)){
                        Toast.makeText(SetUserCanFind.this,"Kiểm tra Internet của bạn",Toast.LENGTH_LONG).show();
                        return;
                }
                new OperationServer().changeInteractiveUser(InfoMyAccount.getAccountId(),states);
                Toast.makeText(SetUserCanFind.this,"Thay đổi thành công",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
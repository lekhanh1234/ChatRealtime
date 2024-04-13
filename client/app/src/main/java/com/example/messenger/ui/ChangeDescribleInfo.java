package com.example.messenger.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

public class ChangeDescribleInfo extends AppCompatActivity {
    private EditText EDT_DescribleYourself;
    private Button BTN_Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_describle_info);
        EDT_DescribleYourself = findViewById(R.id.EDT_DescribleYourself);
        EDT_DescribleYourself.setText(InfoMyAccount.getIntroduce());

        BTN_Confirm = findViewById(R.id.BTN_Confirm);
        BTN_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(ChangeDescribleInfo.this)){
                    Toast.makeText(ChangeDescribleInfo.this,"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                new OperationServer().changeIntroduce(InfoMyAccount.getAccountId(),EDT_DescribleYourself.getText().toString());
                Toast.makeText(ChangeDescribleInfo.this,"Đã cập nhật thông tin",Toast.LENGTH_SHORT).show();
                InfoMyAccount.setIntroduce(EDT_DescribleYourself.getText().toString());
            }
        });
    }
}
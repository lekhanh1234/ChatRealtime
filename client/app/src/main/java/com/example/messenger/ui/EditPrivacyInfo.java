package com.example.messenger.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import java.util.Calendar;

public class EditPrivacyInfo extends AppCompatActivity {
    private EditText EDT_yourName;
    Switch SW_sex;
    private TextView TV_selectedBirthDay;
    private Button BT_selectBirthDay;
    private EditText EDT_myAccountName;
    private Button BT_updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_privacy_info);
        Intent a= getIntent();
        EDT_yourName = findViewById(R.id.EDT_yourName);
        SW_sex = findViewById(R.id.SW_sex);
        TV_selectedBirthDay =findViewById(R.id.TV_selectedBirthDay);
        // value chi con co the la 2 => nu
        SW_sex.setChecked(a.getIntExtra("sex", 0) == 1 || a.getIntExtra("sex", 0) == 0); // 1 => nam  || 0 => chưa thiết lập trong db. (set mac dinh la nam)
        
        TV_selectedBirthDay.setText(a.getStringExtra("birthday"));
        BT_selectBirthDay = findViewById(R.id.BT_selectBirthDay);
        BT_selectBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditPrivacyInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TV_selectedBirthDay.setText(year+" - "+(month+1)+" - "+dayOfMonth);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        EDT_myAccountName = findViewById(R.id.EDT_myAccountName);
        EDT_myAccountName.setText(InfoMyAccount.getNameAccount());

        BT_updateInfo = findViewById(R.id.BT_updateInfo);
        BT_updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newYourName = EDT_yourName.getText().toString().trim();
                if(!newYourName.matches("^[a-zA-Z ]{2,}$")){
                    Toast.makeText(EditPrivacyInfo.this,"Tên của bạn phải có ít nhất 2 kí tự và chỉ bao gồm chữ cái và không bao gồm kí tự đặc biệt",Toast.LENGTH_LONG).show();
                    return;
                }
                String newSex = SW_sex.isChecked() ? "Nam" : "Nữ";

                String newbirthDay = TV_selectedBirthDay.getText().toString();
                if(newbirthDay.equals("-----")){
                    Toast.makeText(EditPrivacyInfo.this,"Vui lòng nhập thông tin ngày sinh",Toast.LENGTH_SHORT).show();
                    return;
                }
                String newNameAccount = EDT_myAccountName.getText().toString().trim();
                if(!newNameAccount.matches("^[a-zA-Z0-9 ]{3,}$")){
                    Toast.makeText(EditPrivacyInfo.this,"Tên tài khoản phải có ít nhất 3 kí tự và chỉ bao gồm chữ cái và số. Không bao gồm kí tự đặc biệt",Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPrivacyInfo.this);
                alertDialog.setTitle("Update Profile");
                alertDialog.setMessage("Bạn chắc chắn thay đổi thông tin");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("5555", "onClick: "+newYourName+" - "+newSex+" - "+newbirthDay+" - "+newNameAccount);
                        //chay 1 luong len may chu de tien hanh thay doi thong tin tren server
                        if(!CheckInternet.isNetworkAvailable(EditPrivacyInfo.this)){
                            Toast.makeText(EditPrivacyInfo.this,"Kiểm tra Internet của bạn",Toast.LENGTH_LONG).show();
                            return;
                        }
                        new OperationServer().changePrivacyInfo(InfoMyAccount.getAccountId(),newYourName,newSex,newbirthDay,newNameAccount);
                        // tai day chung ta se thuc hien thay doi thong tin cua account trong app
                        InfoMyAccount.setNameAccount(newNameAccount);
                        Intent a = new Intent();
                        a.putExtra("newYourName",newYourName);
                        a.putExtra("newSex",newSex);
                        a.putExtra("newbirthDay",newbirthDay);
                        setResult(200,a);
                        finish();
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });
    }
}
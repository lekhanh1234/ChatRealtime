package com.example.messenger.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    private EditText edt_passWord;
    private EditText EDT_newPassword;
    private  EditText EDT_reNewPassword;
    private Button BTN_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edt_passWord = findViewById(R.id.edt_passWord);
        EDT_newPassword = findViewById(R.id.EDT_newPassword);
        EDT_reNewPassword = findViewById(R.id.EDT_reNewPassword);
        BTN_submit = findViewById(R.id.BTN_submit);
        getSupportActionBar().setTitle("Đổi mật khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BTN_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chạy một luồng lên server để thay đổi thông tin
                String password = edt_passWord.getText().toString();
                String newPassword = EDT_newPassword.getText().toString();
                String reNewPassword = EDT_reNewPassword.getText().toString();
                if(password.length() == 0 || newPassword.length() == 0 || reNewPassword.length() == 0){
                    Toast.makeText(ChangePassword.this,"Thông tin trống",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!checkPassword(newPassword)){
                    Toast.makeText(ChangePassword.this, "password mới không hợp lệ,mật khẩu yêu cầu có ít nhất 6 kí tự và ít nhất 1 kí tự hoa,thường và chữ số", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!newPassword.equals(reNewPassword)){
                    Toast.makeText(ChangePassword.this,"Mật khẩu mới không khớp",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.equals(newPassword)){
                    Toast.makeText(ChangePassword.this,"Mật khẩu mới trùng mật khẩu cũ của bạn",Toast.LENGTH_LONG).show();
                    return;
                }
                // chạy một luồng lên server để kiểm tra mật khẩu mới. và xem có user nào có tên account và password giống này không
                if(!CheckInternet.isNetworkAvailable(ChangePassword.this)){
                    Toast.makeText(ChangePassword.this,"Kiểm tra kết nối Internet",Toast.LENGTH_LONG).show();
                    return;
                }
                int result = new OperationServer().changePassword(InfoMyAccount.getAccountId(),password,newPassword);
                if(result == -1){
                    Toast.makeText(ChangePassword.this,"Mật khẩu bạn nhập không chính xác",Toast.LENGTH_LONG).show();
                }
                if(result == 0){
                    Toast.makeText(ChangePassword.this,"Thao tác thất bại,đã tồn tại tài khoản khác với UserName và Password của bạn",Toast.LENGTH_LONG).show();
                }
                if(result == 1){
                    Toast.makeText(ChangePassword.this,"Đã thay đổi mật khẩu",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public boolean checkPassword(String password) {
        String formatPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        Pattern patternPassword = Pattern.compile(formatPassword);
        Matcher matcherPassword = patternPassword.matcher(password);
        return matcherPassword.matches();
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
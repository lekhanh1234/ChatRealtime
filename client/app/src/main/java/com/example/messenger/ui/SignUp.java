package com.example.messenger.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    String TAG = "signUp";
    private final Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText edt_usedName = findViewById(R.id.EDT_UserNameLogin);
        EditText edt_passWord = findViewById(R.id.edt_passWord);
        EditText edt_rePassWord = findViewById(R.id.edt_re_enter_password);
        Button btn_signUp = findViewById(R.id.btn_signUp);
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(isChecked){
                     btn_signUp.setBackgroundColor(getResources().getColor(R.color.blue));
                 }
                 else btn_signUp.setBackgroundColor(getResources().getColor(R.color.color_xam_tab));
            }
        });
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked()) return;
                String usedname = edt_usedName.getText().toString();
                String passWord = edt_passWord.getText().toString();
                String repassWord = edt_rePassWord.getText().toString();
                if(!usedname.matches("^[a-zA-Z0-9]{6,}$")){
                    Toast.makeText(SignUp.this,"Username chỉ có thể chứa kí tự hoặc số và ít nhất 6 chữ số",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!checkPassword(passWord)){
                    Toast.makeText(SignUp.this, "password không hợp lệ,mật khẩu yêu cầu có ít nhất 6 kí tự và ít nhất 1 kí tự hoa,thường và chữ số", Toast.LENGTH_LONG).show();
                    return;
                }
                if (repassWord.equals(passWord) == false){
                    Toast.makeText(SignUp.this, "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!CheckInternet.isNetworkAvailable(SignUp.this)){
                    Toast.makeText(SignUp.this,"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                }
                int result = new OperationServer().addAccount(usedname,passWord);
                if(result == 1) Toast.makeText(SignUp.this,"Đăng kí thành công",Toast.LENGTH_LONG).show();
                else if(result == 0) Toast.makeText(SignUp.this,"Tài khoản đã tồn tại",Toast.LENGTH_LONG).show();
                else Toast.makeText(SignUp.this,"Lỗi ! ,Không thể kết nối máy chủ",Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkPassword(String password) {
        String formatPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        Pattern patternPassword = Pattern.compile(formatPassword);
        Matcher matcherPassword = patternPassword.matcher(password);
        return matcherPassword.matches();
    }
}
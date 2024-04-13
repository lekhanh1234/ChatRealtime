package com.example.messenger.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.Service.ServiceSendRetrieveMessage;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

public class LogIn extends AppCompatActivity {
    public static boolean requestFinish = false; // mac dinh khi quay ve la se finish
    //  0 nghĩa là chưa có thay đổi gì.
    // -1 là trả về tử activity from_main với yêu cầu logout. // lúc này hàm finish k được gọi
    //  1 là trả về tử from_main và k yêu cầu logout // gọi ngay hàm finish
    private final String TAG="logIn";
   private Button createAccount;
   private EditText EDTUserNameLogin;
   private EditText EDTPasswordLogin;
   private Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityLifecycleCallbacks.finish = false;
        setContentView(R.layout.activity_log_in);
        EDTUserNameLogin=findViewById(R.id.edt_username_login);
        EDTPasswordLogin=findViewById(R.id.edt_password_login);
        createAccount=findViewById(R.id.btn_createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(LogIn.this, SignUp.class);
                startActivity(a);
            }
        });
        btn_continue=findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameLogin=EDTUserNameLogin.getText().toString();
                String passwordLogin=EDTPasswordLogin.getText().toString();
                if(!CheckInternet.isNetworkAvailable(LogIn.this)){
                    Toast.makeText(LogIn.this,"Kiểm tra kết nối Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ServiceSendRetrieveMessage.service != null ) {
                    ServiceSendRetrieveMessage.service.webSocketClient.close();
                    ServiceSendRetrieveMessage.service.stopSelf();
                }

                if(checkInfo(userNameLogin, passwordLogin)) {
                    int result = new OperationServer().checkLogin(userNameLogin,passwordLogin);
                    if(result == -1 ){
                        Toast.makeText(LogIn.this,"Lỗi ! ,Không thể kết nối đến máy chủ",Toast.LENGTH_SHORT).show();
                    } else if(result > 0){
                        onHandleSucessLogin(result,userNameLogin,passwordLogin);
                    }
                    else onHandleFailLogin();
                }
            }
        });
        checkInternetForLogin();
    }
    private void checkInternetForLogin(){
        if(!CheckInternet.isNetworkAvailable(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet ").setMessage("Kiểm tra kết nối Internet")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            checkInternetForLogin();
                        }
                    });
            builder.create().show();
        } else{
            String[] accountInfo =getFileAccountInfo();
            if(accountInfo != null){
                if (ServiceSendRetrieveMessage.service != null ){
                    ServiceSendRetrieveMessage.service.webSocketClient.close();
                    ServiceSendRetrieveMessage.service.stopSelf();
                }
                int result = new OperationServer().checkLogin(accountInfo[0], accountInfo[1]);
                if (result != -1) {
                    onHandleSucessLogin(result,accountInfo[0],accountInfo[1]);
                } else onHandleFailLogin();
            }
        }
    }
    private boolean checkInfo(String userNameLogin,String passwordLogin){
        if(userNameLogin.length()==0||passwordLogin.length()==0){
            Toast.makeText(this,"Thông tin đăng nhập trống",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private String[] getFileAccountInfo(){
        SharedPreferences sharedPreferences= getSharedPreferences("account",MODE_PRIVATE);
        String userNameLogin=sharedPreferences.getString("userNameLogin","");
        String passwordLogin=sharedPreferences.getString("passwordLogin","");
        if(userNameLogin.length()==0&&passwordLogin.length()==0) return null;
        return new String[]{userNameLogin,passwordLogin};
    }
    public void saveAccountInfo(String userNameLogin,String passwordLogin){
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userNameLogin",userNameLogin);
        editor.putString("passwordLogin",passwordLogin);
        editor.apply();
    }
    public void deleteAccountInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish = true) MyActivityLifecycleCallbacks.finish = false;
        if(LogIn.requestFinish)
        {
            LogIn.requestFinish = false;
            finish();
        }
        super.onResume();
    }
    public void onHandleSucessLogin(int accountId,String userNameLogin,String passwordLogin){
        saveAccountInfo(userNameLogin,passwordLogin);
        EDTUserNameLogin.setText("");
        EDTPasswordLogin.setText("");
        InfoMyAccount.setAccountId(accountId);
        //////////////
        OperationServer operationServer = new OperationServer();
        InfoMyAccount.nameAccount = operationServer.getAccountName(InfoMyAccount.getAccountId());
        InfoMyAccount.setNameAccount(InfoMyAccount.nameAccount);
        InfoMyAccount.avata = operationServer.getAvatar(InfoMyAccount.getAccountId());
        InfoMyAccount.setAvata(InfoMyAccount.avata );
        InfoMyAccount.coverImage = operationServer.getCoverPicture(InfoMyAccount.getAccountId());
        InfoMyAccount.setCoverImage(InfoMyAccount.coverImage);
        InfoMyAccount.introduce = operationServer.getIntroducUser(InfoMyAccount.getAccountId());
        InfoMyAccount.setIntroduce(InfoMyAccount.introduce);
        ///////////////////////////////////////////////////////////////////// bắt đầu service mới
        startService(new Intent(LogIn.this, ServiceSendRetrieveMessage.class));
        startActivity(new Intent(LogIn.this, App.class));
    }
    public void onHandleFailLogin(){
        deleteAccountInfo();
        Toast.makeText(LogIn.this,"Username hoặc password không chính xác",Toast.LENGTH_SHORT).show();
    }
}
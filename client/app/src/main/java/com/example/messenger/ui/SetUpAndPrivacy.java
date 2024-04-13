package com.example.messenger.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.model.MyPrivacyInfo;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.makeramen.roundedimageview.RoundedImageView;

public class SetUpAndPrivacy extends AppCompatActivity {
    ImageView IMG_CoverImage;
    RoundedImageView RIV_avatar;
    TextView TV_NameAccount;
    TextView TV_gioitinh;
    TextView TV_ngaysinhcuthe;
    TextView TV_myName;
    TextView TV_myAccountName;
    ConstraintLayout CL_changePassword;
    ConstraintLayout CL_InteractiveWithUser;
    ConstraintLayout CL_statusWithUser;
    ConstraintLayout CL_BlockUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_and_privacy);
        IMG_CoverImage = findViewById(R.id.IMG_CoverImage);
        IMG_CoverImage.setImageBitmap(BitmapFactory.decodeByteArray(InfoMyAccount.getCoverImage(),0,InfoMyAccount.getCoverImage().length));
        RIV_avatar = findViewById(R.id.RIV_avata);
        RIV_avatar.setImageBitmap(BitmapFactory.decodeByteArray(InfoMyAccount.getAvata(),0, InfoMyAccount.getAvata().length));
        TV_NameAccount = findViewById(R.id.TV_NameAccount);
        TV_gioitinh = findViewById(R.id.TV_gioitinhcuthe);
        TV_ngaysinhcuthe = findViewById(R.id.TV_ngaysinhcuthe);
        TV_myName = findViewById(R.id.TV_myName);
        TV_myAccountName = findViewById(R.id.TV_myAccountName);
        CL_changePassword = findViewById(R.id.CL_changePassword);
        CL_InteractiveWithUser = findViewById(R.id.CL_InteractiveWithUser);
        CL_statusWithUser = findViewById(R.id.CL_statusWithUser);
        CL_BlockUser = findViewById(R.id.CL_BlockUser);

        TV_NameAccount.setText(InfoMyAccount.getNameAccount());
        MyPrivacyInfo myPrivacyInfo = new OperationServer().getMyPrivacyInfo();
        String gioitinh= "-----"; // là khi getSex == 0;
        if(myPrivacyInfo.getSex()==2) gioitinh = "Nữ";
        if(myPrivacyInfo.getSex()==1) gioitinh = "Nam";

        String birthDay = "-----"; // là khi getBirthday == "";
        if(myPrivacyInfo.getBirthday().length() != 0) birthDay =  myPrivacyInfo.getBirthday();

        TV_myName.setText(myPrivacyInfo.getMyName());
        TV_gioitinh.setText(gioitinh);
        TV_ngaysinhcuthe.setText(birthDay);
        TV_myAccountName.setText(InfoMyAccount.getNameAccount());
        Button BTN_editPrivacyInfo = findViewById(R.id.BTN_editPrivacyInfo);
        BTN_editPrivacyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(SetUpAndPrivacy.this, EditPrivacyInfo.class);
                a.putExtra("sex",myPrivacyInfo.getSex());
                a.putExtra("birthday", TV_ngaysinhcuthe.getText().toString());
                startActivityForResult(a, 1);
            }
        });
        CL_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(SetUpAndPrivacy.this)){
                    Toast.makeText(SetUpAndPrivacy.this,"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(SetUpAndPrivacy.this, ChangePassword.class);
                startActivity(a);
            }
        });
        CL_InteractiveWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(SetUpAndPrivacy.this)){
                    Toast.makeText(SetUpAndPrivacy.this,"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(SetUpAndPrivacy.this, SetUserCanFind.class);
                startActivity(a);
            }
        });
        CL_statusWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(SetUpAndPrivacy.this)){
                    Toast.makeText(SetUpAndPrivacy.this,"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(SetUpAndPrivacy.this, ShowState.class);
                startActivity(a);
            }
        });
        CL_BlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(SetUpAndPrivacy.this)){
                    Toast.makeText(SetUpAndPrivacy.this,"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(SetUpAndPrivacy.this, BlockUser.class);
                startActivity(a);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode ==200){
                // tien hanh thay doi thong tin
                Log.d("thong tin da duoc thay doi", "onActivityResult: ");
                String newYourName = data.getStringExtra("newYourName");
                String newSex = data.getStringExtra("newSex");
                String newbirthDay =data.getStringExtra("newbirthDay");
                TV_myName.setText(newYourName);
                TV_gioitinh.setText(newSex);
                TV_ngaysinhcuthe.setText(newbirthDay);
                TV_myAccountName.setText(InfoMyAccount.getNameAccount());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
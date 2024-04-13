package com.example.messenger.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTTopPicture;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class InteractWithAccountUser extends AppCompatActivity {
    private String chatUserName;
    private byte[] avatar;
    private int chatUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_with_account_user);
        //
        //
        Intent intent=getIntent();
        this.chatUserId=intent.getIntExtra("chatUserId",-1);
        this.avatar = new OperationServer().getAvatar(this.chatUserId);
        this.chatUserName = intent.getStringExtra("userAccountName");
        TextView TV_userName = findViewById(R.id.TV_userName);
        TV_userName.setText(this.chatUserName);
        RoundedImageView RIV_Avatar = findViewById(R.id.avatarUser);
        RIV_Avatar.setImageBitmap(BitmapFactory.decodeByteArray(this.avatar,0,this.avatar.length));

        // chay tiep mot luong len may chu de lay thong tin so luong nguoi theo doi. la tu nhung nguoi gui ket ban
        int amountFollow = new OperationServer().getUserAmountFollowByUserId(this.chatUserId);
        TextView TV_AmountUserFollow = findViewById(R.id.TV_AmountUserFollow);
        TV_AmountUserFollow.setText(amountFollow + " người đang theo dõi");
        // => complete
        ///////////------------>

        // tai day ta se chay một luồng lên máy chủ để lấy 6 bức ảnh gần nhất mà nguoi nay da them vao profile
        List<byte[]> profilePictureList = new OperationServer().getTopPictureByUserId(this.chatUserId,6);
        if(profilePictureList == null){
            TextView TV_pictureUser = findViewById(R.id.TV_pictureUser);
            TV_pictureUser.setText("Ảnh người dùng hiện không có sẵn");
            RecyclerView RCV_UserPicture = findViewById(R.id.RCV_UserPicture);
            RCV_UserPicture.setVisibility(View.GONE);
        }else {
            RecyclerView RCV_UserPicture = findViewById(R.id.RCV_UserPicture);
            ADTTopPicture adtTopPicture = new ADTTopPicture(this, profilePictureList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            RCV_UserPicture.setAdapter(adtTopPicture);
            RCV_UserPicture.setLayoutManager(gridLayoutManager);
            RCV_UserPicture.setNestedScrollingEnabled(false);
            ////////----> compelete
        }

        ConstraintLayout CL_goto_profileUser = findViewById(R.id.CL_goto_profileUser);
        CL_goto_profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(InteractWithAccountUser.this)){
                    Toast.makeText(InteractWithAccountUser.this,"Kiểm tra kết nối Internet",Toast.LENGTH_LONG).show();
                    return;
                }
                if(new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),chatUserId) == -1){
                    Toast.makeText(InteractWithAccountUser.this,"Bạn không thể truy cập vào tài khoản này",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent a=new Intent(InteractWithAccountUser.this,User.class);
                a.putExtra("userId",chatUserId);
                a.putExtra("accountName",chatUserName);
                a.putExtra("amountFollow",amountFollow);
                startActivity(a);
        //=>complete
            }
        });


        //////////////// --->
        ConstraintLayout CL_BlockUser = findViewById(R.id.CL_BlockUser);
        CL_BlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // xoa thanh cong
                    AlertDialog.Builder builder = new AlertDialog.Builder(InteractWithAccountUser.this);
                    builder.setMessage("Bạn không thể thay đổi hành động này ít nhất 48h sau ,Bạn cũng không thể kết bạn hay gửi tin nhắn cho "+InteractWithAccountUser.this.chatUserName+" với hành động này ! Vẫn tiếp tục");
                    builder.setTitle("Block User");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!CheckInternet.isNetworkAvailable(InteractWithAccountUser.this)){
                                Toast.makeText(InteractWithAccountUser.this,"Không có kết nối Internet ",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            boolean result = new OperationServer().blockUser(InfoMyAccount.getAccountId(),chatUserId);
                            dialog.dismiss();
                            if(!result){
                                Toast.makeText(InteractWithAccountUser.this,"Thao tác hiện tại là không khả thi",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog =  builder.create();
                    alertDialog.show();
                }
        });
        //////////////// --->
        ConstraintLayout CL_DeleteAllMessages = findViewById(R.id.CL_DeleteAllMessages);
        CL_DeleteAllMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  tao mot hop thoai de dam dao nguoi dung chac chan
                AlertDialog.Builder builder= new AlertDialog.Builder(InteractWithAccountUser.this);
                builder.setTitle("Delete Messages");
                builder.setMessage("Bạn không thể phôi phục hành động này ! Vẫn tiếp tục");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!CheckInternet.isNetworkAvailable(InteractWithAccountUser.this)){
                            Toast.makeText(InteractWithAccountUser.this,"Kiểm tra kết nối Internet của bạn !",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // chay mot luong len may chu de xoa hanh dong
                            new OperationServer().deleteAllMesssage(InfoMyAccount.getAccountId(),chatUserId);
                            dialog.dismiss();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        //////////////////////------>
        /// cuoi cung la yeu cau xoa ban
        ConstraintLayout CL_DeleteFriend = findViewById(R.id.CL_DeleteFriend);
        CL_DeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  tao mot hop thoai de dam dao nguoi dung chac chan
                AlertDialog.Builder builder= new AlertDialog.Builder(InteractWithAccountUser.this);
                builder.setTitle("Delete Friend");
                builder.setMessage("Bạn muốn xóa "+InteractWithAccountUser.this.chatUserName+" khỏi danh sách bạn bè !");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!CheckInternet.isNetworkAvailable(InteractWithAccountUser.this)){
                            Toast.makeText(InteractWithAccountUser.this,"Kiểm tra kết nối Internet của bạn !",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // chay mot luong len may chu de xoa hanh dong
                            boolean result = new OperationServer().deleteFriend(InfoMyAccount.getAccountId(),chatUserId);
                            if(!result) Toast.makeText(InteractWithAccountUser.this,"Hành động hiện tại không khả thi, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
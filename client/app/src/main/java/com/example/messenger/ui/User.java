package com.example.messenger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTSelectFriend;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class User extends AppCompatActivity {
    private int userId;
    private int relationship;

    public int getUserId() {
        return userId;
    }
    private TextView TV_DescribeUser;
    private TextView TV_states;
    private ConstraintLayout CL_InteractiveWithUser;
    private ConstraintLayout CL_UserPicture;
    private ConstraintLayout CL_operation_requestFriendReceive;
    private ConstraintLayout CL_operation_deleteFriend;
    private RecyclerView RV_Friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent a = getIntent();
        this.userId = a.getIntExtra("userId",-1);
        byte[] avatar = new OperationServer().getAvatar(this.userId);
        int amountFollow = a.getIntExtra("amountFollow",-1);
        String accountName = a.getStringExtra("accountName");

        setContentView(R.layout.activity_user);
        TV_DescribeUser = findViewById(R.id.TV_DescribeUser);
        String result = new OperationServer().getIntroducUser(userId);
        if(result.length() == 0) TV_DescribeUser.setVisibility(View.GONE);
        else TV_DescribeUser.setText(result);
        TV_DescribeUser.post(new Runnable() {
            @Override
            public void run() {
                TV_DescribeUser.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int width = TV_DescribeUser.getMeasuredWidth();
                int height = TV_DescribeUser.getMeasuredHeight();
            }
        });

        CL_InteractiveWithUser = findViewById(R.id.CL_InteractiveWithUser);
        CL_operation_requestFriendReceive = findViewById(R.id.CL_operation_requestFriendReceive);
        CL_operation_deleteFriend = findViewById(R.id.CL_operation_deleteFriend);

        CL_UserPicture = findViewById(R.id.CL_UserPicture);
        CL_UserPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.isNetworkAvailable(User.this) == false){
                    Toast.makeText(User.this,"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(User.this, AllUserPicture.class);
                a.putExtra("userId",userId);
                startActivity(a);
            }
        });


        //luong dau tien chay len server de lay thong tin ve anh bia
         byte[] coverPicture = new OperationServer().getCoverPicture(userId);
        ImageView IMG_coverPicture = findViewById(R.id.IMG_anhbia);
        if(coverPicture != null){
            IMG_coverPicture.setImageBitmap(BitmapFactory.decodeByteArray(coverPicture,0,coverPicture.length));
        }
        ////////////==>
        // ảnh đại diện và tên sẽ được gửi từ activity trước đó
        RoundedImageView RIV_anhdaidien = findViewById(R.id.RIV_anhdaidien);
        RIV_anhdaidien.setImageBitmap(BitmapFactory.decodeByteArray(avatar,0,avatar.length));
        TextView TVname_profile = findViewById(R.id.TVname_profile);
        TVname_profile.setText(accountName);
        ////////////////////////////===>
        // thực thi 1 luồng lên server để lấy thông tin trạng thái. //
        this.relationship = new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),userId);
        CL_InteractiveWithUser = findViewById(R.id.CL_InteractiveWithUser);
        CL_operation_requestFriendReceive = findViewById(R.id.CL_operation_requestFriendReceive);
        TV_states = findViewById(R.id.TV_states);
        if(relationship == -1){
            Toast.makeText(this,"Bạn không thể truy cập vào tài khoản này",Toast.LENGTH_LONG).show();
            finish();
        }
        if(relationship == 0){
            TV_states.setText("Thêm bạn bè");
            CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
        }
        if(relationship == 1){
            TV_states.setText("Chấp nhận lời mời");
            CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(this,R.color.color_xam_tab));
        }
        if(relationship == 2){
            TV_states.setText("Hủy");
            CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
        }
        if(relationship == 3){
            TV_states.setText("Bạn bè");
            CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
        }
        CL_InteractiveWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectUser(userId);
            }
        });

        //-->// -->
        TextView TV_amountFollow = findViewById(R.id.TV_amountFollow); // gửi từ activity trước đến
        TV_amountFollow.setText(amountFollow+" người đang theo dõi");

        TextView TV_banchung = findViewById(R.id.TV_banchung); // chạy một luồng lên server
        int resultMutualFriend = new OperationServer().getMutualFriends(InfoMyAccount.getAccountId(),userId);
        TV_banchung.setText(resultMutualFriend+" bạn chung");
        /////////////
        TextView TV_amount_friend = findViewById(R.id.TV_amount_friend); // chạy một luồng lên server
        int amountFriends = new OperationServer().getAmountFriends(userId);
        TV_amount_friend.setText(amountFriends+" bạn bè");

        RV_Friend = findViewById(R.id.RV_Friend);
        List<GeneralUserInfomation> list = new OperationServer().getFriend(userId,6);
        ADTSelectFriend adtSelectFriend = new ADTSelectFriend(list,this);
        RV_Friend.setAdapter(adtSelectFriend);
        RV_Friend.setLayoutManager(new GridLayoutManager(this,3));
        RV_Friend.setNestedScrollingEnabled(false);

    }
    public void connectUser(int userId){
        if(relationship == 0) {
            // yêu cầu kết bạn
            boolean result = new OperationServer().sendRequestFriendToUser(InfoMyAccount.getAccountId(),userId);
            if(result){
                TV_states.setText("Đã gửi lời mời");
                this.relationship = 2;
                CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(this,R.color.color_xam_tab));
            }
            else{
                Toast.makeText(this,"Thao tác hiện không khả thi . vui lòng thử lại sau",Toast.LENGTH_LONG).show();
            }

        }else if(relationship == 1){
            //chấp nhận yêu cầu kết bạn hoặc hủy lời mời
            CL_operation_requestFriendReceive.setVisibility(View.VISIBLE);
            Button BTN_CloseCL = CL_operation_requestFriendReceive.findViewById(R.id.BTN_CloseCL);
            Button BTN_CancelRequest = CL_operation_requestFriendReceive.findViewById(R.id.BTN_CancelRequest);
            Button BTN_acceptRequest = CL_operation_requestFriendReceive.findViewById(R.id.BTN_acceptRequest);
            BTN_CloseCL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CL_operation_requestFriendReceive.setVisibility(View.GONE);
                }
            });
            BTN_CancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // sử lí kết quả tại đây.
                    boolean result = new OperationServer().cancelRequestFriend(InfoMyAccount.getAccountId(),userId);
                    if(!result){
                        Toast.makeText(User.this,"Hành động không khả thi, vui lòng gửi lại sau",Toast.LENGTH_LONG).show();
                    } else{
                        TV_states.setText("Thêm bạn bè");
                        User.this.relationship = 0;
                        CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(User.this,R.color.blue));
                    }
                    CL_operation_requestFriendReceive.setVisibility(View.GONE);
                }
            });
            BTN_acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = new OperationServer().acceptFriendRequest(InfoMyAccount.getAccountId(),userId);
                    if(!result){
                        Toast.makeText(User.this,"Thao tác hiện không khả thi, vui lòng thử lại sau",Toast.LENGTH_LONG).show();
                    } else{
                        TV_states.setText("Bạn bè");
                        User.this.relationship = 3;
                        CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(User.this,R.color.blue));
                    }
                    CL_operation_requestFriendReceive.setVisibility(View.GONE);
                }
            });
        } else if(relationship == 2){
            boolean result = new OperationServer().cancelRequestFriend(InfoMyAccount.getAccountId(),userId);
            if(result){
                TV_states.setText("Thêm bạn bè");
                User.this.relationship = 0;
                CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
            }
            else {
                Toast.makeText(User.this, "Thao tác hiện không khả thi, vui lòng thử lại sau", Toast.LENGTH_LONG).show();
            }
        } else if(relationship == 3){
            CL_operation_deleteFriend.setVisibility(View.VISIBLE);
            Button BTN_CloseCL2= findViewById(R.id.BTN_CloseCL2);
            Button BTN_deleteFriend = findViewById(R.id.BTN_deleteFriend);
            BTN_CloseCL2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CL_operation_deleteFriend.setVisibility(View.GONE);
                }
            });
            BTN_deleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean result = new OperationServer().deleteFriend(InfoMyAccount.getAccountId(),userId);
                    if(!result){
                        Toast.makeText(User.this,"Thao thác hiện tại không khả thi, vui lòng thử lại sau",Toast.LENGTH_LONG).show();
                    }
                    else {
                        TV_states.setText("Thêm bạn bè");
                        CL_InteractiveWithUser.setBackgroundColor(ContextCompat.getColor(User.this,R.color.blue));
                        relationship = 0;
                    }
                    CL_operation_deleteFriend.setVisibility(View.GONE);
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        if(CL_operation_deleteFriend.getVisibility()==View.VISIBLE){
            CL_operation_deleteFriend.setVisibility(View.GONE);
            return;
        }
        if(CL_operation_requestFriendReceive.getVisibility()==View.VISIBLE){
            CL_operation_requestFriendReceive.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if( new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),userId) == -1) finish();
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
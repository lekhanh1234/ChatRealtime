package com.example.messenger.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTMyPost;
import com.example.messenger.adapter.ADTSelectFriend;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.model.MyPost;
import com.example.messenger.model.UserPicture;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ProfileMyself extends AppCompatActivity {
    private ConstraintLayout CL_Content;
    private ImageView IMG_coverImage;
    private RoundedImageView RIV_Avatar;
    private TextView TVname_profile;
    private TextView TV_ViewMyPicture;
    private ImageView IMG_ChangeDescribleYourself;
    private TextView TV_DescribeYourself;
    private TextView TV_FriendAmount;
    private RecyclerView RV_Friend;
    private RecyclerView RCV_Post;
    private LinearLayout LL_CloseSelectPicture;
    private ConstraintLayout myCL_option_picture;
    private ConstraintLayout CL_see_pictureProfile;
    private ConstraintLayout CL_change_pictureProfile;
    private final int codeLookAvatar=0;
    private final int codeGetPicture=1;
    private final int codeChangeDescribleInfo=2;
    private int screenHeight;
    private List<MyPost> myPost;
    private ADTMyPost adtMyPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_myself);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        CL_Content = findViewById(R.id.CL_Content);
        CL_Content.getLayoutParams().height = screenHeight;
        CL_Content.requestLayout();

        IMG_coverImage = findViewById(R.id.IMG_coverImage);
        IMG_coverImage.setImageBitmap(BitmapFactory.decodeByteArray(InfoMyAccount.getCoverImage(),0, InfoMyAccount.getCoverImage().length));

        RIV_Avatar = findViewById(R.id.RIV_anhdaidien);
        RIV_Avatar.setImageBitmap(BitmapFactory.decodeByteArray(InfoMyAccount.getAvata(),0, InfoMyAccount.getAvata().length));
        RIV_Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        myCL_option_picture.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams lp = myCL_option_picture.getLayoutParams();
                        lp.height = screenHeight;
                        myCL_option_picture.setLayoutParams(lp);
                    }
                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                    }
                };

                TedPermission.create().setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });

        TVname_profile = findViewById(R.id.TVname_profile);
        TVname_profile.setText(InfoMyAccount.getNameAccount());

        TV_ViewMyPicture = findViewById(R.id.TV_ViewMyPicture);
        TV_ViewMyPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckInternet.isNetworkAvailable(ProfileMyself.this) == false){
                    Toast.makeText(ProfileMyself.this,"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(ProfileMyself.this, AllUserPicture.class);
                a.putExtra("userId",InfoMyAccount.getAccountId());
                startActivity(a);
            }
        });

        IMG_ChangeDescribleYourself = findViewById(R.id.IMG_ChangeDescribleYourself);
        if(InfoMyAccount.getIntroduce().length() == 0){
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)IMG_ChangeDescribleYourself.getLayoutParams();
            layoutParams.setMarginEnd(-10);
            IMG_ChangeDescribleYourself.setLayoutParams(layoutParams);
        }
        IMG_ChangeDescribleYourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivityForResult(new Intent(ProfileMyself.this,ChangeDescribleInfo.class),codeChangeDescribleInfo);
            }
        });

        TV_DescribeYourself = findViewById(R.id.TV_DescribeYourself);
        TV_DescribeYourself.setText(InfoMyAccount.getIntroduce());

        TV_FriendAmount = findViewById(R.id.TV_FriendAmount);
        int amountFriends = new OperationServer().getAmountFriends(InfoMyAccount.getAccountId());
        TV_FriendAmount.setText(amountFriends+" người bạn");

        RV_Friend = findViewById(R.id.RV_Friend);
        List<GeneralUserInfomation> list = new OperationServer().getFriend(InfoMyAccount.getAccountId(),6);
        ADTSelectFriend adtSelectFriend = new ADTSelectFriend(list,this);
        RV_Friend.setAdapter(adtSelectFriend);
        RV_Friend.setLayoutManager(new GridLayoutManager(this,3));
        RV_Friend.setNestedScrollingEnabled(false);

        RCV_Post = findViewById(R.id.RCV_Post);
        // thực hiện 2 việc. lấy post từ server và thêm post.
        if(CheckInternet.isNetworkAvailable(this)){
            myPost = new OperationServer().getMyPost(InfoMyAccount.getAccountId());
        }
        adtMyPost = new ADTMyPost(myPost,this);
        RCV_Post.setAdapter(adtMyPost);
        RCV_Post.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        RCV_Post.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = 25;
            }
        });

        myCL_option_picture =findViewById(R.id.CL_option_picture);
        LL_CloseSelectPicture = findViewById(R.id.LL_CloseSelectPicture);
        LL_CloseSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCL_option_picture.setVisibility(View.GONE);
            }
        });

        CL_see_pictureProfile = findViewById(R.id.CL_see_pictureProfile);
        CL_see_pictureProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(ProfileMyself.this, LookAvatar.class);
                startActivityForResult(a,codeGetPicture);
            }
        });

        CL_change_pictureProfile=findViewById(R.id.CL_change_pictureProfile);
        CL_change_pictureProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {

                    @Override
                    public void onPermissionGranted() {
                        Intent a=new Intent(ProfileMyself.this, PickPictureToAvatar.class);
                        startActivityForResult(a,codeGetPicture);
                    }
                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                    }
                };

                TedPermission.create().setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(myCL_option_picture.getVisibility()==View.GONE) super.onBackPressed();
        else myCL_option_picture.setVisibility(View.GONE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == codeLookAvatar)  myCL_option_picture.setVisibility(View.GONE);
        if(requestCode == codeGetPicture){
            if(resultCode == 200){
                TVname_profile.setText(InfoMyAccount.getNameAccount());
                Bitmap bitmap = BitmapFactory.decodeByteArray(InfoMyAccount.getAvata(),0, InfoMyAccount.getAvata().length);
                RIV_Avatar.setImageBitmap(bitmap);
            }
            myCL_option_picture.setVisibility(View.GONE);
        }
        if(requestCode == codeChangeDescribleInfo){
                TV_DescribeYourself.setText(InfoMyAccount.getIntroduce());
            if(InfoMyAccount.getIntroduce().length() > 0){
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)IMG_ChangeDescribleYourself.getLayoutParams();
                layoutParams.setMarginEnd(10);
                IMG_ChangeDescribleYourself.setLayoutParams(layoutParams);
            }else{
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)IMG_ChangeDescribleYourself.getLayoutParams();
                layoutParams.setMarginEnd(-10);
                IMG_ChangeDescribleYourself.setLayoutParams(layoutParams);
            }
        }

        if(requestCode == 3 && resultCode == 200){
            if(CheckInternet.isNetworkAvailable(this)){
                myPost = new OperationServer().getMyPost(InfoMyAccount.getAccountId());
                adtMyPost.setUserPost(myPost);
                adtMyPost.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}



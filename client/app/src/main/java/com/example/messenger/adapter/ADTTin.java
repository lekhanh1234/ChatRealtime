package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.model.UserPostVideo;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.AddPreferencePost;
import com.example.messenger.ui.UserPost;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ADTTin extends RecyclerView.Adapter<ADTTin.Viewhold> {
    private Context context;
    private List<UserPostVideo> userListVideo;
    public void setUserListVideo(List<UserPostVideo> userListVideo) {
        this.userListVideo = userListVideo;
    }
    public ADTTin(Context context, List<UserPostVideo> userListVideo) {
        this.context = context;
        this.userListVideo = userListVideo;
    }

    @NonNull
    @Override
    public ADTTin.Viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        if(viewType == 1){
            view=inflater.inflate(R.layout.add_new_tin24h,parent,false);
        }else { // viewType just == 2
            view = inflater.inflate(R.layout.tin_card_view, parent, false);
        }
        return new Viewhold(view);
    }

    @Override
    public void onBindViewHolder(ADTTin.Viewhold holder, int position) {
        ImageView IMG_Post = holder.itemView.findViewById(R.id.IMG_Post);
        if(position == 0){
            IMG_Post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(context,AddPreferencePost.class);
                    a.putExtra("type",2);
                    context.startActivity(a);
                }
            });
        }
        else{
            TextView TV_UserName = holder.itemView.findViewById(R.id.TV_UserName);
            TV_UserName.setText(userListVideo.get(position -1).getAccountName());
            RoundedImageView RIV_avatar = holder.itemView.findViewById(R.id.RIV_avatar);
            RIV_avatar.setImageBitmap(BitmapFactory.decodeByteArray(userListVideo.get(position - 1).getAvatar(),0,userListVideo.get(position - 1).getAvatar().length));
            ImageView imageView = holder.itemView.findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(userListVideo.get(position-1).getPostVideo(),0,userListVideo.get(position - 1).getPostVideo().length));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!CheckInternet.isNetworkAvailable(context)) return;
                    // co thể lúc này đã bị block . vì hành động block k ảnh hưởng ngay đến fragment 123 cho dù vẫn online
                    if(new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),userListVideo.get(position - 1).getIdUser()) == -1){
                        Toast.makeText(context,"Bạn hiện không thể truy cập vào tài khoản này",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent a = new Intent(context, UserPost.class);
                    a.putExtra("postPath", "C:\\AllMessengerProjectFile\\Post\\" + userListVideo.get(position - 1).getIdUser()
                            + "video2.mp4");
                    a.putExtra("userId",userListVideo.get(position -1).getIdUser());
                    a.putExtra("type",2);
                    context.startActivity(a);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.d("checknull", "getItemCount:"+( userListVideo == null ? "null" : userListVideo.size()+""));
        return userListVideo == null ? 1 : 1+userListVideo.size();
    }
    public int getItemViewType(int position) {
        if(position==0) return 1;
        return 2;
    }

    class Viewhold extends RecyclerView.ViewHolder{

        public Viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }

}

package com.example.messenger.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.model.MyPost;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.ui.AddPreferencePost;
import com.example.messenger.ui.ProfileMyself;
import com.example.messenger.ui.UserPost;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.List;

public class ADTMyPost extends RecyclerView.Adapter<ADTMyPost.Viewhold> {
    private  List<MyPost> userPost;

    public void setUserPost(List<MyPost> userPost) {
        this.userPost = userPost;
    }

    private  Context context;

    public ADTMyPost(List<MyPost> userPost, Context context) {
        this.userPost = userPost;
        this.context = context;
    }

    @NonNull
    @Override
    public ADTMyPost.Viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType ==1 ) view = ((ProfileMyself)context).getLayoutInflater().inflate(R.layout.add_my_post,null,false);
        else view = ((ProfileMyself)context).getLayoutInflater().inflate(R.layout.my_post,null,false);
        return new ADTMyPost.Viewhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ADTMyPost.Viewhold holder, int position) {
        if(position == 0){
            ImageView IMG_ADDPost = holder.itemView.findViewById(R.id.IMG_AddPost);
            IMG_ADDPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a =new Intent(context, AddPreferencePost.class);
                    a.putExtra("type",1);
                    ((ProfileMyself)context).startActivityForResult(a,3);
                }
            });
        }
        else{
            ImageView IMG_MyPost = holder.itemView.findViewById(R.id.IMG_MyPost);
            IMG_MyPost.setImageBitmap(BitmapFactory.decodeByteArray(userPost.get(position - 1).getPost(),0,userPost.get(position - 1).getPost().length));
            IMG_MyPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!CheckInternet.isNetworkAvailable(context)) return;
                    Intent a = new Intent(context, UserPost.class);
                    a.putExtra("postPath",userPost.get(position - 1).getPath());
                    a.putExtra("userId",InfoMyAccount.getAccountId());
                    a.putExtra("type",1);
                    ((ProfileMyself)context).startActivityForResult(a,3);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userPost == null ? 1 : userPost.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 1;
        return super.getItemViewType(position);
    }

    public class Viewhold extends RecyclerView.ViewHolder {
        public Viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }
}

package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.model.UserPicture;
import com.example.messenger.ui.AllUserPicture;
import com.example.messenger.ui.UserPictureDetail;

import java.util.ArrayList;


public class ADTUserPicture extends RecyclerView.Adapter<ADTUserPicture.viewhold> {
    private final Context context;
    private ArrayList<UserPicture> listPicture;
    private int userId;
    public void setListPicture(ArrayList<UserPicture> listPicture) {
        this.listPicture = listPicture;
    }

    public ADTUserPicture(Context context, ArrayList<UserPicture> listPicture,int userId) {
        this.context = context;
        this.listPicture = listPicture;
        this.userId = userId;
    }
    @Override
    public viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.user_profile_picture, parent, false);
        return new viewhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewhold holder,int position) {
        ImageView imageView=holder.itemView.findViewById(R.id.IMG_UserPicture);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(listPicture.get(position).getPicture(),0,listPicture.get(position).getPicture().length));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPictureDetail.picture = listPicture.get(position).getPicture();
                UserPictureDetail.pictureId = listPicture.get(position).getPictureId();
                UserPictureDetail.userId = userId;
                Intent a = new Intent(context, UserPictureDetail.class);
                ((AllUserPicture)context).startActivityForResult(a,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPicture == null ? 0 : listPicture.size();
    }

    class viewhold extends RecyclerView.ViewHolder{

        public viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }
}

package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.messenger.R;

import java.util.List;

public class ADTTopPicture extends RecyclerView.Adapter<ADTTopPicture.viewhold> {
    private final Context context;
    private final List<byte[]> profilePictureList;

    public ADTTopPicture(Context context, List<byte[]> profilePictureList) {
        this.context = context;
        this.profilePictureList = profilePictureList;
    }

    @NonNull
    @Override
    public ADTTopPicture.viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.user_profile_picture,parent,false);
        return new viewhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ADTTopPicture.viewhold holder, int position) {
        ImageView IMG_UserPicture = holder.itemView.findViewById(R.id.IMG_UserPicture);
        Bitmap bitmap = BitmapFactory.decodeByteArray(this.profilePictureList.get(position),0,profilePictureList.get(position).length);
        IMG_UserPicture.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return profilePictureList != null ? profilePictureList.size() : 0;
    }

    public class viewhold extends RecyclerView.ViewHolder {
        public viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }
}

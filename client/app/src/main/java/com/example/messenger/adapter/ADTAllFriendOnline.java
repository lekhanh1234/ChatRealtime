package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.messenger.R;
import com.example.messenger.model.GeneralUserInfomation;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ADTAllFriendOnline extends RecyclerView.Adapter<UserPhonebookOnline> {
    private final Context context;
    List<GeneralUserInfomation> listOnlineFriend;

    public ADTAllFriendOnline(Context context, List<GeneralUserInfomation> list){
        this.listOnlineFriend = list;
        this.context = context;
    }
    @NonNull
    @Override
    public UserPhonebookOnline onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=((Activity)context).getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.phonebook_online,null);


        return new UserPhonebookOnline(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserPhonebookOnline holder, int position) {
        View view = holder.itemView;

        TextView TV_OnlineFriendName = view.findViewById(R.id.OnlineFriendName);
        TV_OnlineFriendName.setText(listOnlineFriend.get(position).getAccountName());

        RoundedImageView RIV_OnlineFriendAvatar = view.findViewById(R.id.RIV_OnlineFriendAvatar);
        byte[] avatar = listOnlineFriend.get(position).getAvatar();
        RIV_OnlineFriendAvatar.setImageBitmap(BitmapFactory.decodeByteArray(avatar,0,avatar.length));
    }

    @Override
    public int getItemCount() {
        return listOnlineFriend != null ? listOnlineFriend.size() : 0;
    }
}
class UserPhonebookOnline extends RecyclerView.ViewHolder{

    public UserPhonebookOnline(@NonNull View itemView) {
        super(itemView);
    }
}
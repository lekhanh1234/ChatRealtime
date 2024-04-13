package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.model.GeneralUserInfomation;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ADTUserChatOnline extends RecyclerView.Adapter<Viewhold> {
    Context context;
    List<GeneralUserInfomation> onlineFriendList;
    public ADTUserChatOnline(Context context, List<GeneralUserInfomation> list){
        this.context = context;
        this.onlineFriendList = list;
    }
    @NonNull
    @Override
    public Viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =((Activity)context).getLayoutInflater().inflate(R.layout.viewchild_recycview_fg1,null);
        return new Viewhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhold holder, int position) {
        View view = holder.itemView;
        RoundedImageView RIV_FriendAvata = view.findViewById(R.id.RIV_FriendAvata);
        TextView TV_FriendName = view.findViewById(R.id.TV_FriendName);
        byte[] friendAvata = onlineFriendList.get(position).getAvatar();
        RIV_FriendAvata.setImageBitmap(BitmapFactory.decodeByteArray(friendAvata,0,friendAvata.length));
        TV_FriendName.setText(onlineFriendList.get(position).getAccountName());
    }

    @Override
    public int getItemCount() {
        return onlineFriendList.size();
    }
}

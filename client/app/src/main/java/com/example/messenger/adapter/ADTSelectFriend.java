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

public class ADTSelectFriend extends RecyclerView.Adapter<ADTSelectFriend.viewhold> {
    private final List<GeneralUserInfomation> list;
    private final Context context;

    public ADTSelectFriend(List<GeneralUserInfomation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.info_friend_conversation,parent,false);
        return new viewhold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ADTSelectFriend.viewhold holder, int position) {
        View view = holder.itemView;
        RoundedImageView RIV_userAvatar = view.findViewById(R.id.RIV_userAvatar);
        RIV_userAvatar.setImageBitmap(BitmapFactory.decodeByteArray(list.get(position).getAvatar(),0,list.get(position).getAvatar().length));
        TextView TV_UserAccountName = view.findViewById(R.id.TV_UserAccountName);
        TV_UserAccountName.setText(list.get(position).getAccountName());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size() >= 6 ? 6 : list.size();
    }

    public class viewhold extends RecyclerView.ViewHolder{
        public viewhold(@NonNull View itemView) {
            super(itemView);
        }
    }
}

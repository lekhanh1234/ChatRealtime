package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.R;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.ui.UserChat;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ADTAllFriend extends BaseAdapter {
    private final List<GeneralUserInfomation> list;
    private final Context context;

    public ADTAllFriend(List<GeneralUserInfomation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.user_is_rearched,null);
        RoundedImageView RIV_UserAvatar = convertView.findViewById(R.id.RIV_userAvatar);
        TextView TV_UserAccountName = convertView.findViewById(R.id.TV_AccountName);
        /////
        String userNameAccount = list.get(position).getAccountName();
        int userId = list.get(position).getIdUser();
        byte[]userAvatar = list.get(position).getAvatar();
        Bitmap bitmap = BitmapFactory.decodeByteArray(userAvatar,0,userAvatar.length);

        RIV_UserAvatar.setImageBitmap(bitmap);
        TV_UserAccountName.setText(userNameAccount);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// bat su kien khi click vao view o day
                if(!CheckInternet.isNetworkAvailable(context)){
                    Toast.makeText(context,"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a = new Intent(context, UserChat.class);
                a.putExtra("chatUserId",userId);
                context.startActivity(a);
            }
        });
        return convertView;
    }
}

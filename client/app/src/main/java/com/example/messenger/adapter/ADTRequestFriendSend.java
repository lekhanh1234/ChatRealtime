package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ADTRequestFriendSend extends BaseAdapter {
    private final List<GeneralUserInfomation> list;
    private final Context context;

    public ADTRequestFriendSend(List<GeneralUserInfomation> list, Context context) {
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
        convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.friend_request_sent,null);
        RoundedImageView RIV_userAvatar = convertView.findViewById(R.id.RIV_userAvatar);
        GeneralUserInfomation requestFriend = list.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(requestFriend.getAvatar(),0,requestFriend.getAvatar().length);
        RIV_userAvatar.setImageBitmap(bitmap);
        TextView TV_UserAccountName = convertView.findViewById(R.id.TV_UserAccountName);
        TV_UserAccountName.setText(requestFriend.getAccountName());
        Button BT_RequestSent = convertView.findViewById(R.id.BT_RequestSent);
        BT_RequestSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gửi 1 thông điệp lên server yêu cầu hủy yêu cau ket ban
                if(!CheckInternet.isNetworkAvailable(context)){
                    Toast.makeText(context,"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                boolean result = new OperationServer().cancelRequestFriend(InfoMyAccount.getAccountId(),requestFriend.getIdUser());
                if(result){
                    list.remove(requestFriend);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context,"Thao tác hiện tại là không khả dụng, vui lòng thử lại sau",Toast.LENGTH_LONG).show();
                }

            }
        });
        return convertView;
    }
}

package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.messenger.operationServer.OperationServer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ADTBlockUser extends BaseAdapter {
    private final List<GeneralUserInfomation> list;
    private final Context context;

    public ADTBlockUser(List<GeneralUserInfomation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list !=null ? list.size() : 0;
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
        convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.block_user,null);
        GeneralUserInfomation infomation = list.get(position);

        RoundedImageView RIV_Avatar = convertView.findViewById(R.id.RIV_userAvatar);
        RIV_Avatar.setImageBitmap(BitmapFactory.decodeByteArray(infomation.getAvatar(),0,infomation.getAvatar().length));
        TextView TV_UserAccountName = convertView.findViewById(R.id.TV_UserAccountName);
        TV_UserAccountName.setText(infomation.getAccountName());
        Button BT_DeleteUserBlocked = convertView.findViewById(R.id.BT_DeleteUserBlocked);
        BT_DeleteUserBlocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gui 1 luong len server de xóa hành động block. nếu thành công thì trả về 200. ngược lại nếu ta vừa mới block thì báo về bạn chỉ có thể bỏ block 48h sau khi block
                boolean result = new OperationServer().deleteBlockUser(InfoMyAccount.getAccountId(),infomation.getIdUser());
                if(result){
                    list.remove(infomation);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context,"Bạn không thể xóa block ít nhất 48h",Toast.LENGTH_LONG).show();
                }
            }
        });
        return convertView;
    }
}

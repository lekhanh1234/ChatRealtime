package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.messenger.R;
import com.example.messenger.model.Messenger;
import com.example.messenger.ui.UserChat;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ADTMessageWating extends BaseAdapter {
    private final List<Messenger> list; // list Message
    private final Context context;

    public ADTMessageWating(List<Messenger> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        Messenger msg = list.get(position);
        convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.view_user_chat_listview_fg1,null); // su dung lai bo cuc cho cac tin nhan trong fg1
        RoundedImageView RIV_Avata=convertView.findViewById(R.id.avata_user);
        Bitmap bitmap = BitmapFactory.decodeByteArray(msg.getAvata(),0,msg.getAvata().length);
        RIV_Avata.setImageBitmap(bitmap);
        TextView TV_AccountName=convertView.findViewById(R.id.TV_AccountName);
        TV_AccountName.setText(msg.getAccountName());
        TextView TV_Content=convertView.findViewById(R.id.TV_Content);
        TV_Content.setText(msg.getLastMessage());  // mac dinh thi la k in dam. o day la tin nhan cho nen ta khong can in dam
        TextView TV_LastChatDate=convertView.findViewById(R.id.TV_LastChatDate);
        TV_LastChatDate.setText(msg.getLastReceiveMessageDate().split(" ")[0]);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(context, UserChat.class);
                a.putExtra("chatUserId",msg.getUserId());
                context.startActivity(a);
            }
        });
        return convertView;
    }
}

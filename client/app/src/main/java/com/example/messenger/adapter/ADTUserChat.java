package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.model.Messenger;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.ui.UserChat;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ADTUserChat extends RecyclerView.Adapter<viewHoldListChat> {
    private final Context context;
    private final ArrayList<Messenger> messengerList;

    public ADTUserChat(Context context, ArrayList<Messenger> messengerList) {
        this.context = context;
        this.messengerList = messengerList;
    }

    @NonNull
    @Override
    public viewHoldListChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view= inflater.inflate(R.layout.view_user_chat_listview_fg1,null);
        RoundedImageView RIV_Avata=view.findViewById(R.id.avata_user);
        TextView TV_AccountName=view.findViewById(R.id.TV_AccountName);
        TextView TV_Content=view.findViewById(R.id.TV_Content);
        TextView TV_LastChatDate=view.findViewById(R.id.TV_LastChatDate);
        return new viewHoldListChat(view,RIV_Avata,TV_AccountName,TV_Content,TV_LastChatDate);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHoldListChat holder, int position) {
           Messenger messenger=messengerList.get(position);
           Bitmap bitmap= BitmapFactory.decodeByteArray(messenger.getAvata(),0,messenger.getAvata().length);
           holder.RIV_Avata.setImageBitmap(bitmap);
           holder.TV_AccountName.setText(messenger.getAccountName());
           holder.TV_Content.setText(messenger.getLastMessage());
           if(!messenger.isSeeMessage() && messenger.getUserIdSendMessage() != InfoMyAccount.getAccountId()){ // neu tin nhan do chua doc va k phai la cua chung ta
               holder.TV_Content.setTypeface(holder.TV_Content.getTypeface(), Typeface.BOLD);
               Log.d("TV_BOLD", "onBindViewHolder: ");
           }
           else{
               holder.TV_Content.setTypeface(null, Typeface.NORMAL);
               Log.d("TV_NORMAL"+messenger.isSeeMessage()+":"+messenger.getUserIdSendMessage(), "onBindViewHolder: ");


           }
        Log.d("ADT duoc goi", "onBindViewHolder: ");
        String[] spLastTime = messenger.getLastReceiveMessageDate().split(" ");
           holder.TV_LastChatDate.setText(spLastTime[0]);
           holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(context)){
                    Toast.makeText(context,"Kiểm tra kết nối Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a=new Intent(context, UserChat.class);
                a.putExtra("chatUserId",messenger.getUserId());
                context.startActivity(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("get item duoc chay", "getItemCount: ");
        return messengerList.size();
    }
}
class viewHoldListChat extends RecyclerView.ViewHolder{
    RoundedImageView RIV_Avata;
    TextView TV_AccountName;
    TextView TV_Content;
    TextView TV_LastChatDate;

    public viewHoldListChat(@NonNull View itemView, RoundedImageView RIV_Avata, TextView TV_AccountName, TextView TV_Content, TextView TV_LastChatDate) {
        super(itemView);
        this.RIV_Avata = RIV_Avata;
        this.TV_AccountName = TV_AccountName;
        this.TV_Content = TV_Content;
        this.TV_LastChatDate = TV_LastChatDate;
    }
}

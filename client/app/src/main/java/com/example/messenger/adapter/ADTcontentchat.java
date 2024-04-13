package com.example.messenger.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.model.ChatUserMessages;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Base64;

public class ADTcontentchat extends RecyclerView.Adapter<vhContentChat> {
    Context context;
    private final ArrayList<ChatUserMessages> listChat;
    private final String chatUserName;
    private final byte[] avatarUser;
    private final int userView = 0;
    private final int relationship;
    private final int chatViewByMySelf = 1;
    private final int chatViewByUser = 2;
    private final int ImageLikeByMySelt = 3;
    private final int ImageLikeByUser = 4;
    private final int imageViewByMySelt =5;
    private final int imageViewByUser =6;





    public ADTcontentchat(Context context, ArrayList<ChatUserMessages> listChat,String chatUserName ,byte[] avatar,int relationship) {
        this.chatUserName = chatUserName;
        this.avatarUser = avatar;
        this.context = context;
        this.listChat = listChat;
        this.relationship = relationship;
    }

    @NonNull
    @Override
    public vhContentChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == userView){
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.introduct_user, parent,false);
            return new vhContentChat(view);
        }
        // neu
        if(viewType==this.chatViewByUser) {
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.contentchat, parent,false);
            return new vhContentChat(view);
        }
        if(viewType==this.chatViewByMySelf) {
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.contentbymyself, parent,false);
            return new vhContentChat(view);
        }
        if(viewType==this.ImageLikeByMySelt){
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.image_like_myself, parent,false);
            return new vhContentChat(view);
        }
        if(viewType==this.ImageLikeByUser){
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.image_like_by_user, parent,false);
            return new vhContentChat(view);
        }
        if(viewType==this.imageViewByMySelt){
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.picture_by_myself, parent,false);
            return new vhContentChat(view);
        }
        if(viewType==this.imageViewByUser){
            View view = ((Activity) context).getLayoutInflater().inflate(R.layout.picture_by_user, parent,false);
            return new vhContentChat(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull vhContentChat holder, int position) {
        if(position == 0) {
            RoundedImageView RIV_userAvatar = holder.itemView.findViewById(R.id.RIV_userAvatar);
            RIV_userAvatar.setImageBitmap(BitmapFactory.decodeByteArray(avatarUser,0,avatarUser.length));
            TextView userName = holder.itemView.findViewById(R.id.TV_userName);
            userName.setText(this.chatUserName);
            TextView TV_relationship = holder.itemView.findViewById(R.id.TV_relationship);
            if(this.relationship == -1){
                TV_relationship.setText("Bạn hiện không thể liên lạc với người này");
            }
            if(this.relationship == 0 || this.relationship == 1 || this.relationship == 2){
                TV_relationship.setText("Các bạn hiện không phải là bạn bè");
            }
            if(this.relationship == 3){
                TV_relationship.setText("Các bạn là bạn bè trên ConversationApp");
            }
            return;
        }
        ChatUserMessages chat=listChat.get(position - 1);
        if(chat.getMessageType().equals("Text")){
            View view=holder.itemView;
            TextView TV_ChatContent=view.findViewById(R.id.chatContent);
            TV_ChatContent.setText(chat.getContent());
        }
        if(chat.getMessageType().equals("Image") || chat.getMessageType().equals("LikeImage") ){
            View view=holder.itemView;
            ImageView imageView = view.findViewById(R.id.IMG_ImageContent);
            String contentInBase64 = chat.getContent();
            byte[] content = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                content = Base64.getDecoder().decode(contentInBase64);
            }
            else content = android.util.Base64.decode(contentInBase64, android.util.Base64.DEFAULT);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(content,0,content.length));
        }

    }

    @Override
    public int getItemCount() {
        return listChat.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) return userView;
        ChatUserMessages messages=listChat.get(position-1);
        if(messages.getMessageType().equals("Text")){
            if(messages.getChatUserId()== InfoMyAccount.getAccountId()){
                return this.chatViewByMySelf;
            }
            return this.chatViewByUser;
        }

        if(messages.getMessageType().equals("LikeImage")){
            if(messages.getChatUserId()== InfoMyAccount.getAccountId()){
                return this.ImageLikeByMySelt;
            }
            return this.ImageLikeByUser;
        }

        if(messages.getMessageType().equals("Image")){
            if(messages.getChatUserId()== InfoMyAccount.getAccountId()){
                return this.imageViewByMySelt;
            }
            return this.imageViewByUser;
        }
        return super.getItemViewType(position);
    }
}
class vhContentChat extends  RecyclerView.ViewHolder{
    public vhContentChat(@NonNull View itemView) {
        super(itemView);
    }
}

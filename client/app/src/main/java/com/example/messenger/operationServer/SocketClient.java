package com.example.messenger.operationServer;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.Service.ServiceSendRetrieveMessage;
import com.example.messenger.model.ChatUserMessages;
import com.example.messenger.model.Messenger;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.model.UserPostVideo;
import com.example.messenger.ui.AllUserPicture;
import com.example.messenger.ui.App;
import com.example.messenger.ui.Fragments.Fragment_1;
import com.example.messenger.ui.Fragments.Fragment_2;
import com.example.messenger.ui.Fragments.Fragment_3;
import com.example.messenger.ui.MyActivityLifecycleCallbacks;
import com.example.messenger.ui.User;
import com.example.messenger.ui.UserChat;
import com.example.messenger.ui.LogIn;
import com.example.messenger.ui.UserPictureDetail;
import com.example.messenger.ui.UserPost;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;

public class SocketClient extends WebSocketClient {
    private String codeClient;
    private final Handler handler;

    public String getCodeClient() {
        return codeClient;
    }

    public SocketClient(String serverUri,Handler handler) throws URISyntaxException {
        super(new URI(serverUri));
        this.handler = handler;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("T0485845DIGS8D7GMD8XKGM7DEMGUFKG-"+ InfoMyAccount.getAccountId()); // duoc goi sau khi login thanh cong. yeu cau ket noi voi server
    }

    @Override
    public void onMessage(String message) {
        if(message.contains("8JUFKG9SKD8D8NGM7DE7N8DNMGD")){ // server chinh thuc xac nhan ket noi va tra ve mot ma code cho client
            Log.d("ma tu server",  message.split("-")[1]);
            this.codeClient = message.split("-")[1];
            return;
        }

        if(message.equals("8JNGM7DEMGUFKG9SKD7N8DN8D8D")){ // yeu cau thoat app vi co user khac login
            Log.d("co user khac login", "onMessage: ");
            if(MyActivityLifecycleCallbacks.currentActivity != null){
                Log.d("xoa file account", "onMessage: ");
                SharedPreferences sharedPreferences= MyActivityLifecycleCallbacks.currentActivity.getSharedPreferences("account", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyActivityLifecycleCallbacks.finish = true;
                    LogIn.requestFinish = false;
                    if ( MyActivityLifecycleCallbacks.currentActivity != null ) MyActivityLifecycleCallbacks.currentActivity.finish();
                    if(ServiceSendRetrieveMessage.service != null) ServiceSendRetrieveMessage.service.stopSelf();
                }
            });

            return;
        }
        // su li thong diep json  tra ve tu server
        try {
            JSONObject content = new JSONObject(message);
            if(content.getInt("type") == 1){  // type == 1 nghia la message tu mot nguoi dung gui den
                onHandleMessageFromServer(content);
            }
            if(content.getInt("type") == 2){  // type == 2 ppnghia la yêu cầu block
                onHandleEventUserBlock(content);
            }
            if(content.getInt("type") == 3){ // yeu cau xoa toan bo message
                onHandleEventDeleteAllMessage(content);
            }
            if(content.getInt("type") == 4 || content.getInt("type") == 5){ // có user thay đổi avatar hoặc tên tài khoản
                onHandleEventUserChangeInfoUser(content);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Được gọi khi kết nối đóng
    }

    @Override
    public void onError(Exception ex) {
        // Được gọi khi xảy ra lỗi trong quá trình xử lý WebSocket
        ex.printStackTrace();
    }
    public void onHandleMessageFromServer(JSONObject content) throws JSONException {
        Log.d("nhan message moi tu server", "onHandleMessageFromServer: ");
        int messageId = content.getInt("messageId");
        int sendUserId = content.getInt("sendUserId");
        String accountName = content.getString("accountName");
        boolean isFriend = content.getBoolean("isFriend");
        String contentChat = content.getString("content");
        String messageType = content.getString("messageType");
        String sendingTime = content.getString("sendingTime");
        String contentToFg1 = content.getString("contentToFg1");
        String avatarInBase64 = content.getString("avatarInBase64");

        // tien hanh su li trong luong ui
        handler.post(new Runnable() {
            @Override
            public void run() {
                showNotification(contentToFg1); // show lên thanh thông báo
                if(MyActivityLifecycleCallbacks.currentActivity == null) return;
                if(MyActivityLifecycleCallbacks.currentActivity instanceof UserChat){
                    UserChat activityCurrent = (UserChat)MyActivityLifecycleCallbacks.currentActivity;
                    if(activityCurrent.isOnResume()) {
                        if(sendUserId == activityCurrent.getChatUserId()) {
                            activityCurrent.getListMessage().add(new ChatUserMessages(messageId, sendUserId, messageType, contentChat));
                            activityCurrent.getADTContentchat().notifyDataSetChanged();
                        }
                    }
                }
                else if(MyActivityLifecycleCallbacks.currentActivity instanceof App){
                    App activityCurrent = (App)MyActivityLifecycleCallbacks.currentActivity;
                    if(activityCurrent.getCurrentFragmentInOnResume() instanceof Fragment_1){
                        Fragment_1 currentFragment = (Fragment_1) activityCurrent.getCurrentFragmentInOnResume();
                        // gui thong diep cho fragment 1 su li
                        List<Messenger> list =  ((Fragment_1) activityCurrent.getCurrentFragmentInOnResume()).messengerList;
                        for(int i = 0;i<list.size();i++){ // truong hop list co phan tu
                            if(list.get(i).getUserId() == sendUserId){
                                list.get(i).setLastMessage(contentToFg1);
                                list.get(i).setLastReceiveMessageDate(sendingTime);
                                list.get(i).setSeeMessage(false);
                                currentFragment.getADT_UserChat().notifyDataSetChanged();
                                break;
                            }
                            if(i == list.size() -1){ //
                                if(isFriend){
                                    byte[] avatarFriend;
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        avatarFriend = Base64.getDecoder().decode(avatarInBase64);
                                    } else avatarFriend = android.util.Base64.decode(avatarInBase64, android.util.Base64.DEFAULT);
                                    Messenger messenger = new Messenger(sendUserId,accountName,contentToFg1,messageType,sendingTime,avatarFriend,sendUserId,false); // bien userId dau tien la user id chat voi chung ta. bien thu 2 la userId gui tin nhan
                                    list.add(messenger);
                                    currentFragment.getADT_UserChat().notifyDataSetChanged();
                                }

                            }
                        }

                        if(list.size() == 0 && isFriend){ //
                                byte[] avatarFriend;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    avatarFriend = Base64.getDecoder().decode(avatarInBase64);
                                } else avatarFriend = android.util.Base64.decode(avatarInBase64, android.util.Base64.DEFAULT);
                                Messenger messenger = new Messenger(sendUserId,accountName,contentToFg1,messageType,sendingTime,avatarFriend,sendUserId,false); // bien userId dau tien la user id chat voi chung ta. bien thu 2 la userId gui tin nhan
                                list.add(messenger);
                                currentFragment.getADT_UserChat().notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    public void onHandleEventUserBlock(JSONObject content) throws JSONException {
        int userId = content.getInt("userId");
        // tien hanh su li trong luong ui
        handler.post(new Runnable() {
            @Override
            public void run() {
                Activity activityCurrent = MyActivityLifecycleCallbacks.currentActivity;
                if(activityCurrent == null) return;
                if (
                   (activityCurrent instanceof User && ((User)activityCurrent).getUserId() == userId )
                || ( activityCurrent instanceof UserPost && ((UserPost)activityCurrent).getUserId() == userId )
                || ( activityCurrent instanceof AllUserPicture && ((AllUserPicture)activityCurrent).getUserId() == userId )
                || ( activityCurrent instanceof UserPictureDetail && ((UserPictureDetail)activityCurrent).userId == userId )
                ) {
                          Toast.makeText(activityCurrent,"Bạn không có quyền truy cập vào tài khoản này",Toast.LENGTH_LONG).show();
                          activityCurrent.finish();
                }
                if(activityCurrent instanceof UserChat) {
                    if (((UserChat) activityCurrent).getChatUserId() == userId) {
                        activityCurrent.findViewById(R.id.CL_notificationBlocked).setVisibility(View.VISIBLE);
                        activityCurrent.findViewById(R.id.constraintLayout).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void onHandleEventDeleteAllMessage(JSONObject content) throws JSONException {
        int userId = content.getInt("userId");
        handler.post(new Runnable() {
            @Override
            public void run() {
                Activity activityCurrent = MyActivityLifecycleCallbacks.currentActivity;
                if(activityCurrent == null) return;
                if(activityCurrent instanceof UserChat) {
                    if (((UserChat) activityCurrent).getChatUserId()== userId) {
                        ((UserChat) activityCurrent).getListMessage().clear();
                        ((UserChat)activityCurrent).getADTContentchat().notifyDataSetChanged();
                    }
                }
                else if(activityCurrent instanceof App && ((App)activityCurrent).getCurrentFragmentInOnResume() instanceof Fragment_1) {
                    Fragment fragment = ((App)activityCurrent).getCurrentFragmentInOnResume();
                    Fragment_1 fragment1 = (Fragment_1)fragment;
                    List<Messenger> list =  fragment1.messengerList;
                    for(int i = 0;i<list.size();i++){
                        if(list.get(i).getUserId() == userId){
                            list.remove(i);
                            fragment1.getADT_UserChat().notifyDataSetChanged();
                            break;
                    }
                }
            }
        }
        });
    }
    private void showNotification(String message) {
        Log.d("show thong bao", "showNotification: ");
        NotificationManager notificationManager = (NotificationManager) ServiceSendRetrieveMessage.service.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo một Notification Channel nếu thiết bị chạy Android 8.0 trở lên.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "Your Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(ServiceSendRetrieveMessage.service, "1")
                    .setContentTitle("Bạn có tin nhắn mới")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.icon_chat_tab_fg1).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        else {
            builder = new Notification.Builder(ServiceSendRetrieveMessage.service)
                    .setContentTitle("Bạn có tin nhắn mới")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.icon_chat_tab_fg1);
        }
        notificationManager.notify(1,builder.build());
    }
    private void onHandleEventUserChangeInfoUser(JSONObject content) throws JSONException{
        int type = content.getInt("type");
        int userId = content.getInt("userId");

        byte[] avatarUser = null;
        String accountName = null;
        if(type == 4) accountName =content.getString("accountName");
        if(type == 5){
            // chuyển avatar từ base64 thành byte[]
            String avatarInBase64 = content.getString("avatarUser");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                avatarUser = Base64.getDecoder().decode(avatarInBase64);
            } else avatarUser = android.util.Base64.decode(avatarInBase64, android.util.Base64.DEFAULT);
        }
        byte[] finalAvatarUser = avatarUser;
        String finalAccountName = accountName;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Activity activityCurrent = MyActivityLifecycleCallbacks.currentActivity;
                if(activityCurrent instanceof App && ((App)activityCurrent).getCurrentFragmentInOnResume() instanceof Fragment_1) {
                    Fragment fragment = ((App)activityCurrent).getCurrentFragmentInOnResume();
                    Fragment_1 fragment1 = (Fragment_1)fragment;
                    List<Messenger> list1 =  fragment1.messengerList;
                    List<GeneralUserInfomation> list2 = fragment1.getOnlineFriendList();
                    for(int i = 0;i<list1.size();i++){
                        if(list1.get(i).getUserId() == userId){
                            if(type == 4) list1.get(i).setAccountName(finalAccountName);
                            if(type == 5) list1.get(i).setAvata(finalAvatarUser);
                            fragment1.getADT_UserChat().notifyDataSetChanged();
                            break;
                        }
                    }
                    for(int i = 0;i<list2.size();i++){
                        if(list2.get(i).getIdUser() == userId){
                            if(type == 4 ) list2.get(i).setAccountName(finalAccountName);
                            if(type == 5) list2.get(i).setAvatar(finalAvatarUser);
                            fragment1.getADT_UserChat().notifyDataSetChanged();
                            break;
                        }
                    }
                }
                if(activityCurrent instanceof App && ((App)activityCurrent).getCurrentFragmentInOnResume() instanceof Fragment_2) {
                    Fragment fragment = ((App)activityCurrent).getCurrentFragmentInOnResume();
                    Fragment_2 fragment2 = (Fragment_2)fragment;
                    List<GeneralUserInfomation> list = fragment2.getOnlineFriendList();
                    for(int i = 0;i<list.size();i++){
                        if(list.get(i).getIdUser() == userId){
                            if(type == 4) list.get(i).setAccountName(finalAccountName);
                            if(type == 5) list.get(i).setAvatar(finalAvatarUser);
                            fragment2.getADT_AllFriendOnline().notifyDataSetChanged();
                            break;
                        }
                    }
                }
                // đã sử lí xong fg1 va fg2// chỉ còn lại fg3
                if(activityCurrent instanceof App && ((App)activityCurrent).getCurrentFragmentInOnResume() instanceof Fragment_2) {
                    Fragment fragment = ((App)activityCurrent).getCurrentFragmentInOnResume();
                    Fragment_3 fragment3 = (Fragment_3)fragment;
                    List<UserPostVideo> list = fragment3.getPostList();
                    for(int i = 0;i<list.size();i++){
                        if(list.get(i).getIdUser() == userId){
                            if(type == 4) list.get(i).setAccountName(finalAccountName);
                            if(type == 5) list.get(i).setAvatar(finalAvatarUser);
                            fragment3.getADTTin().notifyDataSetChanged();
                            break;
                        }
                    }
                }

            }
        });
    }

}

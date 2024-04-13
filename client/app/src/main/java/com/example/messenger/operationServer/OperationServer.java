package com.example.messenger.operationServer;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.model.ChatUserMessages;
import com.example.messenger.model.Messenger;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.model.MyPost;
import com.example.messenger.model.MyPrivacyInfo;
import com.example.messenger.model.UserPicture;
import com.example.messenger.model.UserPostVideo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
public class OperationServer {
    public static String ServerHttpPath = "http://192.168.75.1:8080/projectMessenger";

    ////////////////////////////////////// lấy thông tin chỉ riêng của user.
    public byte[] getAvatar(int userId) {
        byte[][] result = new byte[1][];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    os.write(("userId="+userId+"&type=1").getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    // chuyển về dạng byte từ base64
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        result[0] = Base64.getDecoder().decode(content);
                    } else result[0] = android.util.Base64.decode(content, android.util.Base64.DEFAULT);
                }
                catch(Exception e) {}
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }
    public byte[] getCoverPicture(int userId) {
        List<byte[]> list = new ArrayList<>();
        try {
            Thread thread = new Thread(() -> {
                try {
                    HttpURLConnection httpURLConnection = new PrepareConnectServer().getHttpUrlConnect(OperationServer.ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = httpURLConnection.getOutputStream();
                    String PARAMS = "userId=" + userId+"&type=2";
                    os.write(PARAMS.getBytes());
                    os.flush();
                    httpURLConnection.getResponseCode();
                    ///
                    String content = new ServerContent().getServerJsonContent(httpURLConnection.getInputStream());
                    // du lieu gui ve la dang stringbase64
                    byte[] avataImage = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        avataImage = Base64.getDecoder().decode(content);
                    } else {
                        avataImage = android.util.Base64.decode(content, android.util.Base64.DEFAULT);
                    }
                    list.add(avataImage);
                } catch (Exception e) {
                    list.clear();
                }
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
        }
        return list.size() > 0 ? list.get(0) : null;
    }
    public String getIntroducUser(int userId){
        String[] result = new String[]{""};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=3";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    result[0] = new ServerContent().getServerJsonContent(connection.getInputStream());
                    // dữ liệu trả vê là 1 string thuần chứa nội dung.
                }
                catch(Exception e){
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];
    }
    public List<GeneralUserInfomation> getUserBlocked(int userId){
        List<GeneralUserInfomation> list = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection =   new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=4";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    JSONArray jsonArray = new JSONArray(content);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int userId = jsonObject.getInt("userId");
                        String accountName = jsonObject.getString("accountName");
                        String avatarInBase64 = jsonObject.getString("avatarInBase64");
                        byte[] avatar = changeBase64ImageToByteArray(avatarInBase64);
                        list.add(new GeneralUserInfomation(userId,avatar,accountName));
                    }
                }
                catch(Exception e){
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return list.size() > 0 ? list : null;

    }
    public int getShowStates(int userId){
        int[] result = new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=5";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    Log.d("thong diep2 :", content);
                    result[0] = Integer.parseInt(content);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }
    public int getStatesInteractiveUser(int userId){
        int[] result = new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=6";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    result[0]= Integer.parseInt(content);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }
    public List<byte[]> getTopPictureByUserId(int userId,int amount){
        List<byte[]> list = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId=" + userId+"&type=7&amount="+amount;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream()); //
                    // dang doc luong stream theo dinh dang urf 8 -> vì server gui luong byte ve theo dinh dang urf 8
                    JSONArray jsonArray = new JSONArray(jsonData);
                    // Lặp qua các đối tượng JSON trong mảng
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Lấy thông tin từ đối tượng JSON
                        String PictureInBase64Format = jsonObject.getString("pictureInBase64Format");
                        byte[] avatarByte = changeBase64ImageToByteArray(PictureInBase64Format);
                        list.add(avatarByte);
                    }
                }
                catch(Exception e){
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return list.size() > 0 ? list : null;

    }
    public MyPrivacyInfo getMyPrivacyInfo() {
        MyPrivacyInfo[] myPrivacyInfo = new MyPrivacyInfo[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath+ "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String PARAMS = "userId=" + InfoMyAccount.getAccountId()+"&type=8";
                    os.write(PARAMS.getBytes());
                    os.flush();
                    Log.d("lay thong tin rieng tu account", "run: ");
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream());
                    // doc thong tin tu server
                    JSONObject jsonObject = new JSONObject(jsonData);
                    // doi tuong json nay se tra ve 5 thong tin qua trong
                    // 1 string : ten. 1 int gioi tinh - 1 string con lai - ngay thang nam sinh
                    // 2 thong tin con lai la anh dai dien va anh bia se duoc lay tu luc login
                    String myName = jsonObject.getString("userName");
                    int sex = jsonObject.getInt("sex");
                    String birthDay = jsonObject.getString("birthday");
                    myPrivacyInfo[0] =new MyPrivacyInfo(myName,sex,birthDay);
                }
                catch(Exception e) {}
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return myPrivacyInfo[0];
    }

    public String getAccountName(int userId){
        String[] result = new String[]{""};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=9";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    result[0] = new ServerContent().getServerJsonContent(connection.getInputStream());
                    // dữ liệu trả vê là 1 string thuần chứa nội dung.
                }
                catch(Exception e){
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];
    }
    public List<MyPost> getMyPost(int userId){
        List<MyPost> result = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=10";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    if(content.length() != 0) { // co phan tu
                       JSONArray jsonArray = new JSONArray(content);
                       for(int i = 0;i< jsonArray.length();i++){
                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                           String path = jsonObject.getString("path");
                           String postInBase64 = jsonObject.getString("postInBase64");
                           byte[] post;
                           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                               post = Base64.getDecoder().decode(postInBase64);
                           } else post = android.util.Base64.decode(postInBase64, android.util.Base64.DEFAULT);
                           result.add(new MyPost(post,path));
                       }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.size() > 0 ? result : null;
    }
    public List<UserPicture> getAllPictureByUserId(int userId){
        List<UserPicture> list = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetGeneralUserInformation", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId=" + userId+"&type=11";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream()); //
                    // dang doc luong stream theo dinh dang urf 8 -> vì server gui luong byte ve theo dinh dang urf 8

                    JSONArray jsonArray = new JSONArray(jsonData);
                    // Lặp qua các đối tượng JSON trong mảng
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Lấy thông tin từ đối tượng JSON
                        int userId = jsonObject.getInt("userId");
                        int pictureId = jsonObject.getInt("pictureId");
                        String PictureInBase64Format = jsonObject.getString("pictureInBase64Format");
                        byte[] picture = changeBase64ImageToByteArray(PictureInBase64Format);
                        list.add(new UserPicture(userId,picture,pictureId));
                    }
                }
                catch(Exception e){
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return list.size() > 0 ? list : null;
    }


    /////////////////////////////// cac message tương tác cua 2 hoac nhieu user
    public List<ChatUserMessages> getMessageBetweenTwoUser(int UserID, int chatUserID){
        List<ChatUserMessages> list=new ArrayList<>();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpURLConnection connection=new PrepareConnectServer().getHttpUrlConnect(OperationServer.ServerHttpPath + "/GetMessage","POST");
                    OutputStream os = connection.getOutputStream();
                    String PARAMS = "userId=" +UserID+"&user2Id="+chatUserID+"&type=2";
                    os.write(PARAMS.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    ////////////////////////////////////////////////////
                    InputStream is = connection.getInputStream();
                    String chuoinhantumaychu = new ServerContent().getServerJsonContent(is);
                    // Log.d("chuoi tu server", chuoinhantumaychu.length()+"");
                    JSONArray jsonArray=new JSONArray(chuoinhantumaychu);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        int messageId = jsonObject.getInt("messageId");
                        int chatUserId=jsonObject.getInt("senderId");
                        String messageType=jsonObject.getString("messageType");
                        String Content=jsonObject.getString("content");
                        ChatUserMessages chatUserMessages=new ChatUserMessages(messageId,chatUserId,messageType,Content);
                        list.add(chatUserMessages);
                    }
                }catch(Exception e){
                    Log.d("ngoai le khi lay message", e.getMessage());
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {}
        return list;
    }
    public ArrayList<Messenger> getMessengerLastForEachUser(int userId) {
        ArrayList<Messenger> MessengerArrayList = new ArrayList<>();
        try {
            Thread thread = new Thread(() -> {
                try {
                    HttpURLConnection httpURLConnection = new PrepareConnectServer().getHttpUrlConnect(OperationServer.ServerHttpPath + "/GetMessage", "POST");
                    OutputStream os = httpURLConnection.getOutputStream();
                    String PARAMS = "userId=" + userId + "&type=1";
                    os.write(PARAMS.getBytes());
                    os.flush();
                    httpURLConnection.getResponseCode();
                    ///
                    InputStream is = httpURLConnection.getInputStream();
                    String jsonData = new ServerContent().getServerJsonContent(is);
                    JSONArray jsonArray = new JSONArray(jsonData);
                    // Lặp qua các đối tượng JSON trong mảng
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Lấy thông tin từ đối tượng JSON
                        int chatUserId = Integer.parseInt(jsonObject.getString("chatUserId"));
                        String avataText = jsonObject.getString("avatar");
                        String userAccountName = jsonObject.getString("userAccountName");
                        String lastTimeChat = jsonObject.getString("lastTimeChat");
                        String lastMessageType = jsonObject.getString("lastMessageType");
                        String lastMessageContent = jsonObject.getString("lastMessageContent"); // luu y rang noi dung o day se gui vao fragment 1. nen noi dung la text tieng viet da gui ve tu server. k phai base64
                        int userIdSendMessage = jsonObject.getInt("userIdSendMessage");
                        boolean seeMessage = jsonObject.getInt("seeMessage") == 1;
                        byte[] avataImage = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            avataImage = Base64.getDecoder().decode(avataText);
                        } else {
                            avataImage = android.util.Base64.decode(avataText, android.util.Base64.DEFAULT);
                        }
                        Log.d("9999", lastMessageContent);
                        Messenger messenger = new Messenger(chatUserId, userAccountName, lastMessageContent, lastMessageType, lastTimeChat, avataImage,userIdSendMessage,seeMessage);
                        MessengerArrayList.add(messenger);
                    }
                } catch (Exception e) {
                }
            });

            thread.start();
            thread.join();
        } catch (Exception e) {
        }
        return MessengerArrayList;
    }
    public List<Messenger> getMessengerWating(int userId) {
        List<Messenger> MessengerArrayList = new ArrayList<>();
        try {
            Thread thread = new Thread(() -> {
                try {
                    HttpURLConnection httpURLConnection = new PrepareConnectServer().getHttpUrlConnect(OperationServer.ServerHttpPath + "/GetMessage", "POST");
                    OutputStream os = httpURLConnection.getOutputStream();
                    String PARAMS = "userId=" + userId+"&type=3";
                    os.write(PARAMS.getBytes());
                    os.flush();
                    httpURLConnection.getResponseCode();
                    ///
                    InputStream is = httpURLConnection.getInputStream();
                    String jsonData = new ServerContent().getServerJsonContent(is);
                    JSONArray jsonArray = new JSONArray(jsonData);
                    // Lặp qua các đối tượng JSON trong mảng
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Lấy thông tin từ đối tượng JSON
                        int chatUserId = Integer.parseInt(jsonObject.getString("chatUserId"));
                        String avatarText = jsonObject.getString("avatar");
                        String userAccountName = jsonObject.getString("userAccountName");
                        String lastTimeChat = jsonObject.getString("lastTimeChat");
                        String lastMessageType = jsonObject.getString("lastMessageType");
                        String lastMessageContent = jsonObject.getString("lastMessageContent"); // luu y rang noi dung o day se gui vao fragment 1. nen noi dung la text tieng viet da gui ve tu server. k phai base64
                        int userIdSendMessage = jsonObject.getInt("userIdSendMessage");
                        byte[] avataImage = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            avataImage = Base64.getDecoder().decode(avatarText);
                        } else {
                            avataImage = android.util.Base64.decode(avatarText, android.util.Base64.DEFAULT);
                        }
                        Log.d("9999", lastMessageContent);
                        Messenger messenger = new Messenger(chatUserId, userAccountName, lastMessageContent, lastMessageType, lastTimeChat, avataImage,userIdSendMessage,false);
                        MessengerArrayList.add(messenger);
                    }
                    Log.d("message wating size :", String.valueOf(MessengerArrayList.size()));
                } catch (Exception e) {
                    Log.d("ngoai le :", e.getMessage());
                }
            });

            thread.start();
            thread.join();
        } catch (Exception e) {
        }
        return MessengerArrayList;
    }


    /////////////////////////////// thông tin tương tác 2 hoặc nhiều user
    public List<GeneralUserInfomation> getAllOnlineFriend(int userId){
    // ham nay se lay danh sach 20 ban be online//
    // -> voi dieu kien la 20 ban be online voi nhieu tin nhan tuong tac gui di nhat
    List<GeneralUserInfomation> list = new ArrayList<>();
    Thread a = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                OutputStream os = connection.getOutputStream();
                String Param = "userId="+userId+"&type=1";
                os.write(Param.getBytes());
                os.flush();
                connection.getResponseCode();
                String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream());
                JSONArray jsonArray = new JSONArray(jsonData);
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int friendId=jsonObject.getInt("friendId");
                    String Base64avatarImage = jsonObject.getString("avatar");
                    byte[] avatar = changeBase64ImageToByteArray(Base64avatarImage);
                    String accountName = jsonObject.getString("accountName");
                    GeneralUserInfomation a = new GeneralUserInfomation(friendId,avatar,accountName);
                    list.add(a);
                }
            }
            catch(Exception e) {
                list.clear();
            }
        }
    });
    a.start();
    try {
        a.join();
    } catch (InterruptedException e) {
    }
    return list.size() > 0 ? list : null;
}
    public List<GeneralUserInfomation> getOnlineChatUser(int userId){
        List<GeneralUserInfomation> list = new ArrayList<>();
        Thread a = null;
        a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath+ "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=2";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream());
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int friendId=jsonObject.getInt("userId");
                        String Base64avatarImage = jsonObject.getString("avatar");
                        byte[] avatar = changeBase64ImageToByteArray(Base64avatarImage);
                        String accountName = jsonObject.getString("accountName");
                        GeneralUserInfomation a = new GeneralUserInfomation(friendId,avatar,accountName);
                        list.add(a);
                    }
                }
                catch(Exception e) {
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list.size() > 0 ? list : null;
    }
    public List<GeneralUserInfomation> getFriendRequest(int userId, int codeRequest){
        List<GeneralUserInfomation> list = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = null;
                    String Param = null;
                    if(codeRequest == 0) {
                        connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                        Param = "userId="+userId+"&type=4";

                    }
                    if(codeRequest == 1) {
                        connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                        Param = "userId="+userId+"&type=3";
                    }
                    OutputStream os = connection.getOutputStream();
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    JSONArray jsonArray = new JSONArray(content);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int userId = jsonObject.getInt("userId");
                        String accountName = jsonObject.getString("accountName");
                        String avatarInBase64 = jsonObject.getString("avatarInBase64");
                        byte[] avatar = changeBase64ImageToByteArray(avatarInBase64);
                        list.add(new GeneralUserInfomation(userId,avatar,accountName));
                    }
                }
                catch(Exception e){
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return list.size() > 0 ? list : null;

    }
    public int getMutualFriends(int userId, int user2Id) {
        int[] result = new int[]{0};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&user2Id="+user2Id+"&type=5";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    result[0] = Integer.parseInt(content);
                }
                catch(Exception e){
                    result[0] = -2;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];
    }
    public List<GeneralUserInfomation> findUserByName(String userAccountName){
        List<GeneralUserInfomation> list = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+InfoMyAccount.getAccountId()+"&type=6"+"&userAccountName="+userAccountName;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream()); //
                    // dang doc luong stream theo dinh dang urf 8 -> vì server gui luong byte ve theo dinh dang urf 8

                    JSONArray jsonArray = new JSONArray(jsonData);
                    // Lặp qua các đối tượng JSON trong mảng
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // Lấy thông tin từ đối tượng JSON
                        int UserId = jsonObject.getInt("userId");
                        String UserAccountName = jsonObject.getString("userAccountName");
                        String avatarInBase64Format = jsonObject.getString("avatarInBase64Format");
                        byte[] avatarByte = changeBase64ImageToByteArray(avatarInBase64Format);
                        GeneralUserInfomation user = new GeneralUserInfomation(UserId,avatarByte,UserAccountName);
                        list.add(user);
                    }
                }
                catch(Exception e){
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return list.size() > 0 ? list : null;

    }
    public int checkRelationshipUser(int userId, int user2Id) {
        int[] result = new int[]{-1};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&user2Id="+user2Id+"&type=7";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    result[0] = Integer.parseInt(content);
                }
                catch(Exception e){
                    result[0] = -1;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];
    }
    public List<GeneralUserInfomation> getFriend(int userId,int amount){
        List<GeneralUserInfomation> list = new ArrayList<>();
        Thread a = null;
        a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&amount="+amount+"&type=8";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream());
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int friendId=jsonObject.getInt("friendId");
                        String Base64avatarImage = jsonObject.getString("avatar");
                        byte[] avatar = changeBase64ImageToByteArray(Base64avatarImage);
                        String accountName = jsonObject.getString("accountName");
                        GeneralUserInfomation a = new GeneralUserInfomation(friendId,avatar,accountName);
                        list.add(a);
                    }
                }
                catch(Exception e) {
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list.size() > 0 ? list : null;
    }
    public int getUserAmountFollowByUserId(int userId){
        int[] amountFollow = new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId=" + userId+"&type=9";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream()); // // thực ra ket qua chi la 1 dong va k phai json
                    // dang doc luong stream theo dinh dang urf 8 -> vì server gui luong byte ve theo dinh dang urf 8
                    int amount = Integer.parseInt(jsonData); // vi ket qua tra ve la 1 con so duy nhat the hien duoi dang string
                    amountFollow[0] = amount;
                }
                catch(Exception e){
                    amountFollow[0] = 0;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return amountFollow[0];

    }
    public int getAmountFriends(int userId) {
        int[] result = new int[]{0};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&type=10";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    result[0] = Integer.parseInt(content);
                }
                catch(Exception e){
                    result[0] = -2;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];
    }
    public List<UserPostVideo> getUserPostVideos(int accountId){
        List<UserPostVideo> list = new ArrayList<>();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/GetUserInteraction", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+accountId+"&type=11";
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                    String jsonData = new ServerContent().getServerJsonContent(connection.getInputStream());
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int userId=jsonObject.getInt("userId");
                        String avatarInBase64 = jsonObject.getString("avatarInBase64");
                        byte[] avatar = changeBase64ImageToByteArray(avatarInBase64);
                        String accountName = jsonObject.getString("accountName");
                        String postInBase64 = jsonObject.getString("postInBase64");
                        byte[] post = changeBase64ImageToByteArray(postInBase64);

                        list.add(new UserPostVideo(userId,avatar,accountName,post));
                    }
                }
                catch(Exception e) {
                    list.clear();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list.size() > 0 ? list : null;
    }



  ////////////////////////////////////////  //tuong tac thao tac them sua xoa

    public boolean acceptFriendRequest(int userId,int user2Id){
        boolean[] result = new boolean[]{false};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/AcceptFriendRequest", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&user2Id="+user2Id;
                    os.write(Param.getBytes());
                    os.flush();
                    result[0] = connection.getResponseCode() == 200;
                }
                catch(Exception e){
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];

    }
    public boolean blockUser(int userId,int userIdToBlock){
        int[] resutl = new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/BlockUser", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&userIdToBlock="+userIdToBlock;
                    os.write(Param.getBytes());
                    os.flush();
                    resutl[0] = connection.getResponseCode();
                }
                catch(Exception e){
                    resutl[0] = 505;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return resutl[0] == 200;

    }
    public boolean cancelRequestFriend(int userId,int user2Id){
        boolean[] result = new boolean[]{false};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/CancelRequestFriend", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&user2Id="+user2Id;
                    os.write(Param.getBytes());
                    os.flush();
                    result[0] = connection.getResponseCode() == 200;
                }
                catch(Exception e){
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];

    }
    public void deleteAllMesssage(int userId,int userIdToDelete){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String Param = "userId="+userId+"&userIdToDelete="+userIdToDelete;
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/DeleteAllMesssage?"+Param, "DELETE");
                    connection.getResponseCode();
                }
                catch(Exception e){
                }
            }
        });
        a.start();
    }
    public boolean deleteBlockUser(int userId,int userBlocked){
        boolean[] result = new boolean[]{false};
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/DeleteBlockUser", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&userBlocked="+userBlocked;
                    os.write(Param.getBytes());
                    os.flush();
                    result[0] = connection.getResponseCode() == 200;
                }
                catch(Exception e){
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];

    }
    public boolean deleteFriend(int userId,int userIdToDelete){
        int[] resutl = new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String Param = "userId="+userId+"&userIdToDelete="+userIdToDelete;
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/DeleteFriend?"+Param, "DELETE");
                    resutl[0] = connection.getResponseCode();
                }
                catch(Exception e){
                    resutl[0] = 503;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return resutl[0] == 200;

    }
    public int sendMessageToServer(String content, int type, int userSendId, int userReceiveId){
        int[] result = new int[]{-1};
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpURLConnection connection=new PrepareConnectServer().getHttpUrlConnect(OperationServer.ServerHttpPath + "/MessageSentByClient","POST");
                    OutputStream os = connection.getOutputStream();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userSendId",userSendId);
                    jsonObject.put("userReceiveId",userReceiveId);
                    jsonObject.put("typeContent",type);
                    jsonObject.put("content",content);
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    int status = connection.getResponseCode();
                    if(status == 200){
                        // lay ra id tra ve tu server
                        String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                        Log.d("ma tu server:"+status, "run: ");
                        Log.d("content tra ve:"+content, "run: ");
                        result[0] = Integer.parseInt(content);
                    }
                }catch(Exception e){
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {}
        return result[0];
    }
    public boolean sendRequestFriendToUser(int userIdSend,int userIdReceive){
        boolean[] result = new boolean[]{false};
        Thread a = null;
        a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/SendRequestFriendToUser", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userIdSend="+userIdSend+"&userIdReceive="+userIdReceive;
                    os.write(Param.getBytes());
                    os.flush();
                    result[0]= connection.getResponseCode() == 200;

                }
                catch(Exception e) {
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }
    public void sendPostToFriend(int userIdSend,byte[] file,int type){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/AddPostUser", "POST");
                    OutputStream os = connection.getOutputStream();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId",userIdSend);
                    // chuyển sang base64;
                    String fileInBase64 = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fileInBase64 = Base64.getEncoder().encodeToString(file);
                    } else fileInBase64 = android.util.Base64.encodeToString(file, android.util.Base64.DEFAULT);
                   jsonObject.put("fileInBase64",fileInBase64);
                    jsonObject.put("type",type);
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    connection.getResponseCode();
                }
                catch(Exception e) {
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void deletePost(String pathAtServer){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/DeleteProfile24HPostAndProfilePost", "POST");
                    OutputStream os = connection.getOutputStream();
                    os.write(("pathAtServer="+pathAtServer).getBytes());
                    os.flush();
                    connection.getResponseCode();
                }
                catch(Exception e) {
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    ////////////////////// cac hoat dong voi account
    public int addAccount(String nameAccountLogin, String passwordLogin){
        int[] result= new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/AddAccount", "POST");
                        OutputStream os = connection.getOutputStream();
                        String Param = "userName=" + nameAccountLogin + "&password=" + passwordLogin;
                        os.write(Param.getBytes());
                        os.flush();
                        result[0] = connection.getResponseCode() == 200 ? 1 : 0;
                    } catch (Exception e) {
                        result[0] = -1;
                    }
                }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {}
        return result[0];
    }
    public void changeAvatar(byte [] avata,String fileType) {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/ChangeAvata", "POST");
                    OutputStream os = connection.getOutputStream();
                    ////////
                    //  String PARAMS = "userId=" + infoMyAccount.getAccountId();
                    //  os.write(PARAMS.getBytes());
                    // tiep theo chung ta se lay ra chuoi text dang base64 cua duong dan file tren
                    String imageText = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        imageText = Base64.getEncoder().encodeToString(avata);
                    }
                    else{
                        imageText = android.util.Base64.encodeToString(avata, android.util.Base64.DEFAULT);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", InfoMyAccount.getAccountId());
                    jsonObject.put("fileType",fileType);
                    jsonObject.put("avataText",imageText);
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    connection.getResponseCode();
                }
                catch(Exception e) {}
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void changeInteractiveUser(int userId, int interactiveUser){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/ChangeInteractiveUser", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&interactiveUser="+interactiveUser;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
    }
    public int changePassword(int userId,String password,String newPassword){
        int[] result = new int[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/ChangePassword", "POST");
                    OutputStream os = connection.getOutputStream();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId",userId);
                    jsonObject.put("password",password);
                    jsonObject.put("newPassword",newPassword);
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    connection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(connection.getInputStream());
                    result[0] = Integer.parseInt(content);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];

    }
    public void changePrivacyInfo(int userId, String myName,String sex,String birthday,String accountName) {
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/ChangePrivacyInfo", "POST");
                    OutputStream os = connection.getOutputStream();

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId",userId);
                    jsonObject.put("myName",myName);
                    jsonObject.put("sex",sex);
                    jsonObject.put("birthday",birthday);
                    jsonObject.put("accountName",accountName);
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    /////////////
                    os.flush();
                    connection.getResponseCode();
                }
                catch(Exception e) {
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
        }
    }
    public void changeShowStates(int userId,int state){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/ChangeShowStates", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&state="+state;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
    }
    // return value that is userId of user. return -1 if account don't exits
    public int checkLogin(String userNameLogin,String passwordLogin){
        int[] result = new int[]{-1}; // ma -1 là co lỗi trong việc connect server, 0 là login fail. 1 là thành công
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection= new PrepareConnectServer().getHttpUrlConnect(OperationServer.ServerHttpPath + "/Login","POST");
                    OutputStream os=httpURLConnection.getOutputStream();
                    String PARAMS="userNameLogin="+userNameLogin+"&"+"passwordLogin="+passwordLogin;
                    os.write(PARAMS.getBytes());
                    os.flush();
                    httpURLConnection.getResponseCode();
                    String content = new ServerContent().getServerJsonContent(httpURLConnection.getInputStream());
                    result[0] = Integer.parseInt(content);
                }
                catch (Exception e){
                    result[0] = -1;
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result[0];
    }
    public void logout(int userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/Logout", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                }
                catch(Exception e){
                }
            }
        }).start();
    }
    public byte[] changeBase64ImageToByteArray(String Base64Image){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getDecoder().decode(Base64Image);
        }
        else{
            return android.util.Base64.decode(Base64Image, android.util.Base64.DEFAULT);
        }
    }
    public void changeIntroduce(int userId,String newIntroduce){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/ChangeIntroduce", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId+"&newIntroduce="+newIntroduce;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        a.start();
    }
    public void sendStateToServer(int userId){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/StateOnlineClient", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "userId="+userId;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        a.start();
    }
    public void sendMyProfilePicture(int userId,String picture){
       Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/MyPicture", "POST");
                    OutputStream os = connection.getOutputStream();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId",userId);
                    jsonObject.put("picture",picture);
                    os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    connection.getResponseCode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
       a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void deletePicture(int pictureId){
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/DeleteUserPicture", "POST");
                    OutputStream os = connection.getOutputStream();
                    String Param = "pictureId="+pictureId;
                    os.write(Param.getBytes());
                    os.flush();
                    connection.getResponseCode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /////////////////////////////////////// hoat dong ket noi phien session socket
    public boolean checkAllowEstablishNewSession(int userId,String clientCode){
        boolean[] result= new boolean[1];
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1;i<=10;i++) {
                    try {
                        HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/CheckAllowNewSession", "POST");
                        OutputStream os = connection.getOutputStream();
                        String Param = "userId=" + userId + "&clientCode=" + clientCode;
                        os.write(Param.getBytes());
                        os.flush();
                        int ketqua = connection.getResponseCode();
                        result[0] = (ketqua == 200);
                        break;
                    } catch (Exception e) {
                        Log.d("ngoaile001", "");
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        if(i == 10) result[0] = false;
                    }
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException ee) {
        }
        return result[0];
    }

    //
    public void savePostFromServer(String path){
        String outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/aipzusrygzpziyopaie2os.mp4";
        File file = new File(outputPath); if(file.exists()) file.delete();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = new PrepareConnectServer().getHttpUrlConnect(ServerHttpPath + "/SendPostToClient", "POST");
                    OutputStream os = connection.getOutputStream();
                    os.write(("path="+path).getBytes());
                    os.flush();
                    connection.getResponseCode();
                    FileOutputStream fos = new FileOutputStream(outputPath);
                    InputStream is = connection.getInputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.close();
                }
                catch(Exception e) {
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

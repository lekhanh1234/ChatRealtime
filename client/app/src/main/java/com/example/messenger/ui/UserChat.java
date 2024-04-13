package com.example.messenger.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTcontentchat;
import com.example.messenger.model.ChatUserMessages;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserChat extends AppCompatActivity {
    private boolean onResume = false;

    public boolean isOnResume() {
        return onResume;
    }

    private String chatUserName;
    private int chatUserId;

    public int getChatUserId() {
        return chatUserId;
    }

    private int relationship;
    private ConstraintLayout CL_outSideEDT;
    private ImageButton IMG_sendMessage;
    private List<ChatUserMessages> listMessage = new ArrayList<>();

    public List<ChatUserMessages> getListMessage() {
        return listMessage;
    }

    private ADTcontentchat ADTContentchat;

    public ADTcontentchat getADTContentchat() {
        return ADTContentchat;
    }

    private ImageButton IMG_picture_gallary;
    private EditText EDT_chat;
    private RecyclerView doanchat;
    TextView txt_nameFriendChat;
    RoundedImageView RIV_avataUser;
    private ImageButton BTN_Call;
    private ImageButton BTN_CallVideo;



    public static List<String> getSelectGallaryPicture() {
        return selectGallaryPicture;
    }

    private static final List<String> selectGallaryPicture = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        CL_outSideEDT = findViewById(R.id.CL_outSideEDT);
        IMG_picture_gallary = findViewById(R.id.IMG_picture_gallary);
        IMG_sendMessage = findViewById(R.id.IMG_sendMessage);
        IMG_picture_gallary = findViewById(R.id.IMG_picture_gallary);
        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) CL_outSideEDT.getLayoutParams();
        newLayoutParams.setMarginStart(250);
        IMG_picture_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectGallaryPicture();
            }
        });
        txt_nameFriendChat=findViewById(R.id.TV_chatUserName);
        RIV_avataUser =findViewById(R.id.RIV_avataUser);
        BTN_Call = findViewById(R.id.BTN_Call);
        BTN_CallVideo = findViewById(R.id.BTN_CallVideo);
        BTN_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableFunction();
            }
        });
        BTN_CallVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableFunction();
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent=getIntent();
        this.chatUserId=intent.getIntExtra("chatUserId",-1);
        Toolbar toolbar=findViewById(R.id.toolbar_userChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageButton IMB_interactive=findViewById(R.id.IMB_interactive);
        IMB_interactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(UserChat.this)){
                    Toast.makeText(UserChat.this,"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a=new Intent(UserChat.this, InteractWithAccountUser.class);
                a.putExtra("chatUserId",chatUserId);
                a.putExtra("userAccountName",chatUserName);
                startActivity(a);
            }
        });
        doanchat=findViewById(R.id.doanchat);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        doanchat.setLayoutManager(layoutManager);
        doanchat.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom=40;
            }
        });
        doanchat.setNestedScrollingEnabled(false);
        EDT_chat=findViewById(R.id.EDT_chat);
        EDT_chat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            EDT_chat.requestFocus();
                        }
                    });

                }
            }
        });
        EDT_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                      if(EDT_chat.getText().toString().length()>0){
                          if(IMG_picture_gallary.getVisibility()==View.VISIBLE) {
                              IMG_picture_gallary.setVisibility(View.GONE);
                              ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) CL_outSideEDT.getLayoutParams();
                              newLayoutParams.topMargin = 0;
                              newLayoutParams.setMarginStart(20);
                              CL_outSideEDT.setLayoutParams(newLayoutParams);
                              IMG_sendMessage.setImageResource(R.drawable.sentmessage);
                              IMG_sendMessage.setPadding(24,24,24,24);
                          }
                      }
                      else{
                          IMG_picture_gallary.setVisibility(View.VISIBLE);
                          ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) CL_outSideEDT.getLayoutParams();
                          newLayoutParams.topMargin = 0;
                          newLayoutParams.leftMargin = 0;
                          newLayoutParams.setMarginStart(250);
                          CL_outSideEDT.setLayoutParams(newLayoutParams);
                          IMG_sendMessage.setImageResource(R.drawable.icon_like);
                          IMG_sendMessage.setPadding(0,0,0,0);
                      }
            }
        });
        IMG_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // day la phan quan trong nhat. luc chung da day du lieu len server.
                if(!CheckInternet.isNetworkAvailable(UserChat.this)) {
                    Toast.makeText(UserChat.this,"Không có kết nối internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                String textContent = EDT_chat.getText().toString();
                if(textContent.length()>0) {
                    // chung ta can ma hoa text truoc khi gui di
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        textContent = Base64.getEncoder().encodeToString(textContent.getBytes(StandardCharsets.UTF_8));
                    }
                    else textContent =android.util.Base64.encodeToString(textContent.getBytes(StandardCharsets.UTF_8),android.util.Base64.DEFAULT);
                    int resultId = new OperationServer().sendMessageToServer(textContent,1, InfoMyAccount.getAccountId(),chatUserId);
                    ChatUserMessages messageSent = new ChatUserMessages(resultId, InfoMyAccount.getAccountId(), "Text", EDT_chat.getText().toString());
                    // goi ham su li ket qua tra ve tu server
                    onHandleResultForSendMessage(messageSent,resultId);
                }
                //
                else{
                    //chuyen buc anh like do sang dang base 64 va hien thi no len tren screen
                    String Imagecontent = convertResourceToString(R.drawable.icon_like, UserChat.this);
                    int resultId =  new OperationServer().sendMessageToServer(Imagecontent,2, InfoMyAccount.getAccountId(),chatUserId);
                    ChatUserMessages messageSent = new ChatUserMessages(resultId, InfoMyAccount.getAccountId(), "LikeImage", Imagecontent);
                    // goi ham su li ket qua tra ve tu server
                    onHandleResultForSendMessage(messageSent,resultId);
                }
                EDT_chat.setText("");
            }
        });
    }
    public String convertResourceToString(int resource, Context context) {
        Resources resources = context.getResources();
        try (InputStream inputStream = resources.openRawResource(resource)) {
            // Đọc từng byte từ InputStream và ghi vào ByteArrayOutputStream
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteStream.write(buffer, 0, bytesRead);
            }
            byte[] byteArray = byteStream.toByteArray();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String s = Base64.getEncoder().encodeToString(byteArray);
                // giai ma nguoc
                Base64.getDecoder().decode(s);
                return s;
            } else
                return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        }
        catch (Exception e){
            return null;
        }
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onPause() {
        onResume = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        onResume = true;
        requestInternetToResult();
        super.onResume();
    }

    @Override
    public void finish() {
        EDT_chat.clearFocus();
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
                // chung ta se lay hinh anh ve duoi dang string.
                if(UserChat.selectGallaryPicture.size() == 0) return;
                // du lieu tra ve la string da duoc ma hoa sang dang base64
                for(int i = 0; i< UserChat.selectGallaryPicture.size(); i++){
                    // lay ra tung anh va day len may chu
                    String imageContent = UserChat.selectGallaryPicture.get(i);
                    if(!CheckInternet.isNetworkAvailable(UserChat.this)) {
                        Toast.makeText(UserChat.this,"Kiểm tra internet của bạn",Toast.LENGTH_SHORT).show();
                        UserChat.selectGallaryPicture.clear();
                        return;
                    }
                    int resultId = new OperationServer().sendMessageToServer(imageContent,0, InfoMyAccount.getAccountId(),chatUserId);
                    ChatUserMessages messageSent = new ChatUserMessages(resultId, InfoMyAccount.getAccountId(), "Image",imageContent);
                    onHandleResultForSendMessage(messageSent,resultId);
                }
                UserChat.selectGallaryPicture.clear();
            }
    }
    private void SelectGallaryPicture() {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent a=new Intent(UserChat.this, PickPictureToSendMessage.class);
                        startActivityForResult(a,1);
                    }
                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                    }
                };

                TedPermission.create().setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }


   public void onHandleResultForSendMessage( ChatUserMessages messageSent,int resultForSendMessage) {
       // neu thao tac gui thanh cong hoac tren server co thong diep moi gui ve . ta se cap nhat lai giao dien
       if (resultForSendMessage != -1) {
           this.listMessage.add(messageSent);
           ADTContentchat.notifyDataSetChanged();
           doanchat.smoothScrollToPosition(ADTContentchat.getItemCount() - 1);
       } else Toast.makeText(this, "Bạn hiện không thể gửi tin nhắn cho người này", Toast.LENGTH_LONG).show();
   }
    public void requestInternetToResult(){
        if(CheckInternet.isNetworkAvailable(this)){
            this.chatUserName =new OperationServer().getAccountName(chatUserId); // lấy ra tên account theo Id.
            txt_nameFriendChat.setText(this.chatUserName);
            byte[] avatarUser = new OperationServer().getAvatar(chatUserId);
            Bitmap bitmapAvatarImage = BitmapFactory.decodeByteArray(avatarUser,0,avatarUser.length);
            RIV_avataUser.setImageBitmap(bitmapAvatarImage);
            this.relationship = new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),chatUserId);
            this.listMessage = new OperationServer().getMessageBetweenTwoUser(InfoMyAccount.getAccountId(),this.chatUserId);
            ADTContentchat = new ADTcontentchat(this,(ArrayList<ChatUserMessages>)listMessage,chatUserName,avatarUser,relationship);
            doanchat.setAdapter(ADTContentchat);
            ADTContentchat.notifyDataSetChanged();
            doanchat.smoothScrollToPosition(ADTContentchat.getItemCount()-1);
            // kiểm tra xem nếu user bị block thì hiện thong bao block duoi cung man hinh
            if(this.relationship == -1){
                this.findViewById(R.id.CL_notificationBlocked).setVisibility(View.VISIBLE);
                this.findViewById(R.id.constraintLayout).setVisibility(View.GONE);
            }
        }
    }

    private void disableFunction(){
        //  tao mot hop thoai de dam dao nguoi dung chac chan
        AlertDialog.Builder builder= new AlertDialog.Builder(UserChat.this);
        builder.setTitle("");
        builder.setMessage("Tinh năng hiện đang được cập nhật ! Vui lòng thử lại sau");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
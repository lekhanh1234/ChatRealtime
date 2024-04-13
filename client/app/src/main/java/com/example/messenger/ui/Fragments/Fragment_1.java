package com.example.messenger.ui.Fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTUserChat;
import com.example.messenger.adapter.ADTUserChatOnline;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.model.Messenger;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.App;
import com.example.messenger.ui.FindUser;

import java.util.ArrayList;
import java.util.List;


public class Fragment_1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int isActive ; // 0 la trang thai chua di vao hoat dong nhung chua bi huy
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView RCV_friendOnline;
    public ArrayList<Messenger> messengerList = new ArrayList<>();
    private RecyclerView RCV_UserListChat;

   // => tu day chung ta se su li danh sach cac ban be dang online
    private ADTUserChatOnline ADT_UserChatOnline;
    private final List<GeneralUserInfomation> OnlineUserChatList = new ArrayList<>();

    public List<GeneralUserInfomation> getOnlineFriendList() {
        return OnlineUserChatList;
    }

    ///////////////////////////////////////////
    private ADTUserChat ADT_UserChat;

    public ADTUserChat getADT_UserChat() {
        return ADT_UserChat;
    }

    public Fragment_1() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_1, container, false);
        ScrollView scrollView=view.findViewById(R.id.scrollview);
        RelativeLayout constraintLayout=scrollView.findViewById(R.id.constraintlayout);
        RCV_friendOnline = constraintLayout.findViewById(R.id.recycler_list_userOnline);
        ADT_UserChatOnline = new ADTUserChatOnline(getContext(),OnlineUserChatList);
        RCV_friendOnline.setAdapter(ADT_UserChatOnline);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RCV_friendOnline.setLayoutManager(layoutManager);
        Button BTN_findFriend=view.findViewById(R.id.BTN_findFriend);
        ImageButton IMB_findFriend=view.findViewById(R.id.IMB_findFriend);

        BTN_findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent a=new Intent(getContext(), FindUser.class);
              startActivity(a);
            }
        });
        IMB_findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getContext(), FindUser.class);
                startActivity(a);
            }
        });// 2 đoạn mã này thực ra là đều chỉ đến một button

        RCV_friendOnline.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 15;
                outRect.right = 18;
                // Có thể thay đổi giá trị của outRect.top và outRect.bottom nếu muốn thêm khoảng cách theo chiều dọc
            }
        });

        RCV_UserListChat=constraintLayout.findViewById(R.id.list_user_chat);
        ADT_UserChat=new ADTUserChat(getContext(),messengerList);
        RCV_UserListChat.setAdapter(ADT_UserChat);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RCV_UserListChat.setLayoutManager(layoutManager2);
        RCV_UserListChat.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 25;
                outRect.right = 18;
                outRect.bottom=60;
                // Có thể thay đổi giá trị của outRect.top và outRect.bottom nếu muốn thêm khoảng cách theo chiều dọc
            }
        });
        RCV_UserListChat.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onPause() {
        ((App)getContext()).setCurrentFragmentInOnResume(null);
        isActive = 0;
        super.onPause();
    }

    @Override
    public void onResume() {
        isActive = 1 ;  // fragment san sang nhan thong diep va di vao trang thai hoat dong
        ((App)getContext()).setCurrentFragmentInOnResume(this);
        // sau khi onresume hoat dong tro lai thi ta se cap nhat lai thong tin từ server
        requestInternetToResult();
        // => chúng ta đang chạy trên một luong ngam de nhan cac user dang online moi nhat
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverNotificationUserOnline(handler);
            }
        }).start();
        super.onResume();
    }
    public void serverNotificationUserOnline(Handler handler){
        while (true) {
            if(isActive != 1) break;
            // neu fragment da roi vap pause hoặc thấp hơn. thoát luông
            if(!CheckInternet.isNetworkAvailable(Fragment_1.this.getContext())) {
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // neu khong co internet tam dung 1 giay

            List<GeneralUserInfomation> listFromServer = new OperationServer().getOnlineChatUser(InfoMyAccount.getAccountId());
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 // tai day ta chay trong luong ui. neu fragment dung hoat dong. ta huy luong hanh dong
                                 if (isActive != 1) return; // neu fragment 1 k o trang thai  hoat dong huy bo tac vu
                                 OnlineUserChatList.clear();
                                 if(listFromServer != null) {
                                     for (GeneralUserInfomation x : listFromServer) {
                                         OnlineUserChatList.add(x);
                                     }
                                 }
                                 ADT_UserChatOnline.notifyDataSetChanged();
                             }
                         }
            );

            try{
                    Thread.sleep(20000); // 20 giay gui len server mot lan
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void requestInternetToResult(){
        if(CheckInternet.isNetworkAvailable(getContext())){
            ArrayList<Messenger> messengersFromServer = new OperationServer().getMessengerLastForEachUser(InfoMyAccount.getAccountId());
            this.messengerList.clear();
            for(Messenger m : messengersFromServer) this.messengerList.add(m);
            ADT_UserChat.notifyDataSetChanged();
            Log.d("nhan thong diep moi tu phia may chu", "requestInternetToResult: ");
        }
    }
}
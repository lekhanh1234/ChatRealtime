package com.example.messenger.ui.Fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTAllFriendOnline;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.AllFriend;
import com.example.messenger.ui.App;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean inOnResume;
    private String mParam1;
    private String mParam2;
    private ADTAllFriendOnline ADT_AllFriendOnline;
    private final List<GeneralUserInfomation> OnlineFriendList = new ArrayList<>();

    public List<GeneralUserInfomation> getOnlineFriendList() {
        return OnlineFriendList;
    }

    public ADTAllFriendOnline getADT_AllFriendOnline() {
        return ADT_AllFriendOnline;
    }

    private Button BTN_AllFriend;


    public Fragment_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_2 newInstance(String param1, String param2) {
        Fragment_2 fragment = new Fragment_2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_2, container, false);
        NestedScrollView nestedScrollView=view.findViewById(R.id.NSV_phonebook_online);
        RelativeLayout relativeLayout=nestedScrollView.findViewById(R.id.RL_phonebook_online);
        RecyclerView RV_Friend_online=relativeLayout.findViewById(R.id.RV_Friend_online);
        BTN_AllFriend = view.findViewById(R.id.BTN_AllFriend);
        BTN_AllFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(Fragment_2.this.getContext())){
                    Toast.makeText(Fragment_2.this.getContext(),"Kiểm tra kết nối Internet",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(Fragment_2.this.getContext(), AllFriend.class));
            }
        });
        ADT_AllFriendOnline=new ADTAllFriendOnline(getContext(),OnlineFriendList);
        RV_Friend_online.setAdapter(ADT_AllFriendOnline);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        RV_Friend_online.setLayoutManager(layoutManager);
        RV_Friend_online.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom=30;
                // Có thể thay đổi giá trị của outRect.top và outRect.bottom nếu muốn thêm khoảng cách theo chiều dọc
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        inOnResume = true;
        ((App)getContext()).setCurrentFragmentInOnResume(this);
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
            if(!inOnResume) break;
            if(!CheckInternet.isNetworkAvailable(this.getContext())) {
                try {
                    Thread.sleep(1000);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            List<GeneralUserInfomation> list = new OperationServer().getAllOnlineFriend(InfoMyAccount.getAccountId());
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 if (!inOnResume) return; // neu fragment 1 k o trang thai  hoat dong huy bo tac vu
                                 OnlineFriendList.clear();
                                 if(list != null) {
                                     for (GeneralUserInfomation x : list) {
                                         OnlineFriendList.add(x);
                                     }
                                 }
                                 ADT_AllFriendOnline.notifyDataSetChanged();
                             }
                         }
            );
            try{
                Thread.sleep(60000); // 60 giay gui len server mot lan
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onPause() {
        inOnResume = false;
        ((App)getContext()).setCurrentFragmentInOnResume(null);
        super.onPause();
    }
}
package com.example.messenger.ui.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTTin;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.model.UserPostVideo;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.App;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ADTTin ADTTin;
    private RecyclerView RCV_tin;

    public String getmParam1() {
        return mParam1;
    }

    public com.example.messenger.adapter.ADTTin getADTTin() {
        return ADTTin;
    }

    public List<UserPostVideo> getPostList() {
        return postList;
    }

    public List<UserPostVideo> postList;


    public Fragment_3() {
        // Required empty public constructor
    }

    public static Fragment_3 newInstance(String param1, String param2) {
        Fragment_3 fragment = new Fragment_3();
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
        View view= inflater.inflate(R.layout.fragment_3, container, false);
        RCV_tin=view.findViewById(R.id.RCV_tin);
        ADTTin = new ADTTin(getContext(),null);
        RCV_tin.setAdapter(ADTTin);
        RCV_tin.setLayoutManager(new GridLayoutManager(getContext(),2));
        RCV_tin.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = 10;
                outRect.bottom = 20;
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!CheckInternet.isNetworkAvailable(getContext())) return;
                List<UserPostVideo> list = new OperationServer().getUserPostVideos(InfoMyAccount.getAccountId());
                ADTTin.setUserListVideo(list);
                ADTTin.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        if(postList != null) ADTTin.setUserListVideo(postList);
        ((App)getContext()).setCurrentFragmentInOnResume(this);
            if(CheckInternet.isNetworkAvailable(getContext())) {
                postList = new OperationServer().getUserPostVideos(InfoMyAccount.getAccountId());
                ADTTin.setUserListVideo(postList);
            }
        ADTTin.notifyDataSetChanged();
        super.onResume();
    }
    @Override
    public void onPause() {
        ((App)getContext()).setCurrentFragmentInOnResume(null);
        super.onPause();
    }
}
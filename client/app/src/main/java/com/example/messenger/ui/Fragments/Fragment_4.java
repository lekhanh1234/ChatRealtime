package com.example.messenger.ui.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.Service.ServiceSendRetrieveMessage;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.App;
import com.example.messenger.ui.FindUser;
import com.example.messenger.ui.HelpAndSupport;
import com.example.messenger.ui.ProfileMyself;
import com.example.messenger.ui.SetUpAndPrivacy;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageButton IMB_Setting;
    private ImageButton IMB_FindFriend;
    private TextView TV_AccountName;
    private RoundedImageView RIV_Avatar;
    private ConstraintLayout CL_profile;
    private  ConstraintLayout CL_findFriend;
    private ConstraintLayout CL_SetupAndPrivacy;
    private ConstraintLayout CL_help;
    private ConstraintLayout CL_Logout;
    public Fragment_4() {
        // Required empty public constructor
    }
    public static Fragment_4 newInstance(String param1, String param2) {
        Fragment_4 fragment = new Fragment_4();
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
        View view =inflater.inflate(R.layout.fragment_4, container, false);
        IMB_Setting=view.findViewById(R.id.IMB_Setting);
        IMB_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(getContext())){
                    Toast.makeText(getContext(),"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a=new Intent(getContext(),SetUpAndPrivacy.class);
                startActivity(a);
            }
        });

        IMB_FindFriend=view.findViewById(R.id.IMB_FindFriend);
        IMB_FindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getContext(), FindUser.class);
                startActivity(a);
            }
        });

        TV_AccountName=view.findViewById(R.id.TV_AccountName);
        TV_AccountName.setText(InfoMyAccount.getNameAccount());

        RIV_Avatar=view.findViewById(R.id.RIV_Avatar);
        byte[] avataByteArray = InfoMyAccount.getAvata();
        Bitmap bitmap= BitmapFactory.decodeByteArray(avataByteArray,0,avataByteArray.length);
        RIV_Avatar.setImageBitmap(bitmap);

        CL_profile=view.findViewById(R.id.CL_profile);
        CL_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(getContext())){
                    Toast.makeText(getContext(),"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a=new Intent(getContext(), ProfileMyself.class);
                startActivity(a);
            }
        });

        CL_findFriend=view.findViewById(R.id.CL_findFriend);
        CL_findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getContext(), FindUser.class);
                startActivity(a);
            }
        });

        CL_SetupAndPrivacy=view.findViewById(R.id.CL_SetupAndPrivacy);
        CL_SetupAndPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(getContext())){
                    Toast.makeText(getContext(),"Kiểm tra Internet của bạn",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent a=new Intent(getContext(), SetUpAndPrivacy.class);
                startActivity(a);
            }
        });

        CL_help=view.findViewById(R.id.CL_help);
        CL_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getContext(), HelpAndSupport.class);
                startActivity(a);
            }
        });

        CL_Logout =view.findViewById(R.id.CL_Logout);
        CL_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(getContext())){
                    Toast.makeText(getContext(),"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                new OperationServer().logout(InfoMyAccount.getAccountId());
                ((App)getContext()).deleteFileAccountInfo();
                ((App)getContext()).setLogoutApp(true);
                ServiceSendRetrieveMessage.service.stopSelf();
                ((App)getContext()).finish();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        ((App)getContext()).setCurrentFragmentInOnResume(this);
        TV_AccountName.setText(InfoMyAccount.getNameAccount());
        Bitmap bitmap = BitmapFactory.decodeByteArray(InfoMyAccount.getAvata(),0, InfoMyAccount.getAvata().length);
        RIV_Avatar.setImageBitmap(bitmap);
        super.onResume();
    }

    @Override
    public void onPause() {
        ((App)getContext()).setCurrentFragmentInOnResume(null);
        super.onPause();
    }
}
package com.example.messenger.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.Service.ServiceSendRetrieveMessage;
import com.example.messenger.adapter.ADTViewPager;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

public class App extends AppCompatActivity {
    private boolean logoutApp = false;
    private boolean active = true;
    public void setLogoutApp(boolean logoutApp) {
        this.logoutApp = logoutApp;
    }

    private Fragment currentFragmentInOnResume;

    public Fragment getCurrentFragmentInOnResume() {
        return currentFragmentInOnResume;
    }

    public void setCurrentFragmentInOnResume(Fragment currentFragmentInOnResume) {
        this.currentFragmentInOnResume = currentFragmentInOnResume;
    }
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private  NavigationView navigationView;
    private RoundedImageView RIV_avata;
    private TextView TV_accountName;
    private ImageView IMG_Setting;
    private FrameLayout FL_MessageWaiting;
    private FrameLayout FL_FriendRequestToMyAccount;
    private FrameLayout FL_FriendRequestSent;
    private FrameLayout FL_Logout;
    public Toolbar TB_Toolbar;

    private TextView TV_TitleToolbar;
    public static ActionBar actionBar_form_main_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_main1);
        drawerLayout=findViewById(R.id.drawer_form_main_1);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView =findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        TV_accountName = findViewById(R.id.TV_accountName);
        RIV_avata = findViewById(R.id.RIV_avata);

        IMG_Setting = findViewById(R.id.IMG_Setting);
        IMG_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(App.this)){
                    Toast.makeText(App.this,"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent a=new Intent(App.this, SetUpAndPrivacy.class);
                startActivity(a);
            }
        });

        FL_MessageWaiting = findViewById(R.id.FL_MessageWaiting);
        FL_MessageWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(App.this)){
                    Toast.makeText(App.this,"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(App.this, MessageWating.class));
            }
        });

        FL_FriendRequestToMyAccount = findViewById(R.id.FL_FriendRequestToMyAccount);
        FL_FriendRequestToMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(App.this)){
                    Toast.makeText(App.this,"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(App.this, FriendRequestToMyAccount.class));
            }
        });

        FL_FriendRequestSent = findViewById(R.id.FL_FriendRequestSent);
        FL_FriendRequestSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckInternet.isNetworkAvailable(App.this)){
                    Toast.makeText(App.this,"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(App.this, FriendRequestSent.class));
            }
        });

        FL_Logout = findViewById(R.id.FL_Logout);
        FL_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // yêu cầu logout. gửi 1 request lên server yêu cầu logout tài khoản và xóa phiên
                if(!CheckInternet.isNetworkAvailable(App.this)){
                    Toast.makeText(App.this,"Kiểm tra Internet của bạn !",Toast.LENGTH_LONG).show();
                    return;
                }
                // xóa dữ liệu lưu trữ trong file sharedPreferance
                new OperationServer().logout(InfoMyAccount.getAccountId());
                deleteFileAccountInfo();
                logoutApp = true;
                ServiceSendRetrieveMessage.service.stopSelf();
                finish();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////
        TB_Toolbar= findViewById(R.id.TB_Toolbar);
        setSupportActionBar(TB_Toolbar);
        actionBarDrawerToggle.syncState();
        actionBar_form_main_1 = getSupportActionBar();
        actionBar_form_main_1.setDisplayHomeAsUpEnabled(true);

        TV_TitleToolbar = findViewById(R.id.TV_TitleToolbar);
        actionBar_form_main_1.setTitle("");

        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ViewPager viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(new ADTViewPager(this.getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this));
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<4;i++){
            TabLayout.Tab tab=tabLayout.getTabAt(i);
            if(i==0) tab.setCustomView(R.layout.tab1);
            if(i==1) tab.setCustomView(R.layout.tab2);
            if(i==2) tab.setCustomView(R.layout.tab3);
            if(i==3) tab.setCustomView(R.layout.tab4);
            if(i==0){
                ImageView imageView=tab.getCustomView().findViewById(R.id.image_view_icontab);
                imageView.setColorFilter(getResources().getColor(R.color.blue));
                TextView textView=tab.getCustomView().findViewById(R.id.content_tab);
                textView.setTextColor(ContextCompat.getColor(App.this,R.color.blue));
                continue;
            }
            ImageView imageView=tab.getCustomView().findViewById(R.id.image_view_icontab);
            imageView.setColorFilter(getResources().getColor(R.color.color_xam_tab));
            TextView textView=tab.getCustomView().findViewById(R.id.content_tab);
            textView.setTextColor(ContextCompat.getColor(this,R.color.color_xam_tab));
        } // setup color cho từng tab
        checkTabSelect(tabLayout); // thiết lập color thay đổi khi tab được select và bỏ select
        // chay mot luong ngam de lien tuc gui cac thong diep len server .
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendStatesToServer();// gửi trạng thái cho server là đang online 60 giây một lâần
            }
        }).start();
    }
    public void checkTabSelect(TabLayout tabLayout){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ImageView imageView=tab.getCustomView().findViewById(R.id.image_view_icontab);
                imageView.setColorFilter(getResources().getColor(R.color.blue));
                TextView textView=tab.getCustomView().findViewById(R.id.content_tab);
                textView.setTextColor(ContextCompat.getColor(App.this,R.color.blue));
                if(tab.getPosition()==0) TV_TitleToolbar.setText("Đoạn chat");
                if(tab.getPosition()==1) TV_TitleToolbar.setText("Danh bạ");
                if(tab.getPosition()==2) TV_TitleToolbar.setText("Tin");
                if(tab.getPosition()==3) TV_TitleToolbar.setText("Người dùng");
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView imageView=tab.getCustomView().findViewById(R.id.image_view_icontab);
                imageView.setColorFilter(getResources().getColor(R.color.color_xam_tab));
                TextView textView=tab.getCustomView().findViewById(R.id.content_tab);
                textView.setTextColor(ContextCompat.getColor(App.this,R.color.color_xam_tab));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        TV_accountName = findViewById(R.id.TV_accountName);
        TV_accountName.setText(InfoMyAccount.getNameAccount());
        Bitmap bitmap = BitmapFactory.decodeByteArray(InfoMyAccount.getAvata(),0, InfoMyAccount.getAvata().length);
        RIV_avata.setImageBitmap(bitmap);
        super.onResume();
    }
    public void deleteFileAccountInfo(){
        SharedPreferences sharedPreferences= getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    private void sendStatesToServer(){
        while (true){
            if(!active) break;
            try {
            if(CheckInternet.isNetworkAvailable(this) == false){
                    Thread.sleep(1000);
                    continue;
            }
            new OperationServer().sendStateToServer(InfoMyAccount.getAccountId());
            Thread.sleep(60000);
        } catch (Exception e){}
        }
    }

    @Override
    public void finish() {
        // ngược lại là khi ta đăng xuất app. khi trả về logIn k gọi hàm finish
        LogIn.requestFinish = !logoutApp; // nếu biến logoutApp vẫn bằng false => khi trả về Login thì Login gọi finish luôn (mặc định)
        active = false;
        super.finish();
    }
}
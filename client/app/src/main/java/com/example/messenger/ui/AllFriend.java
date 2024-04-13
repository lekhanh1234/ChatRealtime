package com.example.messenger.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.R;
import com.example.messenger.adapter.ADTAllFriend;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.OperationServer;
import java.util.List;

public class AllFriend extends AppCompatActivity {
    private ListView LV_AllFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_friend);
        LV_AllFriend = findViewById(R.id.LV_AllFriend);
        // lấy 1 list tat ca ban be tu trên server ve;
        List<GeneralUserInfomation> list = new OperationServer().getFriend(InfoMyAccount.getAccountId(),Integer.MAX_VALUE);
        ADTAllFriend adtAllFriend = new ADTAllFriend(list,this);
        LV_AllFriend.setAdapter(adtAllFriend);
    }
    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
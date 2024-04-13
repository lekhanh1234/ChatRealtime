package com.example.messenger.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messenger.R;
import com.example.messenger.adapter.ADTFindUser;
import com.example.messenger.model.GeneralUserInfomation;
import com.example.messenger.operationServer.CheckInternet;
import com.example.messenger.operationServer.OperationServer;

import java.util.List;

public class FindUser extends AppCompatActivity {
    private ListView LV_find_user;
    private ADTFindUser ADTFindUser;
    private List<GeneralUserInfomation> userListFromSearch;
    private Toolbar toolbar_findFriend;
    private ActionBar actionBar_find_friend;
    private EditText EDT_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        toolbar_findFriend=findViewById(R.id.Toolbar_findFriend);
        setSupportActionBar(toolbar_findFriend);
        actionBar_find_friend=getSupportActionBar();
        actionBar_find_friend.setDisplayHomeAsUpEnabled(true);
        //
        LV_find_user = findViewById(R.id.LV_find_user);
        ADTFindUser = new ADTFindUser(userListFromSearch,this);
        LV_find_user.setAdapter(ADTFindUser);
        EDT_Search = findViewById(R.id.EDT_Search);
        EDT_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                   String userAccountName = EDT_Search.getText().toString();
                   if(userAccountName.length() == 0) {
                       if(userListFromSearch != null) userListFromSearch.clear();
                       ADTFindUser.notifyDataSetChanged();
                       return;
                   }
                   if(!CheckInternet.isNetworkAvailable(FindUser.this)) return;
                   userListFromSearch = new OperationServer().findUserByName(userAccountName);
                   ADTFindUser.setList(userListFromSearch);
                   ADTFindUser.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if(MyActivityLifecycleCallbacks.finish) finish();
        super.onResume();
    }
}
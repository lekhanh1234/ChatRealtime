package com.example.messenger.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.messenger.ui.Fragments.Fragment_1;
import com.example.messenger.ui.Fragments.Fragment_2;
import com.example.messenger.ui.Fragments.Fragment_3;
import com.example.messenger.ui.Fragments.Fragment_4;

public class ADTViewPager extends FragmentPagerAdapter {

    Context context;

    public ADTViewPager(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0) {
            Fragment_1 fragment_1=new Fragment_1();
            return fragment_1;
        }
        if(position==1) {
            Fragment_2 fragment_2=new Fragment_2();
            return fragment_2;
        }
        if(position==2){
            Fragment_3 fragment_3=new Fragment_3();
            return fragment_3;
        }
        if(position==3){
            Fragment_4 fragment_4=new Fragment_4();
            return fragment_4;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

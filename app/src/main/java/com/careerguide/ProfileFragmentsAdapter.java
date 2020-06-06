package com.careerguide;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProfileFragmentsAdapter extends FragmentPagerAdapter {
    private Context context;


    ProfileFragmentsAdapter(FragmentManager fm, Context context1) {
        super(fm);
        context = context1;

    }

    @Override
    public Fragment getItem(int position){
        if (position == 1){
            return new RewardLBFragment();
        }
        else{
            return new StudentProfileFragment();
        }
    }

    @Override
    public int getCount() {return 2;}

    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "Student Profile";
            case 1:
                return "Rewards";
            default:
                return null;
        }
    }
}

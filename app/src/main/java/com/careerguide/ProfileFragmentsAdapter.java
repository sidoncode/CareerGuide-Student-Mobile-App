package com.careerguide;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.careerguide.Book_One_To_One.fragment.MyBookingsFragment;

public class ProfileFragmentsAdapter extends FragmentPagerAdapter {
    private Context context;


    ProfileFragmentsAdapter(FragmentManager fm, Context context1) {
        super(fm);
        context = context1;

    }

    @Override
    public Fragment getItem(int position){

        switch (position){
            case 0:{
                return new StudentProfileFragment();
            }
            case 1:{
                return new RewardLBFragment();
            }
            case 2:{
                return new MyBookingsFragment();
            }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {return 3;}

    @Override
    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "Profile";
            case 1:
                return "Rewards";
            case 2:
                return "Bookings";
            default:
                return null;
        }
    }
}

package com.careerguide;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

//import me.relex.circleindicator.CircleIndicator;


public class OnBoardingActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);

        List<Fragment> fList = new ArrayList<Fragment>();

        switch (getIntent().getIntExtra("selection",1))
        {
            case 1:
                fList.add(BlankFragment.newInstance(R.drawable.welcome_two,R.layout.fragment_blank, null, "Talk To A Counsellor","Counsellors are ready to help you anytime"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_three,R.layout.fragment_blank, null, "Take Psychometric Tests","A test to guide your direction in your career"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_four,R.layout.fragment_blank,null, "Find Career Options","We will help you look out all the options you have to make your career successful"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_four,R.layout.fragment_blank, null, "College Admission","We help you to find & connect to best colleges"));
                break;
            case 2:
                fList.add(BlankFragment.newInstance(R.drawable.welcome_one,R.layout.fragment_blank,null, "Guide Students On Phone","Answer career queries on phone and earn"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_two,R.layout.fragment_blank, null, "Certification Courses","Up-Skill & upgrade knowledge"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_three,R.layout.fragment_blank, null, "Career Counsellor Jobs","We curate & list counselling jobs for you"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_three,R.layout.fragment_blank, null, "Know Your Community","Increase your network-know other career counsellors"));
                break;
            case 3:
                fList.add(BlankFragment.newInstance(R.drawable.welcome_two,R.layout.fragment_blank, null, "Talk To A Counsellor","Counsellors are ready to help your children anytime"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_three,R.layout.fragment_blank, null, "Take Psychometric Tests","Scientifically designed career tests for your child"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_four,R.layout.fragment_blank,null, "Search A Career Counsellor","Let your child meet a career counsellor from your city"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_four,R.layout.fragment_blank,null, "Study In India","We help your child find & connect to best colleges in india"));
                fList.add(BlankFragment.newInstance(R.drawable.welcome_four,R.layout.fragment_blank,null, "Study In Abroad","We help your child find & connect to best across the world"));
                break;
        }
        MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager(),fList);
        pager.setAdapter(adapter);
        PageIndicatorView circleIndicator = (PageIndicatorView) findViewById(R.id.indicator);
        circleIndicator.setUnselectedColor(getResources().getColor(R.color.grey));
        circleIndicator.setSelectedColor(getResources().getColor(R.color.buttonColor));
        circleIndicator.setRadius(7);
        circleIndicator.setAnimationType(AnimationType.SCALE);
        circleIndicator.setViewPager(pager);
    }

    class MyPageAdapter extends FragmentStatePagerAdapter
    {

        private List<Fragment> fragments;
        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}

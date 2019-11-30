package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;
import java.util.List;

public class PsychometricTestsActivity extends AppCompatActivity {

    private Activity activity = this;
    private MyPageAdapter adapter;
    private PageIndicatorView circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychometric_tests);
        setTitle("Psychometric Career Tests");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FixedTransformerViewPager pager = (FixedTransformerViewPager)findViewById(R.id.viewPager);
        pager.setClipToPadding(false);
        pager.setPadding(Utility.getPx(40),0,Utility.getPx(40),0);
        pager.setPageMargin(Utility.getPx(10));
        List<Fragment> fList = new ArrayList<Fragment>();
        IdealCareerTestFragment idealCareerTestFragment = new IdealCareerTestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("start",getIntent().getBooleanExtra("start",false));
        idealCareerTestFragment.setArguments(bundle);
        fList.add(idealCareerTestFragment);
        fList.add(new ProfessionalSkillIndexTestFragment());
        fList.add(new HumanitiesTestFragment());
        fList.add(new StreamSelectorTestFragment());
        fList.add(new CommerceTestFragment());
        fList.add(new EngineeringTestFragment());
        fList.add(new SkillBasedTestFragment());
        adapter = new MyPageAdapter(getSupportFragmentManager(),fList);
        pager.setAdapter(adapter);
        //invalidatePageTransformer(pager);
        /*pager.scrollBy((int)getResources().getDimension(R.dimen.cardview_default_radius),0);
        pager.scrollBy(-(int)getResources().getDimension(R.dimen.cardview_default_radius),0);*/
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                manageHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        circleIndicator = (PageIndicatorView) findViewById(R.id.indicator);
        circleIndicator.setUnselectedColor(Color.parseColor("#a5a5a5"));
        circleIndicator.setSelectedColor(getResources().getColor(R.color.buttonColor));
        circleIndicator.setRadius(7);
        circleIndicator.setAnimationType(AnimationType.SCALE);
        circleIndicator.setViewPager(pager);
        pager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                //page.setRotationY(position * -30);
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                /*page.setScaleX(normalizedposition / 2 + 0.5f);*/
                {
                    page.setScaleY(normalizedposition/10 + 0.9f /*/ 2f + 0.5f*/);
                }
            }
        });
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                manageHeight(0);
            }
        }, 60);
    }

    private void invalidatePageTransformer(final ViewPager pager)
    {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //no need to invalidate if we have no adapter or no items
                if (pager.getAdapter() != null && pager.getAdapter().getCount() > 0)
                {
                    //import check here, only fakeDrag if "beginFakeDrag()" returns true
                    if (pager.beginFakeDrag())
                    {
                        pager.fakeDragBy(0f);
                        pager.endFakeDrag();
                    }
                }
            }
        });

    }

    private void manageHeight(int position)
    {
        TestSuperFragment prevFragment = null;
        TestSuperFragment currentfragment = null;
        TestSuperFragment nextFragment = null;
        try {
            prevFragment = (TestSuperFragment) adapter.getItem(position - 1);
        }
        catch (IndexOutOfBoundsException ex)
        {
            //ex.printStackTrace();
        }
        try {
            currentfragment = (TestSuperFragment) adapter.getItem(position);
        }
        catch (IndexOutOfBoundsException ex)
        {
            //ex.printStackTrace();
        }
        try {
            nextFragment = (TestSuperFragment) adapter.getItem(position + 1);
        }
        catch (IndexOutOfBoundsException ex)
        {
            //ex.printStackTrace();
        }

        /*if (currentfragment != null) {
            currentfragment.increaseHeight();
        }
        if (prevFragment != null) {
            prevFragment.decreaseHeight();
        }
        if (nextFragment != null) {
            nextFragment.decreaseHeight();
        }*/
        switch (position)
        {
            case 0:
                circleIndicator.setSelectedColor(Color.parseColor("#ff8f00"));
                break;
            case 1:
                circleIndicator.setSelectedColor(Color.parseColor("#4ea690"));
                break;
            case 2:
                circleIndicator.setSelectedColor(Color.parseColor("#0579c4"));
                break;
            case 3:
                circleIndicator.setSelectedColor(Color.parseColor("#ffad00"));
                break;
            case 4:
                circleIndicator.setSelectedColor(Color.parseColor("#30b240"));
                break;
            case 5:
                circleIndicator.setSelectedColor(Color.parseColor("#fa4800"));
                break;
            case 6:
                circleIndicator.setSelectedColor(Color.parseColor("#37474f"));
                break;
        }
        /*
        for (int i = 0; i<adapter.getCount(); i++)
        {
            IdealCareerTestFragment fragment = (IdealCareerTestFragment) adapter.getItem(position);
            if(i == position) {
                fragment.increaseHeight();
            }
            else
            {
                if(fragment != null)
                fragment.decreaseHeight();
            }
        }*/
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }
}

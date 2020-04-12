package com.careerguide;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {
    private View view;


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about_us, container, false);

        final ImageView infoIcon = view.findViewById(R.id.aboutIcon);
        final ImageView mgmtIcon = view.findViewById(R.id.mgmtIcon);
        final TextView currentTabTextView = view.findViewById(R.id.currentTab);

        final View aboutLayout = getLayoutInflater().inflate(R.layout.about_us_frame,null);
        final View mgmtLayout = getLayoutInflater().inflate(R.layout.mgmt_frame,null);

        final LinearLayout detailLinearLayout = view.findViewById(R.id.detailLayout);

        infoIcon.setOnClickListener(v -> {
            currentTabTextView.setText("About CareerGuide");
            infoIcon.setImageResource(R.drawable.ic_about_selected);
            mgmtIcon.setImageResource(R.drawable.ic_mgmt_unselected);
            try {
                detailLinearLayout.removeView(mgmtLayout);
                detailLinearLayout.addView(aboutLayout);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        mgmtIcon.setOnClickListener(v -> {
            currentTabTextView.setText("Managemant Team");
            infoIcon.setImageResource(R.drawable.ic_about_unselected);
            mgmtIcon.setImageResource(R.drawable.ic_mgmt_team_selected);
            try {
                detailLinearLayout.removeView(aboutLayout);
                detailLinearLayout.addView(mgmtLayout);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        mgmtIcon.performClick();
        infoIcon.performClick();

        aboutLayout.findViewById(R.id.askUs).setOnClickListener(v -> {
            try {
                MenuItem menuItem = ((HomeActivity) getActivity()).nvDrawer.getMenu().getItem(4);
                ((HomeActivity) getActivity()).selectDrawerItem(menuItem);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        final TextView prnvTextView = mgmtLayout.findViewById(R.id.prnvInfo);
        new Handler().postDelayed(() -> {
            //final int prnvLineCount = prnvTextView.getMaxLines();
            prnvTextView.setMaxLines(3);
            final TextView prnvMore = mgmtLayout.findViewById(R.id.prnvMore);
            prnvMore.setOnClickListener(v -> {
                if(prnvTextView.getMaxLines() == 3)
                {
                    prnvTextView.setMaxLines(Integer.MAX_VALUE);
                    prnvMore.setText("Show Less");
                }
                else
                {
                    prnvMore.setText("Read More");
                    prnvTextView.setMaxLines(3);
                }
            });
        },50);

        final TextView surbhiTextView = mgmtLayout.findViewById(R.id.surbhiInfo);
        new Handler().postDelayed(() -> {
            surbhiTextView.setMaxLines(3);
            final TextView surbhiMore = mgmtLayout.findViewById(R.id.surbhiMore);
            surbhiMore.setOnClickListener(v -> {
                if(surbhiTextView.getMaxLines() == 3)
                {
                    surbhiTextView.setMaxLines(Integer.MAX_VALUE);
                    surbhiMore.setText("Show Less");
                }
                else
                {
                    surbhiMore.setText("Read More");
                    surbhiTextView.setMaxLines(3);
                }
            });
        },50);
        return view;
    }

}

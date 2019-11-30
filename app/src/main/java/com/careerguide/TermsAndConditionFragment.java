package com.careerguide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConditionFragment extends Fragment {


    public TermsAndConditionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
        final WebView tAndCWebView = view.findViewById(R.id.tandCWebView);
        tAndCWebView.loadUrl("https://www.careerguide.com/terms");

        final WebView privacyPolicyWebView = view.findViewById(R.id.privacyPolicyWebView);
        privacyPolicyWebView.loadUrl("https://www.careerguide.com/privacy");

        final ImageView tAndCArrow =  view.findViewById(R.id.arrowTAndC);
        final ImageView privacyPolicyArrow = view.findViewById(R.id.arrowPrivacyPolicy);
        view.findViewById(R.id.TandCRelativeL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tAndCWebView.getVisibility() == View.VISIBLE)
                {
                    tAndCWebView.setVisibility(View.GONE);
                    tAndCArrow.setImageResource(R.drawable.ic_expand);
                }
                else
                {
                    tAndCWebView.setVisibility(View.VISIBLE);
                    tAndCArrow.setImageResource(R.drawable.ic_collapse);
                }
                privacyPolicyWebView.setVisibility(View.GONE);
                privacyPolicyArrow.setImageResource(R.drawable.ic_expand);
            }
        });

        view.findViewById(R.id.PrivacyPolicyRelativeL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privacyPolicyWebView.getVisibility() == View.VISIBLE)
                {
                    privacyPolicyWebView.setVisibility(View.GONE);
                    privacyPolicyArrow.setImageResource(R.drawable.ic_expand);
                }
                else
                {
                    privacyPolicyWebView.setVisibility(View.VISIBLE);
                    privacyPolicyArrow.setImageResource(R.drawable.ic_collapse);
                }
                tAndCWebView.setVisibility(View.GONE);
                tAndCArrow.setImageResource(R.drawable.ic_expand);
            }
        });

        return view;
    }

}

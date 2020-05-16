package com.careerguide.newsfeed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.careerguide.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class FeedViewFragment extends Fragment{




    private String url;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    WebView webView;
    ProgressBar progressBar;
    private Toolbar toolbar;
    public FeedViewFragment() {

        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_feed_view, container, false);

        if(getArguments() != null) {

            url = getArguments().getString("url");
        }




        progressBar = view.findViewById(R.id.progress_wv);
        webView = (WebView) view.findViewById(R.id.web_view_1);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK &&  event.getAction() == KeyEvent.ACTION_DOWN )
                {

                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_feedFragment);
                    bottomNavigationView.setSelectedItemId(R.id.nav_feed);
                    Toolbar mainToolbar = getActivity().findViewById(R.id.toolbar);
                    ((AppCompatActivity)getActivity()).setSupportActionBar(mainToolbar);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    return true;
                }
                return false;
            }
        } );

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        toolbar = view.findViewById(R.id.toolbar_feedview);
        toolbar.setTitle("News");
         ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
                navController.navigate(R.id.nav_to_feedFragment);
                bottomNavigationView.setSelectedItemId(R.id.nav_feed);
                Toolbar mainToolbar = getActivity().findViewById(R.id.toolbar);
                ((AppCompatActivity)getActivity()).setSupportActionBar(mainToolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            }
        });
        /*Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
                navController.navigate(R.id.nav_to_feedFragment);
                bottomNavigationView.setSelectedItemId(R.id.nav_feed);
            }
        });*/


        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(
                url);




        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webView.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });





        return view;
    }


}

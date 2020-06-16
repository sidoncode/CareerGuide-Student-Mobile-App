package com.careerguide;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

public class ebookpdf extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                try{
                    Toast.makeText(getContext(), "Download Completed", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                hideProgressBar();
                Activity activity = getActivity();
                if(activity != null && isAdded())//handles crash if the activity is killed
                    startActivity(goToNextScreen);
            }
        }
    };

    private Intent goToNextScreen;

    private long downloadID;


    private String ebook1="self-career-counselling-ebook.pdf";
    private String ebook1_URL="https://www.careerguide.com/career/wp-content/uploads/2019/09/self-career-counselling-ebook.pdf";
    private String ebook2="new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf";
    private String ebook2_URL="https://www.careerguide.com/career/wp-content/uploads/2019/05/new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf";
    private String toolBarTitle="Career EBook";
    private RelativeLayout progressBar;

    public ebookpdf() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ebook, container, false);
        progressBar=view.findViewById(R.id.progressBar);

        try {
            getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }catch (Exception e){
            e.printStackTrace();
        }



        view.findViewById(R.id.imageViewSelfCareer).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);
            goToNextScreen.putExtra("pdfName", ebook1);
            goToNextScreen.putExtra("toolBarTitle",toolBarTitle);

            if(Utility.getStoragePermissionFromUser(getActivity())) {//file already downloaded
                if (Utility.checkFileExist(ebook1))
                    startActivity(goToNextScreen);
                else {
                    downloadID = Utility.downloadPdf(ebook1, ebook1_URL, "Self Career E-Book", "Downloading...", getActivity());
                    showProgressBar();
                }
            }
        });



        view.findViewById(R.id.buttonSelfCareer).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);
            goToNextScreen.putExtra("pdfName", ebook1);
            goToNextScreen.putExtra("toolBarTitle",toolBarTitle);

            if(Utility.getStoragePermissionFromUser(getActivity())) {//file already downloaded
                if (Utility.checkFileExist(ebook1))
                    startActivity(goToNextScreen);
                else {
                    downloadID = Utility.downloadPdf(ebook1, ebook1_URL, "Self Career E-Book", "Downloading...", getActivity());
                    showProgressBar();
                }
            }
        });

        view.findViewById(R.id.imageViewNewAge).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);
            goToNextScreen.putExtra("pdfName", ebook2);
            goToNextScreen.putExtra("toolBarTitle", toolBarTitle);


            if (Utility.getStoragePermissionFromUser(getActivity())) {//file already downloaded
                if (Utility.checkFileExist(ebook2))
                    startActivity(goToNextScreen);
                else {
                    downloadID = Utility.downloadPdf(ebook2, ebook2_URL, "New Age E-Book", "Downloading...", getActivity());
                    showProgressBar();
                }
            }
        });
        view.findViewById(R.id.buttonNewAge).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);
            goToNextScreen.putExtra("pdfName", ebook2);
            goToNextScreen.putExtra("toolBarTitle",toolBarTitle);

            if(Utility.getStoragePermissionFromUser(getActivity())) {//file already downloaded
                if (Utility.checkFileExist(ebook2))
                    startActivity(goToNextScreen);
                else {
                    downloadID = Utility.downloadPdf(ebook2, ebook2_URL, "New Age E-Book", "Downloading...", getActivity());
                    showProgressBar();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.getStoragePermissionFromUser(getActivity());
    }

    private void showProgressBar(){

        progressBar.setVisibility (View.VISIBLE);

    }
    private void hideProgressBar(){

        progressBar.setVisibility (View.INVISIBLE);

    }
}
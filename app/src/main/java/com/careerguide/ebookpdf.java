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

    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(getContext(), "Download Completed", Toast.LENGTH_SHORT).show();
                hideProgressBar();
                startActivity(goToNextScreen);
            }
        }
    };

    Intent goToNextScreen;

    long downloadID;

    ebookpdf activity = this;
    String ebook1="self-career-counselling-ebook.pdf";
    String ebook1_URL="https://www.careerguide.com/career/wp-content/uploads/2019/09/self-career-counselling-ebook.pdf";
    String ebook2="new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf";
    String ebook2_URL="https://www.careerguide.com/career/wp-content/uploads/2019/05/new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf";
    String toolBarTitle="Career EBook";
    RelativeLayout progressBar;

    public ebookpdf() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ebook, container, false);
        progressBar=view.findViewById(R.id.progressBar);

        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        view.findViewById(R.id.imageViewSelfCareer).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);
            goToNextScreen.putExtra("pdfName", ebook1);
            goToNextScreen.putExtra("toolBarTitle",toolBarTitle);

            if(checkFileExist(ebook1)) {//file already downloaded
                startActivity(goToNextScreen);
            }else
            {
                downloadPdf(ebook1,ebook1_URL);
                showProgressBar();
            }
        });



        view.findViewById(R.id.buttonSelfCareer).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);

            goToNextScreen.putExtra("pdfName", ebook1);
            goToNextScreen.putExtra("toolBarTitle",toolBarTitle);

            if(checkFileExist(ebook1)) {//file already downloaded
                startActivity(goToNextScreen);
            }else
            {
                downloadPdf(ebook1,ebook1_URL);
                showProgressBar();
            }
        });

        view.findViewById(R.id.imageViewNewAge).setOnClickListener(v -> {
            goToNextScreen = new Intent(getActivity(), WebViewActivity.class);
            goToNextScreen.putExtra("pdfName", ebook2);
            goToNextScreen.putExtra("toolBarTitle",toolBarTitle);


            if(checkFileExist(ebook2)) {//file already downloaded
                startActivity(goToNextScreen);
            }else
            {
                downloadPdf(ebook2,ebook2_URL);
                showProgressBar();
            }

        });
        view.findViewById(R.id.buttonNewAge).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("pdfName", ebook2);
            intent.putExtra("toolBarTitle",toolBarTitle);

            if(checkFileExist(ebook2)) {//file already downloaded
                startActivity(intent);
            }else
            {
                downloadPdf(ebook2,ebook2_URL);
                showProgressBar();
            }
        });
        return view;
    }

    void downloadPdf(String fileName,String url){
        DownloadManager downloadmanager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Ebook");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+fileName);
        request.setDestinationUri(Uri.fromFile(file));

        downloadID=downloadmanager.enqueue(request);

    }

    Boolean checkFileExist(String fileName){
        String filename = "/Download/"+fileName;
        File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        if(!f1.exists() && !f1.isDirectory()){
            return  false;
        }else {
            return true;
        }
    }

    void showProgressBar(){

        progressBar.setVisibility (View.VISIBLE);

    }
    void hideProgressBar(){

        progressBar.setVisibility (View.INVISIBLE);

    }
}
package com.careerguide;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.io.File;

public class ebookpdf extends Fragment {

        ebookpdf activity = this;
        String ebook1="self-career-counselling-ebook.pdf";
        String ebook1_URL="https://www.careerguide.com/career/wp-content/uploads/2019/09/self-career-counselling-ebook.pdf";
        String ebook2="new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf";
        String ebook2_URL="https://www.careerguide.com/career/wp-content/uploads/2019/05/new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf";
        String toolBarTitle="Career EBook";
        LinearLayout progressBar;

    public ebookpdf() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ebook, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        view.findViewById(R.id.imageViewSelfCareer).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("pdfName", ebook1);
            intent.putExtra("toolBarTitle",toolBarTitle);

            if(checkFileExist(ebook1)) {//file already downloaded
                startActivity(intent);
            }else
            {
                downloadPdf(ebook1,ebook1_URL);
                showProgressBar();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        startActivity(intent);
                    }
                }, 5500);
            }
        });
        view.findViewById(R.id.buttonSelfCareer).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);

            intent.putExtra("pdfName", ebook1);
            intent.putExtra("toolBarTitle",toolBarTitle);

            if(checkFileExist(ebook1)) {//file already downloaded
                startActivity(intent);
            }else
            {
                downloadPdf(ebook1,ebook1_URL);
                showProgressBar();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        startActivity(intent);
                    }
                }, 5500);
            }
        });

        view.findViewById(R.id.imageViewNewAge).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("pdfName", ebook2);
            intent.putExtra("toolBarTitle",toolBarTitle);


            if(checkFileExist(ebook2)) {//file already downloaded
                startActivity(intent);
            }else
            {
                downloadPdf(ebook2,ebook2_URL);
                showProgressBar();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        startActivity(intent);
                    }
                }, 5500);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        startActivity(intent);
                    }
                }, 5500);
            }
        });
        return view;
    }

    void downloadPdf(String fileName,String url){
            DownloadManager downloadmanager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Pyschometric Report");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+fileName);
            request.setDestinationUri(Uri.fromFile(file));

            downloadmanager.enqueue(request);

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
//    String message = "Choose today Enjoy tomorrow - Make right career choices";
//    File fileBrochure = new File( Environment.getExternalStorageDirectory().getPath() + "/" + pdfsname );
//                if (!fileBrochure.exists()) {
//                        CopyAssetsbrochure();
//                        }
//
//                        /** PDF reader code */
//                        File file = new File( Environment.getExternalStorageDirectory().getPath() + "/" + pdfsname );
//
//                        Intent intent = new Intent( Intent.ACTION_SEND );
//                        intent.putExtra( Intent.EXTRA_STREAM, Uri.fromFile( file ) );
//                        intent.setType( "application/pdf" );
//                        intent.putExtra( Intent.EXTRA_TEXT, message );
//                        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
//                        intent.setPackage( "com.whatsapp" );
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        try {
//                        startActivity( intent );
//                        } catch (ActivityNotFoundException e) {
//                        Toast.makeText( ShareActivity.this, "NO Pdf Viewer", Toast.LENGTH_SHORT ).show();
//                        }
//                        }
//
////method to write the PDFs file to sd card
//private void CopyAssetsbrochure() {
//        AssetManager assetManager = getAssets();
//        String[] files = null;
//        try {
//        files = assetManager.list( "" );
//        } catch (IOException e) {
//        Log.e( "tag", e.getMessage() );
//        }
//        for (int i = 0; i < files.length; i++) {
//        String fStr = files[i];
//        if (fStr.equalsIgnoreCase( pdfsname)) {
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//        in = assetManager.open( files[i] );
//        out = new FileOutputStream ( Environment.getExternalStorageDirectory() + "/" + files[i] );
//        copyFile( in, out );
//        in.close();
//        out.flush();
//        out.close();
//
//        break;
//        } catch (Exception e) {
//        Log.e( "tag", e.getMessage() );
//        }
//        }
//        }
//        }
//        } );
//        }
//
//        private void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int read;
//        while((read = in.read(buffer)) != -1){
//        out.write(buffer, 0, read);
//        }
//        }
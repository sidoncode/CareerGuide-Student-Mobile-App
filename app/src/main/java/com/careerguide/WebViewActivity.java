package com.careerguide;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;import android.os.StrictMode;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.google.firebase.FirebaseApp;

import java.io.File;
import java.io.IOException;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = WebViewActivity.class.getSimpleName ( );
    private static final String AUTHORITY =
            "com.careerguide";
    private static final Uri PROVIDER =
            Uri.parse ( "content://" + AUTHORITY );
    private Activity activity = this;
    private Context mContext;

    WebView webView;
    Activity view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ( );

        String pdfname = getIntent ( ).getStringExtra ( "filename" );
        String pdfurl = getIntent ( ).getStringExtra ( "url" );
        setContentView ( R.layout.activity_web_view );
        if (pdfurl.contains ( "sample" ) || pdfurl.contains("amazonaws") ){
            setTitle ( "View Report" );
        }
        else{
            setTitle ( "Career E-Book" );
        }
        StrictMode.setVmPolicy ( builder.build ( ) );
        getSupportActionBar ( ).setDisplayHomeAsUpEnabled ( true );
        new DownloadFile ( ).execute ( pdfurl, pdfname );
        getSupportActionBar ( ).setDisplayHomeAsUpEnabled ( true );
        FirebaseApp.initializeApp ( activity );
        WebView webView = (WebView) findViewById ( R.id.webView );
        webView.getSettings ( ).setJavaScriptEnabled ( true );
        webView.getSettings ( ).setAllowUniversalAccessFromFileURLs ( true );
        webView.getSettings ( ).setLoadWithOverviewMode ( true );
        webView.getSettings ( ).setDomStorageEnabled ( true );
        final ProgressDialog progressDialog = new ProgressDialog ( activity );
        progressDialog.setMessage ( "Please Wait..." );
        progressDialog.setCancelable ( false );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.show ( );
        webView.loadUrl ( "https://docs.google.com/gview?embedded=true&url=" + pdfurl );
        Handler handler = new Handler ( );
        boolean b = handler.postDelayed (() -> progressDialog.dismiss ( ), 10000 );




        if (ContextCompat.checkSelfPermission ( this, "android.Manifest.permission.READ_EXTERNAL_STORAGE" )
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions ( new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        15 );
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.activity_webview, menu );
        return super.onCreateOptionsMenu ( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId ( ) == android.R.id.home) {
            finish ( );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }



    @Override
    protected void onPause() {
        super.onPause ( );
        Utility.handleOnlineStatus ( null, "" );
    }

    public void shareAsset(View v) {
        String pdfname = getIntent ( ).getStringExtra ( "filename" );
        File pdfFile = new File ( Environment.getExternalStorageDirectory ( ) + "/Career-Ebook/" + pdfname );  // -> filename = maven.pdf
        Uri path = Uri.fromFile ( pdfFile );
        Intent shareIntent = new Intent ( );
        shareIntent.setAction ( Intent.ACTION_SEND ); // temp permission for receiving app to read this file
        shareIntent.setType ( "application/pdf" );
        shareIntent.setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        shareIntent.putExtra ( Intent.EXTRA_STREAM, path );
        shareIntent.addFlags ( Intent.FLAG_GRANT_READ_URI_PERMISSION );
        startActivity ( Intent.createChooser ( shareIntent, "Choose an app" ) );
    }


    @Override
    public void onBackPressed() {
        //  webView.loadUrl("https://docs.google.com/viewerng/viewer?url=" + /*"https://s3-ap-southeast-1.amazonaws.com/fal-careerguide/id-la/67528.pdf"*/ getIntent().getStringExtra("url"));
        super.onBackPressed ( );
//        Intent back1 = new Intent(WebViewActivity.this, ebookpdf.class);
//        back1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(back1);
//        finish();
    }

    public class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory ( ).toString ( );
            File folder = new File ( extStorageDirectory, "Career-Ebook" );
            folder.mkdir ( );

            File pdfFile = new File ( folder, fileName );

            try {
                pdfFile.createNewFile ( );
            } catch (IOException e) {
                e.printStackTrace ( );
            }
            FileDownloader.downloadFile ( fileUrl, pdfFile );
            return null;
        }


    }

}


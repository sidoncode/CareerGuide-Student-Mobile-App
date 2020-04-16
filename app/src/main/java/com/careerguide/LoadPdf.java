package com.careerguide;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class LoadPdf extends AppCompatActivity {
Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_pdf);
        PDFView pdfView=findViewById(R.id.pdfView);
         Uri uri = Uri.parse(getIntent().getStringExtra("url"));
//        pdfFileName = getFileName(uri);

        int pageNumber = 1;
//        pdfView.fromUrl(getIntent().getStringExtra("url"))
//                .enableSwipe(true) // allows to block changing pages using swipe
//                .defaultPage(0)
//                .onLoad(this) // called after document is loaded and starts to be rendered
//                .onPageChange(this)
//                .swipeHorizontal(false)
//                .enableAntialiasing(true)
//                .onFileDownload(this)
//                .loadFromUrl();

  }
}





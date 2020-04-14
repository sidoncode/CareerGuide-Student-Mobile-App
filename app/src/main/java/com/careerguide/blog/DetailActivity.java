package com.careerguide.blog;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.careerguide.R;

public class DetailActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        webView  = findViewById(R.id.webview);
        String reev = getIntent().getStringExtra("url");
        Log.i("Sending:",";"+reev);
        String htmlData = "<!DOCTYPE html><html><head><meta charset='UTF-8' /><meta name='viewport' content='width=device-width, initial-scale=1'><link rel=\"stylesheet\" href=\"style.css\"></head><body><div class=\"content\">" + reev;
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.loadDataWithBaseURL("file:///android_asset/", htmlData,"text/html", "UTF-8", null);
    }

    public void backpress(View view) {
        finish();
    }
}

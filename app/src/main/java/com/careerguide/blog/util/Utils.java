package com.careerguide.blog.util;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.R;
import com.careerguide.blog.api.ApiService;
import com.careerguide.blog.model.CatFilter;
import com.careerguide.blog.model.CategoryDetails;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static ApiService apiService = null;

    private static Retrofit get_retrofit() {
        if (okHttpClient == null)
            get_okhttp();
        if (retrofit == null) {
            String BASE_URL = "https://app.careerguide.com/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void get_okhttp() {
        int time_out = 60;
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(time_out, TimeUnit.SECONDS)
                .readTimeout(time_out, TimeUnit.SECONDS)
                .writeTimeout(time_out, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        okHttpClient = httpClient.build();
    }

    public static ApiService get_api() {
        if (apiService == null)
            apiService = get_retrofit().create(ApiService.class);
        return apiService;
    }

    public static void set_image(Context ctx, String url, AppCompatImageView imageView) {
        if (url != null) {
            if (url.equals("")) {
                imageView.setBackgroundResource(R.drawable.ic_placeholder);
                return;
            }
            imageView.setBackground(null);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();
            Glide.with(ctx).load(url).apply(options).into(imageView);
        }
    }

    public static void set_image(Context ctx, String url, CircleImageView imageView) {
        if (url != null) {
            if (url.equals("")) {
                imageView.setBackgroundResource(R.drawable.ic_placeholder);
                return;
            }
            imageView.setBackground(null);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();
            Glide.with(ctx).load(url).apply(options).into(imageView);
        }
    }

    public static void show_loader(SpinKitView spinKitView) {
        spinKitView.setVisibility(View.VISIBLE);
    }

    public static void hide_loader(SpinKitView spinKitView) {
        spinKitView.setVisibility(View.GONE);
    }

    public static List<CatFilter> filter_content(CategoryDetails categoryDetails) {
        List<CatFilter> catFilters = new ArrayList<>();
        if (categoryDetails == null)
            return catFilters;
        catFilters.add(new CatFilter("title_pic", categoryDetails.getPic_url()));
        catFilters.add(new CatFilter("title", remove_tags(categoryDetails.getTitle())));
        catFilters.addAll(split_content(categoryDetails.getContent()));
        return catFilters;
    }

    private static List<CatFilter> split_content(String source) {
        List<CatFilter> catFilters = new ArrayList<>();
        if (source == null)
            return catFilters;
        String[] f_para = source.split("</p>|</h2>|</figure>");
        catFilters.clear();
        for (String s_content : f_para) {
            if (s_content.contains("<p"))
                catFilters.add(new CatFilter("paragraph", remove_tags(s_content)));
            else if (s_content.contains("<h2"))
                catFilters.add(new CatFilter("par_heading", remove_tags(s_content)));
            else if (s_content.contains("<img"))
                catFilters.add(new CatFilter("par_pic", remove_tags(s_content)));
        }
        return catFilters;
    }

    public static String remove_tags(String source) {
        if (source == null)
            return "";
        source = source.replace("&amp;", "&");
        source = source.replace("&#038;", "&");
        source = source.replace("&#8217;s", "'s");
        source = source.replace("[&hellip;]", "");
        source = source.replace("\n", "");
        source = source.replace("<p>", "");
        source = source.replace("</p>", "");
        source = source.replace("<h1>", "");
        source = source.replace("</h1>", "");
        source = source.replace("<h2>", "");
        source = source.replace("</h2>", "");
        source = source.replace("<h3>", "");
        source = source.replace("</h3>", "");
        source = source.replace("<h4>", "");
        source = source.replace("</h4>", "");
        source = source.replace("<h5>", "");
        source = source.replace("</h5>", "");
        source = source.replace("<h6>", "");
        source = source.replace("</h6>", "");
        source = source.replace("<strong>", "");
        source = source.replace("</strong>", "");
        if (source.contains("<a"))
            source = source.replace(source.substring(source.lastIndexOf("<a"), source.lastIndexOf(">") + 1), "");
        source = source.replace("</a>", "");
        if (source.contains("<span"))
            source = source.replace(source.substring(source.lastIndexOf("<span"), source.lastIndexOf(">") + 1), "");
        source = source.replace("</span>", "");
        if (source.contains("<figure"))
            source = source.replace(source.substring(source.lastIndexOf("<figure"), source.lastIndexOf("><img") + 1), "");
        source = source.replace("</figure>", "");
        if (source.contains("<img"))
            if (source.contains("768w") && source.contains("500w"))
                source = source.substring(source.lastIndexOf("768w, ") + 6, source.lastIndexOf(" 500w"));
            else
                source = source.substring(source.lastIndexOf("<img src =\"") + 11, source.lastIndexOf("\" alt="));
        return source.trim();
    }
}
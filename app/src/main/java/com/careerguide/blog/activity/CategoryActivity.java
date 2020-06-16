package com.careerguide.blog.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ZohoSalesiq;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.blog.adapter.CatDetailAdapter;
import com.careerguide.blog.adapter.CategoryAdapter;
import com.careerguide.blog.model.CatFilter;
import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.id_toolbar)
    Toolbar id_toolbar;
    @BindView(R.id.id_rv)
    RecyclerView id_rv;
    @BindView(R.id.Cat_Blog)
    TextView Cat_Blog;
    @BindView(R.id.related_rv)
    RecyclerView related_rv;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer_view_container;
    private CategoryAdapter adapter;
    private List<CatFilter> catFilters;
    private CategoryDetails categoryDetails;
    private CompositeDisposable disposable,disposable1;
    private CatDetailAdapter adapter_related;
    private List<CategoryDetails> categoryDetails_related;
    private ImageView share;
    private Bundle bundle;
    private String dlink, dlink1,dlink2,dlink3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        Typeface font = Typeface.createFromAsset(this.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        Cat_Blog.setTypeface(font);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e("TAG", "onSuccess: "+deepLink );
                            catFilters = new ArrayList<>();
                            adapter = new CategoryAdapter(CategoryActivity.this, catFilters);
                            dlink = deepLink.toString().substring(28);
                            dlink1 = dlink.substring(0, dlink.indexOf('/'));
                            dlink2 = dlink.substring(dlink.indexOf('/')+1,dlink.lastIndexOf('/'));
                            dlink3= dlink.substring(dlink.lastIndexOf('/')+1);
                            Log.e("MainActivity", "onSuccess: " + deepLink + " " + dlink1 + " " + dlink2 + " " + dlink3);
                            disposable1=new CompositeDisposable();
                            disposable1.add(Utils.get_api().get_specific_cat_by_id(dlink2)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                                        @Override
                                        public void onSuccess(List<CategoryDetails> cd) {
                                            if (cd != null) {
                                                for (CategoryDetails c : cd) {
                                                    c.setTitle(Utils.remove_tags(c.getTitle()));
                                                    c.setDesc(Utils.remove_tags(c.getDesc()));
                                                    categoryDetails = c;
                                                    //id_toolbar.setTitle(categoryDetails.getTitle());
                                                }
                                                if (categoryDetails != null) {
                                                    catFilters.clear();
                                                    catFilters.addAll(Utils.filter_content(categoryDetails));
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                            main1();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }
                                    }));




                            //categoryDetails=new Gson().toJson(categoryDetails.get(position);
                        }
                        else {
                            bundle = getIntent().getExtras();

                            if (bundle != null)
                            {
                                Log.e("TAG", "blog: "+bundle.getString("data"));
                                categoryDetails = new Gson().fromJson(bundle.getString("data"), CategoryDetails.class);
                            }
                            main1();
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity", "getDynamicLink:onFailure", e);
                    }
                });





    }

    private void main1()
    {
        disposable = new CompositeDisposable();
        id_toolbar.setTitle(categoryDetails != null ? categoryDetails.getTitle() : getString(R.string.app_name));
        id_toolbar.setNavigationIcon(R.drawable.ic_back);
        id_toolbar.setNavigationOnClickListener(v -> onBackPressed());
        share=(ImageView) findViewById(R.id.blog_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharee(bundle);
            }
        });
        catFilters = new ArrayList<>();
        adapter = new CategoryAdapter(this, catFilters);
        id_rv.setHasFixedSize(true);
        id_rv.setLayoutManager(new LinearLayoutManager(this));
        id_rv.setAdapter(adapter);



        categoryDetails_related = new ArrayList<>();
        adapter_related = new CatDetailAdapter(this, categoryDetails_related);
        related_rv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager_related = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        related_rv.setLayoutManager(mLayoutManager_related);
        related_rv.setAdapter(adapter_related);
        get_data();
    }

    public void sharee(Bundle bundle) {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.careerguide.com/"+ Utility.getUserId(this)+"/"+ categoryDetails.getId() +"/"+androidId))
                .setDomainUriPrefix("https://careerguidestudentblogs.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("blogs")
                                .setMedium("anyone")
                                .setCampaign("example-blogs")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(String.valueOf(id_toolbar.getTitle()))
                                .setDescription("Read the Blog CareerGuide App.")
                                .setImageUrl(Uri.parse(categoryDetails.getPic_url()))
                                .build())
                // Open links with com.example.ios on iOS
                // .setIosParameters(new DynamicLink.IosParameters.Builder("com.careerguide.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("TAG", "shareblogs: " + dynamicLink.getUri());

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(dynamicLinkUri.toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("TAG", "onComplete: " + shortLink);

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.setType("text/plain");
                            //Intent shareI=Intent.createChooser(intent,null);
                            startActivity(intent);
                        } else {
                            Log.e("TAG", "onComplete: error" + task.getException());
                            // Error
                            // ...
                        }
                    }
                });
    }

    private void get_data() {
        if (categoryDetails != null) {
            catFilters.clear();
            catFilters.addAll(Utils.filter_content(categoryDetails));
            adapter.notifyDataSetChanged();
        }


        disposable.add(Utils.get_api().get_cat_detail("10", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                    @Override
                    public void onSuccess(List<CategoryDetails> cd) {
                 //       if (cd != null) {
                            for (CategoryDetails c : cd) {
                                c.setTitle(Utils.remove_tags(c.getTitle()));
                                c.setDesc(Utils.remove_tags(c.getDesc()));
                                categoryDetails_related.add(c);
                                Log.e("#c-->" , "-->" +c);
                            }
                            adapter_related.notifyDataSetChanged();
                        shimmer_view_container.setVisibility(View.INVISIBLE);
                        //}
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                }));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
package com.careerguide.blog.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.adapter.CatDetailAdapter;
import com.careerguide.blog.adapter.CategoryAdapter;
import com.careerguide.blog.model.CatFilter;
import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
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
    private CompositeDisposable disposable;
    private CatDetailAdapter adapter_related;
    private List<CategoryDetails> categoryDetails_related;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        Typeface font = Typeface.createFromAsset(this.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        Cat_Blog.setTypeface(font);
        Bundle bundle = getIntent().getExtras();
        disposable = new CompositeDisposable();
        if (bundle != null)
            categoryDetails = new Gson().fromJson(bundle.getString("data"), CategoryDetails.class);
        id_toolbar.setTitle(categoryDetails != null ? categoryDetails.getTitle() : getString(R.string.app_name));
        id_toolbar.setNavigationIcon(R.drawable.ic_back);
        id_toolbar.setNavigationOnClickListener(v -> onBackPressed());

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
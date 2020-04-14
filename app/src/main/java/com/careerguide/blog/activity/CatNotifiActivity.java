package com.careerguide.blog.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.careerguide.R;
import com.careerguide.blog.adapter.CategoryAdapter;
import com.careerguide.blog.model.CatFilter;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CatNotifiActivity extends AppCompatActivity {
    @BindView(R.id.id_toolbar)
    Toolbar id_toolbar;
    @BindView(R.id.id_rv)
    RecyclerView id_rv;
    @BindView(R.id.id_sw)
    SwipeRefreshLayout id_sw;
    @BindView(R.id.id_progress)
    SpinKitView id_progress;
    private CategoryAdapter adapter;
    private List<CatFilter> catFilters;
    private CategoryDetails categoryDetails;
    private CompositeDisposable disposable;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_notifi);
        ButterKnife.bind(this);
        disposable = new CompositeDisposable();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            id = bundle.getString("id");

        id_toolbar.setTitle(getString(R.string.app_name));
        id_toolbar.setNavigationIcon(R.drawable.ic_back);
        id_toolbar.setNavigationOnClickListener(v -> onBackPressed());

        id_sw.setColorSchemeResources(R.color.colorPrimary);

        catFilters = new ArrayList<>();
        adapter = new CategoryAdapter(this, catFilters);
        id_rv.setHasFixedSize(true);
        id_rv.setLayoutManager(new LinearLayoutManager(this));
        id_rv.setAdapter(adapter);
        id_sw.setOnRefreshListener(this::get_data);
        get_data();
    }

    private void get_data() {
        if (id != null) {
            Utils.show_loader(id_progress);
            disposable.add(Utils.get_api().get_specific_cat_by_id(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                        @Override
                        public void onSuccess(List<CategoryDetails> cd) {
                            Utils.hide_loader(id_progress);
                            id_sw.setRefreshing(false);
                            if (cd != null) {
                                for (CategoryDetails c : cd) {
                                    c.setTitle(Utils.remove_tags(c.getTitle()));
                                    c.setDesc(Utils.remove_tags(c.getDesc()));
                                    categoryDetails = c;
                                    id_toolbar.setTitle(categoryDetails.getTitle());
                                }
                                if (categoryDetails != null) {
                                    catFilters.clear();
                                    catFilters.addAll(Utils.filter_content(categoryDetails));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Utils.hide_loader(id_progress);
                            id_sw.setRefreshing(false);
                        }
                    }));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
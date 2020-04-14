package com.careerguide.blog.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.careerguide.R;
import com.careerguide.blog.adapter.CatDetailAdapter;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Item_Space_Decoration;
import com.careerguide.blog.util.Utils;
import com.github.ybq.android.spinkit.SpinKitView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CatDetailWoActivity extends AppCompatActivity {
    @BindView(R.id.id_toolbar)
    Toolbar id_toolbar;
    @BindView(R.id.id_rv)
    RecyclerView id_rv;
    @BindView(R.id.id_sw)
    SwipeRefreshLayout id_sw;
    @BindView(R.id.id_progress)
    SpinKitView id_progress;
    private CatDetailAdapter adapter;
    private List<CategoryDetails> categoryDetails;
    private LinearLayoutManager lm;
    private CompositeDisposable disposable;
    private boolean loading = false;
    int page_no = 1, past_visible_count, visible_count, total_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_detail_wo);
        ButterKnife.bind(this);
        disposable = new CompositeDisposable();

        id_toolbar.setTitle(getString(R.string.app_name));
        id_toolbar.setNavigationIcon(R.drawable.ic_back);
        id_toolbar.setNavigationOnClickListener(v -> onBackPressed());

        id_sw.setColorSchemeResources(R.color.colorPrimary);

        categoryDetails = new ArrayList<>();
        adapter = new CatDetailAdapter(this, categoryDetails);
        id_rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        id_rv.setLayoutManager(lm);
        id_rv.addItemDecoration(new Item_Space_Decoration(24, 1));
        id_rv.setAdapter(adapter);
        get_data();
        id_sw.setOnRefreshListener(() -> {
            categoryDetails.clear();
            page_no = 1;
            get_data();
        });

        id_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visible_count = lm.getChildCount();
                    total_Count = lm.getItemCount();
                    past_visible_count = lm.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visible_count + past_visible_count) >= total_Count) {
                            page_no++;
                            get_data();
                        }
                    }
                }
            }
        });
    }

    private void get_data() {
        loading = true;
        Utils.show_loader(id_progress);
        disposable.add(Utils.get_api().get_cat_detail_wo("5", String.valueOf(page_no))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                    @Override
                    public void onSuccess(List<CategoryDetails> cd) {
                        loading = false;
                        Utils.hide_loader(id_progress);
                        id_sw.setRefreshing(false);
                        if (cd != null) {
                            for (CategoryDetails c : cd) {
                                c.setTitle(Utils.remove_tags(c.getTitle()));
                                c.setDesc(Utils.remove_tags(c.getDesc()));
                                categoryDetails.add(c);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                        Utils.hide_loader(id_progress);
                        id_sw.setRefreshing(false);
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
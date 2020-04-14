package com.careerguide.blog.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.adapter.CategoryAdapter;
import com.careerguide.blog.model.CatFilter;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.id_toolbar)
    Toolbar id_toolbar;
    @BindView(R.id.id_rv)
    RecyclerView id_rv;
    private CategoryAdapter adapter;
    private List<CatFilter> catFilters;
    private CategoryDetails categoryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
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
        get_data();
    }

    private void get_data() {
        if (categoryDetails != null) {
            catFilters.clear();
            catFilters.addAll(Utils.filter_content(categoryDetails));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
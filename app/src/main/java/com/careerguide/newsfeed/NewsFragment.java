package com.careerguide.newsfeed;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careerguide.R;
import com.careerguide.blog.DataMembers;
import com.careerguide.blog.RecyclerAdapter_Nav;
import com.careerguide.blog.adapter.CatDetailAdapter;
import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Item_Space_Decoration;
import com.careerguide.blog.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.prof.rssparser.Article;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private MainViewModel viewModel;
    private LinearLayout relativeLayout;
    private TextView blogCard;
    private TextView googleCard;

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    public NewsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        progressBar = view.findViewById(R.id.news_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        blogCard = view.findViewById(R.id.cgblog_btn);
        googleCard = view.findViewById(R.id.google_btn);

        blogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleCard.setBackgroundResource(R.drawable.article_button_border);
                googleCard.setTextColor(getResources().getColor(R.color.app_blue));
                blogCard.setBackgroundResource(R.drawable.article_button_border_selected);
                blogCard.setTextColor(getResources().getColor(R.color.white));
                loadBlogPosts();
            }
        });

        googleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blogCard.setBackgroundResource(R.drawable.article_button_border);
                blogCard.setTextColor(getResources().getColor(R.color.app_blue));
                googleCard.setBackgroundResource(R.drawable.article_button_border_selected);
                googleCard.setTextColor(getResources().getColor(R.color.white));
                loadGoogleFeeds();
            }
        });

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        relativeLayout = view.findViewById(R.id.root_layout);


       view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK &&  event.getAction() == KeyEvent.ACTION_DOWN )
                {

                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_homeFragment);
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    return true;
                }
                return false;
            }
        } );



        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.canChildScrollUp();

        blogCard.setBackgroundResource(R.drawable.article_button_border_selected);
        blogCard.setTextColor(getResources().getColor(R.color.white));
        loadBlogPosts();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadGoogleFeeds(){
        FeedRepo feedRepo =new FeedRepo();
        List<Article> cached= new List<Article>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Article> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] ts) {
                return null;
            }

            @Override
            public boolean add(Article article) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Article> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, Collection<? extends Article> collection) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Article get(int i) {
                return null;
            }

            @Override
            public Article set(int i, Article article) {
                return null;
            }

            @Override
            public void add(int i, Article article) {

            }

            @Override
            public Article remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Article> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Article> listIterator(int i) {
                return null;
            }

            @Override
            public List<Article> subList(int i, int i1) {
                return null;
            }
        };

        if(feedRepo.getFeeds(getContext(),"")!=null)
            cached= feedRepo.getFeeds(getContext(),"");
        progressBar.setVisibility(View.VISIBLE);
        mAdapter = new ArticleAdapter(cached, getContext(),navController);
        if (cached != null) {

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            if(cached.size()>2)
                progressBar.setVisibility(View.GONE);

        }

        viewModel.getArticleList().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null) {

                    mAdapter.updateList(articles);
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        viewModel.getSnackbar().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Snackbar.make(relativeLayout, s, Snackbar.LENGTH_LONG).show();
                    viewModel.onSnackbarShowed();
                }
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //     mAdapter.getArticleList().clear();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(true);
                viewModel.fetchFeed(NewsUtility.NEWS_URL,"");
            }
        });


        if (!isNetworkAvailable()) {

            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        } else  {
            viewModel.fetchFeed(NewsUtility.NEWS_URL,"");
        }
    }




    private LinearLayoutManager lm;
    private CompositeDisposable disposable;
    private CatDetailAdapter adapter;
    private List<CategoryDetails> categoryDetails;
    private Categories categories;
    private boolean loading = false;
    int page_no = 1, past_visible_count, visible_count, total_Count;

    private void loadBlogPosts(){

        disposable = new CompositeDisposable();
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
            categories = new Gson().fromJson(bundle.getString("data"), Categories.class);
        categoryDetails = new ArrayList<>();
        adapter = new CatDetailAdapter(getContext(), categoryDetails);
        adapter.setSeeAllMode(true);
        mRecyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(adapter);
        get_data();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            categoryDetails.clear();
            page_no = 1;
            get_data();
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void get_data() {
        loading = true;
        progressBar.setVisibility(View.VISIBLE);
        if (categories != null && categories.getId() != null) {
            disposable.add(Utils.get_api().get_specific_cat_detail(categories.getId(), "8", String.valueOf(page_no))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                        @Override
                        public void onSuccess(List<CategoryDetails> cd) {
                            loading = false;
                          progressBar.setVisibility(View.GONE);
                           mSwipeRefreshLayout.setRefreshing(false);
                            if (cd != null) {
                                for (CategoryDetails c : cd) {
                                    c.setTitle(Utils.remove_tags(c.getTitle()));
                                    c.setDesc(Utils.remove_tags(c.getDesc()));
                                    Log.e("#c" , "-->" +c);
                                    categoryDetails.add(c);
                                }
                                adapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            loading = false;
                           // Utils.hide_loader(id_progress);
                           mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }));
        } else {
            disposable.add(Utils.get_api().get_cat_detail("5", String.valueOf(page_no))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                        @Override
                        public void onSuccess(List<CategoryDetails> cd) {
                            loading = false;
                            progressBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
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
                         //   Utils.hide_loader(id_progress);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }));
        }
    }



}

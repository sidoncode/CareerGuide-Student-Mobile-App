package com.careerguide.newsfeed;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.careerguide.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.prof.rssparser.Article;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment{


    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private MainViewModel viewModel;
    private LinearLayout relativeLayout;

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    public NewsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        progressBar = view.findViewById(R.id.news_progress_bar);

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
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {

                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_homeFragment);
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    return true;
                }
                return false;
            }
        } );


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
        mAdapter = new ArticleAdapter(cached, getContext());
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

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.canChildScrollUp();
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



        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}

package com.careerguide.newsfeed;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<Article>> articleListLive = null;
    private String urlString = "";
    private List<Article> articles;
    FeedRepo feedRepo;

    private MutableLiveData<String> snackbar = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        feedRepo =new FeedRepo();
    }

    public MutableLiveData<List<Article>> getArticleList() {
        if (articleListLive == null) {
            articleListLive = new MutableLiveData<>();
        }
        return articleListLive;
    }

    private void setArticleList(List<Article> articleList) {
        this.articleListLive.postValue(articleList);
    }

    public LiveData<String> getSnackbar() {
        return snackbar;
    }

    public void onSnackbarShowed() {
        snackbar.setValue(null);
    }

    public void fetchFeed(String url, final String cacheFileName) {
        urlString = url;

        Parser parser = new Parser();
        parser.onFinish(new OnTaskCompleted() {

            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(List<Article> list) {

                Collections.sort(list
                        ,new NewsUtility.CustomComparator());
                ArrayList<Article> shortList=new ArrayList<>();
                if(list.size()>0) {
                    for(int i=0;i<15;i++){
                        shortList.add(list.get(i));
                    }


                    setArticleList(shortList);
                    feedRepo.saveFeeds(getApplication().getApplicationContext(), shortList,cacheFileName);
                }

            }

            //what to do in case of error
            @Override
            public void onError(Exception e) {

                e.printStackTrace();
                snackbar.postValue("An error has occurred. Please try again");
            }
        });

        parser.execute(urlString);

    }





}
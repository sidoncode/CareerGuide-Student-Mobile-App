package com.careerguide.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.prof.rssparser.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;
    private Context mContext;


    public ArticleAdapter(List<Article> list, Context context) {
        this.articles = list;
        this.mContext = context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        return new ViewHolder(v);
    }


    void filter(String text){
        List<Article> temp = new ArrayList();
        for(Article d: articles){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getTitle().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        updateList(temp);
    }
    public void updateList(List<Article> list){
        articles = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        int colorType=position%5;
        switch (colorType){
            case 0 : viewHolder.colorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.fire_green)); break;
            case 1 : viewHolder.colorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.fire_violet)); break;
            case 2 : viewHolder.colorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.fire_yellow)); break;
            case 3 : viewHolder.colorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.fire_red)); break;
            case 4 : viewHolder.colorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.fire_sky)); break;
        }


        Article currentArticle = articles.get(position);

        String pubDateString;
        try {
            String sourceDateString = currentArticle.getPubDate();
            SimpleDateFormat sourceSdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date date = sourceSdf.parse(sourceDateString);
            String niceDateStr = DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
            pubDateString=niceDateStr;

        } catch (ParseException e) {
            e.printStackTrace();
            pubDateString = currentArticle.getPubDate();
        }

        viewHolder.title.setText(currentArticle.getTitle());
        viewHolder.pubDate.setText(pubDateString);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String url = articles.get(viewHolder.getAdapterPosition()).getLink();
                Intent intent=new Intent(mContext,FeedViewActivity.class);
                intent.putExtra("news_url",url);
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        View colorView;

        public ViewHolder(View itemView) {

            super(itemView);

            colorView = itemView.findViewById(R.id.color_view_feed);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.pubDate);

        }
    }


}
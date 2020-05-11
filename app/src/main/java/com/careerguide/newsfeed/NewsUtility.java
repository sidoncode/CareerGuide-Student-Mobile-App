package com.careerguide.newsfeed;

import com.prof.rssparser.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class NewsUtility {
    public static final String NEWS_URL="https://news.google.com/rss/search?q=education+-mindler+-idreamcareer+-careers360+-collegedunia+-shiksha+-naukri+-univariety+-leverageedu+-dheya+-edumilestone+-jagranjosh";
    public static class CustomComparator implements Comparator<Article> {
        @Override
        public int compare(Article o1, Article o2) {


            SimpleDateFormat sourceSdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            Date date1=new Date();
            Date date2=new Date();
            try {
                date1 = sourceSdf.parse(o1.getPubDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                date2 = sourceSdf.parse(o2.getPubDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date2.compareTo(date1);
        }
    }

}

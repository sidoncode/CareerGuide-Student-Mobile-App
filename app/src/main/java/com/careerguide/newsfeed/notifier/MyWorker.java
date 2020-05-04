package com.careerguide.newsfeed.notifier;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.careerguide.R;
import com.careerguide.newsfeed.FeedRepo;
import com.careerguide.newsfeed.NewsFeedActivity;
import com.careerguide.newsfeed.NewsUtility;
import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {
    private static final String CHANNEL_ID = "1000" ;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try{
        myWork();
            return Result.success();
        }
        catch (Throwable throwable){
            return Result.failure();
        }

    }

    private void myWork() {


        Parser parser = new Parser();
        FeedRepo feedRepo=new FeedRepo();
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
                    feedRepo.saveFeeds(getApplicationContext(), shortList,"");
                    showNotification(shortList.get(0).getTitle());
                    rescheduleForNextDay();
                }

            }

            //what to do in case of error
            @Override
            public void onError(Exception e) {

                e.printStackTrace();
            }
        });

        parser.execute(NewsUtility.NEWS_URL);


    }

    private void rescheduleForNextDay(){
        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();
        dueDate.set(Calendar.HOUR_OF_DAY, 15);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }
        long timeDiff=dueDate.getTimeInMillis()-currentDate.getTimeInMillis();
        OneTimeWorkRequest dailyWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag("NEWS_WORK")
                .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueue(dailyWorkRequest);
    }

    private void showNotification(String title){
        Intent intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_tabs)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText("Career Guide's News Feed.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Career and Educational News";
            String description = "Educational and career news from the app";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

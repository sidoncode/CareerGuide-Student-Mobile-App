package com.careerguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
/*import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Subscriber;
import com.opentok.android.OpentokError;*/
import android.support.annotation.NonNull;
import android.Manifest;
import android.widget.FrameLayout;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class TokBoxVideoCall extends AppCompatActivity /*implements  Session.SessionListener, PublisherKit.PublisherListener*/{

    private static String API_KEY = "46132692";
    private static String SESSION_ID = "2_MX40NjEzMjY5Mn5-MTUyODE4MzM0MjcxN35oMWo1ajdyZHpMT3BpL0JBRjRWRGxiUlN-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjEzMjY5MiZzaWc9ZTIxOWRkYzFiNmY5Y2I2OGRjNzRlYzljYTgyYjY5OGY4MzE2YTRhMDpzZXNzaW9uX2lkPTJfTVg0ME5qRXpNalk1TW41LU1UVXlPREU0TXpNME1qY3hOMzVvTVdvMWFqZHlaSHBNVDNCcEwwSkJSalJXUkd4aVVsTi1mZyZjcmVhdGVfdGltZT0xNTI4MTgzMzgyJm5vbmNlPTAuODcyNTkwMTUxMDQ4ODAxNCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTI4MTg2OTgxJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = TokBoxVideoCall.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    /*private Publisher mPublisher;
    private Subscriber mSubscriber;



    private Session mSession;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tok_box_video_call);

        /*requestPermissions();*/
    }

    /*@AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);


            // initialize and connect to the session
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);
            mSession.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "We needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // SessionListener methods

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");

        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());
        mSession.publish(mPublisher);
    }


    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }


    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    // PublisherListener methods

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }*/


}

package com;

import android.app.Application;
import android.content.Intent;
import com.careerguide.blog.activity.CatNotifiActivity;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class ZohoSalesiq extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
      //  ZohoSalesIQ.init(this, "93vXBwoY3Y7wDyuFl68S693hm4hkDaiQLc2XBTAkJFqWF2q16oqxqn%2B26NBE5rd%2FGVUUocMgx84%3D", "msngIggc397QDmMC9BjXrRNSbQDHDDIo9dSvdiX4p514hpTOtO4DUMtddmV841wb3w0klKjhLEk6GNKOvBti8Z9%2B1Eaxy4cGhNr2yafIPJN8vW7oI8g3zIGAZ1wbzUOMIiotxJp4SoFJyUKzr2wbrBPCANC9LnQa");
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ZohoSalesiq.Noti_Handler())
                .init();
    }

    private class Noti_Handler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            JSONObject data = result.notification.payload.additionalData;
            if (data != null) {
                Intent intent = new Intent(ZohoSalesiq.this, CatNotifiActivity.class);
                intent.putExtra("id", data.optString("id"));
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }


}

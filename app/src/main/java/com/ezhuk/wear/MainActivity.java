// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTestNotification();
        showTestNotificationWithPages();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showTestNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.content_title))
                .setContentText(getString(R.string.content_text));

        NotificationManagerCompat.from(this).notify(0,
                new WearableNotifications.Builder(builder).build());
    }

    private void showTestNotificationWithPages() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.page1_title))
                .setContentText(getString(R.string.page1_text));

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle(getString(R.string.page2_title))
                .bigText(getString(R.string.page2_text));

        Notification second = new NotificationCompat.Builder(this)
                .setStyle(style)
                .build();

        NotificationManagerCompat.from(this).notify(1,
                new WearableNotifications.Builder(builder)
                        .addPage(second)
                        .build());
    }
}

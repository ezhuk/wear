// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;


public class MainActivity extends Activity {
    private static final String NOTIFICATION_GROUP = "notification_group";

    private static final String ACTION_TEST = "com.ezhuk.wear.ACTION";
    private static final String ACTION_EXTRA = "action";

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
        showTestNotificationWithAction();
        showTestNotificationWithInputForPrimaryAction();
        showTestNotificationWithInputForSecondaryAction();
        showGroupNotifications();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showTestNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.content_title))
                        .setContentText(getString(R.string.content_text));

        NotificationManagerCompat.from(this).notify(0,
                new WearableNotifications.Builder(builder).build());
    }

    private void showTestNotificationWithPages() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.page1_title))
                        .setContentText(getString(R.string.page1_text));

        NotificationCompat.BigTextStyle style =
                new NotificationCompat.BigTextStyle();
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

    private void showTestNotificationWithAction() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("");
        intent.setData(uri);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.action_title))
                        .setContentText(getString(R.string.action_text))
                        .addAction(R.drawable.ic_launcher,
                                getString(R.string.action_button),
                                pendingIntent);

        NotificationManagerCompat.from(this).notify(2,
                new WearableNotifications.Builder(builder).build());
    }

    private void showTestNotificationWithInputForPrimaryAction() {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.action_title))
                        .setContentText(getString(R.string.action_text))
                        .setContentIntent(pendingIntent);

        String[] choices =
                getResources().getStringArray(R.array.input_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(getString(R.string.action_label))
                .setChoices(choices)
                .build();

        NotificationManagerCompat.from(this).notify(3,
                new WearableNotifications.Builder(builder)
                        .addRemoteInputForContentIntent(remoteInput)
                        .build()
        );
    }

    private void showTestNotificationWithInputForSecondaryAction() {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(getString(R.string.action_label))
                .build();

        WearableNotifications.Action action =
                new WearableNotifications.Action.Builder(
                        R.drawable.ic_launcher,
                        "Action",
                        pendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.action_title));

        NotificationManagerCompat.from(this).notify(4,
                new WearableNotifications.Builder(builder)
                        .addAction(action)
                        .build()
        );
    }

    private void showGroupNotifications() {
        Notification first = new WearableNotifications.Builder(
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.page1_title))
                        .setContentText(getString(R.string.page1_text)))
                .setGroup(NOTIFICATION_GROUP)
                .build();

        Notification second = new WearableNotifications.Builder(
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.page2_title))
                        .setContentText(getString(R.string.page2_text)))
                .setGroup(NOTIFICATION_GROUP)
                .build();

        Notification summary = new WearableNotifications.Builder(
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.summary_title))
                        .setContentText(getString(R.string.summary_text)))
                .setGroup(NOTIFICATION_GROUP,
                        WearableNotifications.GROUP_ORDER_SUMMARY)
                .build();

        NotificationManagerCompat.from(this).notify(5, first);
        NotificationManagerCompat.from(this).notify(6, second);
        NotificationManagerCompat.from(this).notify(7, summary);
    }
}

// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;


public class NotificationUtils {
    private static final String ACTION_TEST = "com.ezhuk.wear.ACTION";
    private static final String ACTION_EXTRA = "action";

    private static final String NOTIFICATION_GROUP = "notification_group";

    public static void showTestNotification(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text));

        NotificationManagerCompat.from(context).notify(0,
                new WearableNotifications.Builder(builder)
                        .build());
    }

    public static void showTestNotificationWithPages(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.page1_title))
                        .setContentText(context.getString(R.string.page1_text));

        NotificationCompat.BigTextStyle style =
                new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(context.getString(R.string.page2_title))
                        .bigText(context.getString(R.string.page2_text));

        Notification second = new NotificationCompat.Builder(context)
                .setStyle(style)
                .build();

        NotificationManagerCompat.from(context).notify(1,
                new WearableNotifications.Builder(builder)
                        .addPage(second)
                        .build());
    }

    public static void showTestNotificationWithAction(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(""));
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.action_title))
                        .setContentText(context.getString(R.string.action_text))
                        .addAction(R.drawable.ic_launcher,
                                context.getString(R.string.action_button),
                                pendingIntent);

        NotificationManagerCompat.from(context).notify(2,
                new WearableNotifications.Builder(builder)
                        .build());
    }

    public static void showTestNotificationWithInputForPrimaryAction(Context context) {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.action_title))
                        .setContentText(context.getString(R.string.action_text))
                        .setContentIntent(pendingIntent);

        String[] choices =
                context.getResources().getStringArray(R.array.input_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(context.getString(R.string.action_label))
                .setChoices(choices)
                .build();

        NotificationManagerCompat.from(context).notify(3,
                new WearableNotifications.Builder(builder)
                        .addRemoteInputForContentIntent(remoteInput)
                        .build());
    }

    public static void showTestNotificationWithInputForSecondaryAction(Context context) {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(context.getString(R.string.action_label))
                .build();

        WearableNotifications.Action action =
                new WearableNotifications.Action.Builder(
                        R.drawable.ic_launcher,
                        "Action",
                        pendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getString(R.string.action_title));

        NotificationManagerCompat.from(context).notify(4,
                new WearableNotifications.Builder(builder)
                        .addAction(action)
                        .build());
    }

    public static void showGroupNotifications(Context context) {
        Notification first = new WearableNotifications.Builder(
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.page1_title))
                        .setContentText(context.getString(R.string.page1_text)))
                .setGroup(NOTIFICATION_GROUP)
                .build();

        Notification second = new WearableNotifications.Builder(
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.page2_title))
                        .setContentText(context.getString(R.string.page2_text)))
                .setGroup(NOTIFICATION_GROUP)
                .build();

        Notification summary = new WearableNotifications.Builder(
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.summary_title))
                        .setContentText(context.getString(R.string.summary_text)))
                .setGroup(NOTIFICATION_GROUP, WearableNotifications.GROUP_ORDER_SUMMARY)
                .build();

        NotificationManagerCompat.from(context).notify(5, first);
        NotificationManagerCompat.from(context).notify(6, second);
        NotificationManagerCompat.from(context).notify(7, summary);
    }
}

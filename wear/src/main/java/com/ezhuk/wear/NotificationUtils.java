// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.RemoteInput;


public class NotificationUtils {
    private static final String ACTION_TEST = "com.ezhuk.wear.ACTION";
    private static final String ACTION_EXTRA = "action";

    private static final String NOTIFICATION_GROUP = "notification_group";

    public static void showNotification(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text));

        NotificationManagerCompat.from(context)
                .notify(0, builder.build());
    }

    public static void showNotificationNoIcon(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text))
                        .extend(new WearableExtender()
                                .setHintHideIcon(true));

        NotificationManagerCompat.from(context)
                .notify(1, builder.build());
    }

    public static void showNotificationMinPriority(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text))
                        .setPriority(NotificationCompat.PRIORITY_MIN);

        NotificationManagerCompat.from(context)
                .notify(2, builder.build());
    }

    public static void showNotificationWithStyle(Context context,
                                                 int id,
                                                 NotificationCompat.Style style) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setStyle(style);

        NotificationManagerCompat.from(context)
                .notify(id, builder.build());
    }

    public static void showNotificationBigTextStyle(Context context) {
        showNotificationWithStyle(context, 3,
                new NotificationCompat.BigTextStyle()
                        .setSummaryText(context.getString(R.string.summary_text))
                        .setBigContentTitle("Big Text Style")
                        .bigText("Sample big text."));
    }

    public static void showNotificationBigPictureStyle(Context context) {
        showNotificationWithStyle(context, 4,
                new NotificationCompat.BigPictureStyle()
                        .setSummaryText(context.getString(R.string.summary_text))
                        .setBigContentTitle("Big Picture Style")
                        .bigPicture(BitmapFactory.decodeResource(
                                context.getResources(), R.drawable.background)));
    }

    public static void showNotificationInboxStyle(Context context) {
        showNotificationWithStyle(context, 5,
                new NotificationCompat.InboxStyle()
                        .setSummaryText(context.getString(R.string.summary_text))
                        .setBigContentTitle("Inbox Style")
                        .addLine("Line 1")
                        .addLine("Line 2"));
    }

    public static void showNotificationWithPages(Context context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.page1_title))
                        .setContentText(context.getString(R.string.page1_text));

        Notification second = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.page2_title))
                .setContentText(context.getString(R.string.page2_text))
                .build();

        NotificationManagerCompat.from(context)
                .notify(6, new WearableExtender()
                        .addPage(second)
                        .extend(builder)
                        .build());
    }

    public static void showNotificationWithAction(Context context) {
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

        NotificationManagerCompat.from(context)
                .notify(7, builder.build());
    }

    public static void showNotificationWithInputForPrimaryAction(Context context) {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        String[] choices =
                context.getResources().getStringArray(R.array.input_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(context.getString(R.string.action_label))
                .setChoices(choices)
                .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        "Action",
                         pendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.action_title))
                        .setContentText(context.getString(R.string.action_text))
                        .setContentIntent(pendingIntent)
                        .extend(new WearableExtender()
                                .addAction(action));

        NotificationManagerCompat.from(context)
                .notify(8, builder.build());
    }

    public static void showNotificationWithInputForSecondaryAction(Context context) {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(context.getString(R.string.action_label))
                .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        "Action",
                        pendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getString(R.string.action_title))
                        .extend(new WearableExtender()
                                .addAction(action));

        NotificationManagerCompat.from(context)
                .notify(9, builder.build());
    }

    public static void showGroupNotifications(Context context) {
        Notification first = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.page1_title))
                .setContentText(context.getString(R.string.page1_text))
                .setGroup(NOTIFICATION_GROUP)
                .build();

        Notification second = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.page2_title))
                .setContentText(context.getString(R.string.page2_text))
                .setGroup(NOTIFICATION_GROUP)
                .build();

        Notification summary = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.summary_title))
                .setContentText(context.getString(R.string.summary_text))
                .setGroup(NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .build();

        NotificationManagerCompat.from(context).notify(10, first);
        NotificationManagerCompat.from(context).notify(11, second);
        NotificationManagerCompat.from(context).notify(12, summary);
    }
}

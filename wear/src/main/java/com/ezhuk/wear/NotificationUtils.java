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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.RemoteInput;


public class NotificationUtils {
    private static final String ACTION_TEST = "com.ezhuk.wear.ACTION";
    private static final String ACTION_EXTRA = "action";

    private static final String NOTIFICATION_GROUP = "notification_group";

    private static int NOTIFICATION_ID = 0;

    private static synchronized int getNewID() {
        return NOTIFICATION_ID++;
    }

    public static void showNotification(Context context) {
        NotificationManagerCompat.from(context).notify(getNewID(),
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text))
                        .build());
    }

    public static void showNotificationNoIcon(Context context) {
        NotificationManagerCompat.from(context).notify(getNewID(),
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text))
                        .extend(new WearableExtender().setHintHideIcon(true))
                        .build());
    }

    private static void showNotificationWithPriority(Context context, int id, int priority) {
        NotificationManagerCompat.from(context).notify(id,
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.content_title))
                        .setContentText(context.getString(R.string.content_text))
                        .setPriority(priority)
                        .build());
    }

    public static void showNotificationMinPriority(Context context) {
        showNotificationWithPriority(context, getNewID(),
                NotificationCompat.PRIORITY_MIN);
    }

    private static void showNotificationWithStyle(Context context, int id,
                                                  NotificationCompat.Style style) {
        NotificationManagerCompat.from(context).notify(id,
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setStyle(style)
                        .build());
    }

    public static void showNotificationBigTextStyle(Context context) {
        showNotificationWithStyle(context, getNewID(),
                new NotificationCompat.BigTextStyle()
                        .setSummaryText(context.getString(R.string.summary_text))
                        .setBigContentTitle("Big Text Style")
                        .bigText("Sample big text."));
    }

    public static void showNotificationBigPictureStyle(Context context) {
        showNotificationWithStyle(context, getNewID(),
                new NotificationCompat.BigPictureStyle()
                        .setSummaryText(context.getString(R.string.summary_text))
                        .setBigContentTitle("Big Picture Style")
                        .bigPicture(BitmapFactory.decodeResource(
                                context.getResources(), R.drawable.background)));
    }

    public static void showNotificationInboxStyle(Context context) {
        showNotificationWithStyle(context, getNewID(),
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
                .notify(getNewID(), new WearableExtender()
                        .addPage(second)
                        .extend(builder)
                        .build());
    }

    public static void showNotificationWithAction(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(""));
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        context.getString(R.string.action_button), pendingIntent)
                        .build();

        NotificationManagerCompat.from(context).notify(getNewID(),
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.action_title))
                        .setContentText(context.getString(R.string.action_text))
                        .addAction(action)
                        .build());
    }

    public static void showNotificationWithInputForPrimaryAction(Context context) {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(context.getString(R.string.action_label))
                .setChoices(context.getResources().getStringArray(R.array.input_choices))
                .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        "Action", pendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationManagerCompat.from(context).notify(getNewID(),
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(context.getString(R.string.action_title))
                        .setContentText(context.getString(R.string.action_text))
                        .setContentIntent(pendingIntent)
                        .extend(new WearableExtender().addAction(action))
                        .build());
    }

    public static void showNotificationWithInputForSecondaryAction(Context context) {
        Intent intent = new Intent(ACTION_TEST);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder(ACTION_EXTRA)
                .setLabel(context.getString(R.string.action_label))
                .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher, "Action", pendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationManagerCompat.from(context).notify(getNewID(),
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getString(R.string.action_title))
                        .extend(new WearableExtender().addAction(action))
                        .build());
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

        NotificationManagerCompat.from(context).notify(getNewID(), first);
        NotificationManagerCompat.from(context).notify(getNewID(), second);
        NotificationManagerCompat.from(context).notify(getNewID(), summary);
    }
}

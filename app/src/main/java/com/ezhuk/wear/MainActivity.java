// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.os.Bundle;

import static com.ezhuk.wear.NotificationUtils.*;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showTestNotification(this);
        showTestNotificationBigTextStyle(this);
        showTestNotificationBigPictureStyle(this);
        showTestNotificationInboxStyle(this);
        showTestNotificationWithPages(this);
        showTestNotificationWithAction(this);
        showTestNotificationWithInputForPrimaryAction(this);
        showTestNotificationWithInputForSecondaryAction(this);
        showGroupNotifications(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

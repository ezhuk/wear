// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import static com.ezhuk.wear.NotificationUtils.*;


public class MainActivity extends Activity
        implements MessageApi.MessageListener,
                   GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener {
    public static final String MESSAGE_PATH = "/message";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button button = (Button) stub.findViewById(R.id.button_show);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showNotifications();
                        moveTaskToBack(true);
                    }
                });
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
 //       if (!mResolvingError) {
            mGoogleApiClient.connect();
 //       }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // empty
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // empty
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(MESSAGE_PATH)) {
            showNotification(this, "", "Message received.");
        }
    }

    private void showNotifications() {
        showNotification(this);
        showNotificationNoIcon(this);
        showNotificationMinPriority(this);
        showNotificationBigTextStyle(this);
        showNotificationBigPictureStyle(this);
        showNotificationInboxStyle(this);
        showNotificationWithPages(this);
        showNotificationWithAction(this);
        showNotificationWithInputForPrimaryAction(this);
        showNotificationWithInputForSecondaryAction(this);
        showGroupNotifications(this);
    }
}

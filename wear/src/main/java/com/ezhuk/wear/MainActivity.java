// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.ezhuk.wear.NotificationUtils.*;
import static com.ezhuk.wear.DataUtils.*;


public class MainActivity extends Activity {
    private static final String TAG = "Wear.MainActivity";
    private static final String MESSAGE_PATH = "/message";
    private static final String DATA_PATH = "/data";
    private static final int SPEECH_REQUEST_CODE = 1;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button buttonShow = (Button) stub.findViewById(R.id.button_show);
                buttonShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showNotifications();
                        moveTaskToBack(true);
                    }
                });

                Button buttonSendMsg = (Button) stub.findViewById(R.id.button_send_msg);
                buttonSendMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSpeechRecognizer();
                    }
                });

                Button buttonSendData = (Button) stub.findViewById(R.id.button_send_data);
                buttonSendData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.background);
                        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
                        sendData(mGoogleApiClient, DATA_PATH,
                                Asset.createFromBytes(byteStream.toByteArray()));
                        showConfirmation();
                    }
                });
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new ConnectionCallbacks())
                .addOnConnectionFailedListener(new ConnectionFailedListener())
                .addApi(Wearable.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mGoogleApiClient && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            // empty
        }

        @Override
        public void onConnectionSuspended(int i) {
            // empty
        }
    }

    private class ConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            // empty
        }
    }

    private void showSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SPEECH_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (0 < results.size()) {
                sendMessage(mGoogleApiClient, MESSAGE_PATH, results.get(0));
                showConfirmation();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showConfirmation() {
        Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "OK");
        startActivity(intent);
    }

    private void showNotifications() {
        showNotification(this);
        showNotificationNoIcon(this);
        showNotificationMinPriority(this);
        showNotificationBigTextStyle(this, "Sample big text.");
        showNotificationBigPictureStyle(this,
                BitmapFactory.decodeResource(getResources(), R.drawable.background));
        showNotificationInboxStyle(this);
        showNotificationWithPages(this);
        showNotificationWithAction(this);
        showNotificationWithInputForPrimaryAction(this);
        showNotificationWithInputForSecondaryAction(this);
        showGroupNotifications(this, "Group");
    }
}

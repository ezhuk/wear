// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.ezhuk.wear.common.DataListener;
import com.ezhuk.wear.common.MessageListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {
    private static final String TAG = "Mobile.MainActivity";
    private static final String MESSAGE_PATH = "/message";
    private static final String DATA_PATH = "/data";

    private GoogleApiClient mGoogleApiClient;
    private MessageListener mMessageListener;
    private DataListener mDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new ConnectionCallbacks())
                .addOnConnectionFailedListener(new ConnectionFailedListener())
                .addApi(Wearable.API)
                .build();

        mMessageListener = new MessageListener();
        mMessageListener.addCallback(MESSAGE_PATH, new MessageCallback());

        mDataListener = new DataListener();
        mDataListener.addCallback(DATA_PATH, new DataCallback());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            removeListeners();
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void addListeners() {
        Wearable.MessageApi.addListener(mGoogleApiClient, mMessageListener);
        Wearable.DataApi.addListener(mGoogleApiClient, mDataListener);
    }

    private void removeListeners() {
        Wearable.MessageApi.removeListener(mGoogleApiClient, mMessageListener);
        Wearable.DataApi.removeListener(mGoogleApiClient, mDataListener);
    }

    private class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            addListeners();
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

    private void showMessage(String message) {
        final String param = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), param, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MessageCallback implements MessageListener.Callback {
        @Override
        public void onMessageReceived(MessageEvent event) {
            showMessage(new String(event.getData()));
        }
    }

    private void showAsset(Bitmap bitmap) {
        final Bitmap param = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView view = (ImageView) findViewById(R.id.image_view);
                view.setImageBitmap(param);
            }
        });
    }

    private void hideAsset() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView view = (ImageView) findViewById(R.id.image_view);
                view.setImageDrawable(null);
            }
        });
    }

    private class DataCallback implements DataListener.Callback {
        private static final String ASSET_KEY = "data";
        private static final int TIMEOUT = 30;

        @Override
        public void onDataChanged(DataEvent event) {
            if (DataEvent.TYPE_CHANGED == event.getType()) {
                DataMapItem item = DataMapItem.fromDataItem(event.getDataItem());
                Asset asset = item.getDataMap().getAsset(ASSET_KEY);

                ConnectionResult result = mGoogleApiClient
                        .blockingConnect(TIMEOUT, TimeUnit.SECONDS);
                if (result.isSuccess()) {
                    InputStream stream = Wearable.DataApi
                            .getFdForAsset(mGoogleApiClient, asset)
                            .await().getInputStream();
                    if (null != asset) {
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        showAsset(bitmap);
                    }
                }
            } else if (DataEvent.TYPE_DELETED == event.getType()) {
                hideAsset();
            }
        }
    }
}

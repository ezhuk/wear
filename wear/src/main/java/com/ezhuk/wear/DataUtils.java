// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;


public class DataUtils {
    private static class SendMessageTask extends AsyncTask<Object, Void, Void> {
        private static final String TAG = "MessageUtils.SendMessageTask";
        protected Void doInBackground(Object... params) {
            GoogleApiClient client = (GoogleApiClient)params[0];
            String path = (String)params[1];
            String text = (String)params[2];

            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
                    .getConnectedNodes(client).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi
                        .sendMessage(client, node.getId(), path, text.getBytes())
                        .await();
                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG, "[ERROR] could not send message (" + result.getStatus() + ")");
                }
            }

            return null;
        }
    }

    private static class SendDataTask extends AsyncTask<Object, Void, Void> {
        private static final String TAG = "MessageUtils.SendDataTask";
        protected Void doInBackground(Object... params) {
            GoogleApiClient client = (GoogleApiClient)params[0];
            String path = (String)params[1];
            Asset asset = (Asset)params[2];

            PutDataMapRequest dataMap = PutDataMapRequest.create(path);
            dataMap.getDataMap().putAsset("data", asset);
            PutDataRequest request = dataMap.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi
                    .putDataItem(client, request).await();
            if (!result.getStatus().isSuccess()) {
                Log.e(TAG, "[ERROR] could not send data (" + result.getStatus() + ")");
            }

            return null;
        }
    }

    public static void sendMessage(GoogleApiClient client, String path, String text) {
        new SendMessageTask().execute(client, path, text);
    }

    public static void sendData(GoogleApiClient client, String path, Asset asset) {
        new SendDataTask().execute(client, path, asset);
    }
}

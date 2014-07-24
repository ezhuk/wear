// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MessageUtils {
    private static class SendTextTask extends AsyncTask<Object, Void, Void> {
        private static final String TAG = "MessageUtils.SendTextTask";
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

    public static void sendMessage(GoogleApiClient client, String path, String text) {
        new SendTextTask().execute(client, path, text);
    }
}

// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String MESSAGE_PATH = "/message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onSendText(View view) {
        EditText editText = (EditText) findViewById(R.id.text);
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            editText.setText("");
            sendText(text);
        } else {
            Toast.makeText(this, "Enter text", Toast.LENGTH_SHORT).show();
        }
    }

    private class SendTextTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params) {
            GoogleApiClient client = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(Wearable.API).build();
            client.connect();

            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
                    .getConnectedNodes(client).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi
                        .sendMessage(client, node.getId(), MESSAGE_PATH, params[0].getBytes())
                        .await();
                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG, "[ERROR] could not send message (" + result.getStatus() + ")");
                }
            }

            return null;
        }
    }

    private void sendText(String text) {
        new SendTextTask().execute(text);
    }
}

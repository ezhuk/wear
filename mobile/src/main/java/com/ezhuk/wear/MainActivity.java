// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SendTextTask";
    private static final String MESSAGE_PATH = "/message";
    private static final String ERROR_DIALOG = "ERR";

    private boolean mResolvingError = false;
    private static final int RESOLVE_ERROR = 1010;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
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
        if (!mResolvingError)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // empty
    }

    @Override
    public void onConnectionSuspended(int i) {
        // empty
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException ex) {
                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int error) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ERROR_DIALOG, error);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), ERROR_DIALOG);
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
            // empty
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(ERROR_DIALOG);
            return GooglePlayServicesUtil
                    .getErrorDialog(errorCode, this.getActivity(), RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity)getActivity()).onDialogDismissed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESOLVE_ERROR == requestCode) {
            mResolvingError = false;

            if (RESULT_OK == resultCode) {
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();
            }
        }
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
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi
                    .getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi
                        .sendMessage(mGoogleApiClient, node.getId(), MESSAGE_PATH, params[0].getBytes())
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

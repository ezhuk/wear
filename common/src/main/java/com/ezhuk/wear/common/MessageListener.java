// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

import java.util.Dictionary;


public class MessageListener implements MessageApi.MessageListener {
    public static interface Callback {
        void onMessageReceived(MessageEvent messageEvent);
    }

    private Dictionary<String, Callback> mCallbacks;

    public void addCallback(String path, Callback callback) {
        mCallbacks.put(path, callback);
    }

    public void removeCallback(String path) {
        mCallbacks.remove(path);
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        Callback callback = mCallbacks.get(event.getPath());
        if (null != callback)
            callback.onMessageReceived(event);
    }
}

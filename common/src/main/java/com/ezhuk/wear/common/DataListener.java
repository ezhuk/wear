// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;

import java.util.Dictionary;


public class DataListener implements DataApi.DataListener {
    public static interface Callback {
        public void onDataChanged(DataEvent event);
    }

    private Dictionary<String, Callback> mCallbacks;

    public void addCallback(String path, Callback callback) {
        mCallbacks.put(path, callback);
    }

    public Callback getCallback(String path) {
        return mCallbacks.get(path);
    }

    public void removeCallback(String path) {
        mCallbacks.remove(path);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            Callback callback = mCallbacks.get(event.getDataItem().getUri().getPath());
            if (null != callback)
                callback.onDataChanged(event);
        }
    }
}

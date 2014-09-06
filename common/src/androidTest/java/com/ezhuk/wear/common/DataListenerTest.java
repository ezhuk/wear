// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import android.net.Uri;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataItem;

import junit.framework.TestCase;

import org.mockito.Mockito;


public class DataListenerTest extends TestCase {
    private static final String DATA_PATH = "/data";

    protected void setUp() {
        // empty
    }

    protected void tearDown() {
        // empty
    }

    public void testAddCallback() {
        DataListener listener = new DataListener();
        assertNull(listener.getCallback(DATA_PATH));

        listener.addCallback(DATA_PATH, Mockito.mock(DataListener.Callback.class));
        assertNotNull(listener.getCallback(DATA_PATH));
    }

    public void testRemoveCallback() {
        DataListener listener = new DataListener();
        assertNull(listener.getCallback(DATA_PATH));

        listener.addCallback(DATA_PATH, Mockito.mock(DataListener.Callback.class));
        assertNotNull(listener.getCallback(DATA_PATH));

        listener.removeCallback(DATA_PATH);
        assertNull(listener.getCallback(DATA_PATH));
    }

    public void testDataChanged() {
        DataItem item = Mockito.mock(DataItem.class);
        Mockito.when(item.getUri()).thenReturn(Uri.parse(DATA_PATH));

        DataEvent event = Mockito.mock(DataEvent.class);
        Mockito.when(event.getType()).thenReturn(DataEvent.TYPE_CHANGED);
        Mockito.when(event.getDataItem()).thenReturn(item);

        DataListener listener = new DataListener();
        DataListener.Callback callback = Mockito.mock(DataListener.Callback.class);
        listener.addCallback(DATA_PATH, callback);

        // TODO

        Mockito.verify(callback).onDataChanged(event);
        Mockito.verifyNoMoreInteractions(callback);
    }
}

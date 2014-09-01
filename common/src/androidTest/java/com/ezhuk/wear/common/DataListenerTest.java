// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.DataEvent;

import junit.framework.TestCase;


public class DataListenerTest extends TestCase {
    private static final String DATA_PATH = "/data";

    private static class MockCallback implements DataListener.Callback {
        @Override
        public void onDataChanged(DataEvent event) {
            // empty
        }
    }

    protected void setUp() {
        // empty
    }

    protected void tearDown() {
        // empty
    }

    public void testAddCallback() {
        DataListener listener = new DataListener();
        listener.addCallback(DATA_PATH, new MockCallback());
        assertNotNull(listener.getCallback(DATA_PATH));
    }

    public void testRemoveCallback() {
        DataListener listener = new DataListener();
        listener.addCallback(DATA_PATH, new MockCallback());
        listener.removeCallback(DATA_PATH);
        assertNull(listener.getCallback(DATA_PATH));
    }
}

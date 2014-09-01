// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.MessageEvent;

import junit.framework.TestCase;


public class MessageListenerTest extends TestCase {
    private static final String MESSAGE_PATH = "/foo";

    private static class CallbackResults {
        public int mCount;
        public String mPath;
        public byte[] mData;
    }

    private static class MockCallback implements MessageListener.Callback {
        private CallbackResults mResults;

        public MockCallback(CallbackResults results) {
            mResults = results;
        }

        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
            mResults.mCount += 1;
            mResults.mPath = messageEvent.getPath();
            mResults.mData = messageEvent.getData();
        }
    }

    private static class MockEvent implements MessageEvent {
        private int mRequestId;
        private String mNodeId;
        private String mPath;
        private byte[] mData;

        public MockEvent(int id, String node, String path, byte[] data) {
            mRequestId = id;
            mNodeId = node;
            mPath = path;
            mData = data;
        }

        @Override
        public int getRequestId() {
            return mRequestId;
        }

        @Override
        public String getSourceNodeId() {
            return mNodeId;
        }

        @Override
        public String getPath() {
            return mPath;
        }

        @Override
        public byte[] getData() {
            return mData;
        }
    }

    protected void setUp() {
        // empty
    }

    protected void tearDown() {
        // empty
    }

    public void testAddCallback() {
        MessageListener listener = new MessageListener();
        listener.addCallback(MESSAGE_PATH, new MockCallback(new CallbackResults()));
        assertNotNull(listener.getCallback(MESSAGE_PATH));
    }

    public void testRemoveCallback() {
        MessageListener listener = new MessageListener();
        listener.addCallback(MESSAGE_PATH, new MockCallback(new CallbackResults()));
        listener.removeCallback(MESSAGE_PATH);
        assertNull(listener.getCallback(MESSAGE_PATH));
    }

    public void testReceiveMessage() {
        MessageListener listener = new MessageListener();
        CallbackResults results = new CallbackResults();
        listener.addCallback(MESSAGE_PATH, new MockCallback(results));

        MockEvent event = new MockEvent(1, "NODE", MESSAGE_PATH, null);
        listener.onMessageReceived(event);
        assertEquals(1, results.mCount);
        assertEquals(MESSAGE_PATH, results.mPath);
        assertNull(null, results.mData);
    }
}

// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.MessageEvent;

import junit.framework.TestCase;

import org.mockito.Mockito;


public class MessageListenerTest extends TestCase {
    private static final String MESSAGE_PATH = "/foo";

    private static class CallbackResult {
        public int mCount;
        public MockEvent mEvent;
    }

    private static class MockCallback implements MessageListener.Callback {
        private CallbackResult mResult;

        public MockCallback(CallbackResult result) {
            mResult = result;
        }

        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
            mResult.mCount += 1;
            mResult.mEvent = new MockEvent(messageEvent);
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

        public MockEvent(MessageEvent event) {
            mRequestId = event.getRequestId();
            mNodeId = event.getSourceNodeId();
            mPath = event.getPath();
            mData = event.getData();
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
        assertNull(listener.getCallback(MESSAGE_PATH));

        listener.addCallback(MESSAGE_PATH, new MockCallback(new CallbackResult()));
        assertNotNull(listener.getCallback(MESSAGE_PATH));
    }

    public void testRemoveCallback() {
        MessageListener listener = new MessageListener();
        assertNull(listener.getCallback(MESSAGE_PATH));

        listener.addCallback(MESSAGE_PATH, new MockCallback(new CallbackResult()));
        assertNotNull(listener.getCallback(MESSAGE_PATH));

        listener.removeCallback(MESSAGE_PATH);
        assertNull(listener.getCallback(MESSAGE_PATH));
    }

    public void testReceiveMessage() {
        MessageListener listener = new MessageListener();

        MessageListener.Callback callback = Mockito.mock(MessageListener.Callback.class);
        listener.addCallback(MESSAGE_PATH, callback);

        MessageEvent event = Mockito.mock(MessageEvent.class);
        Mockito.when(event.getPath()).thenReturn(MESSAGE_PATH);
        listener.onMessageReceived(event);

        Mockito.verify(callback).onMessageReceived(event);
        Mockito.verifyNoMoreInteractions(callback);
    }
}

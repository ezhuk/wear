// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.MessageEvent;

import junit.framework.TestCase;


public class MessageListenerTest extends TestCase {
    private static final String MESSAGE_PATH = "/foo";

    private static class MockCallback implements MessageListener.Callback {
        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
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
        MessageListener listener = new MessageListener();
        listener.addCallback(MESSAGE_PATH, new MockCallback());
        assertNotNull(listener.getCallback(MESSAGE_PATH));
    }

    public void testRemoveCallback() {
        MessageListener listener = new MessageListener();
        listener.addCallback(MESSAGE_PATH, new MockCallback());
        listener.removeCallback(MESSAGE_PATH);
        assertNull(listener.getCallback(MESSAGE_PATH));
    }
}

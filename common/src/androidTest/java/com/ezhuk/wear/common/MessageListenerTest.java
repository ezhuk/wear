// Copyright (c) 2014 Eugene Zhuk.
// Use of this source code is governed by the MIT license that can be found
// in the LICENSE file.

package com.ezhuk.wear.common;

import com.google.android.gms.wearable.MessageEvent;

import junit.framework.TestCase;

import org.mockito.Mockito;

import java.util.ArrayList;


public class MessageListenerTest extends TestCase {
    private static final String MESSAGE_PATH = "/foo";

    protected void setUp() {
        // empty
    }

    protected void tearDown() {
        // empty
    }

    public void testAddCallback() {
        MessageListener listener = new MessageListener();
        assertNull(listener.getCallback(MESSAGE_PATH));

        listener.addCallback(MESSAGE_PATH, Mockito.mock(MessageListener.Callback.class));
        assertNotNull(listener.getCallback(MESSAGE_PATH));
    }

    public void testRemoveCallback() {
        MessageListener listener = new MessageListener();
        assertNull(listener.getCallback(MESSAGE_PATH));

        listener.addCallback(MESSAGE_PATH, Mockito.mock(MessageListener.Callback.class));
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

    public void testReceiveMultipleMessages() {
        MessageListener listener = new MessageListener();

        MessageListener.Callback callback = Mockito.mock(MessageListener.Callback.class);
        listener.addCallback(MESSAGE_PATH, callback);

        ArrayList<MessageEvent> events = new ArrayList<MessageEvent>();
        for (int i = 0; 2 > i; ++i) {
            MessageEvent event = Mockito.mock(MessageEvent.class);
            Mockito.when(event.getPath()).thenReturn(MESSAGE_PATH);
            events.add(event);
        }

        for (MessageEvent event : events) {
            listener.onMessageReceived(event);
        }

        for (MessageEvent event : events) {
            Mockito.verify(callback).onMessageReceived(event);
        }
        Mockito.verifyNoMoreInteractions(callback);
    }
}

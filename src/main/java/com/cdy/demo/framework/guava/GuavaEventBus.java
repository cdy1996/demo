package com.cdy.demo.framework.guava;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class GuavaEventBus {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus("test");
        EventListener listener = new EventListener();

        eventBus.register(listener);

        eventBus.post(new TestEvent(200));
        eventBus.post(new TestEvent(300));
        eventBus.post(new TestEvent(400));

        System.out.println("LastMessage:"+listener.getLastMessage());
    }
}
class TestEvent {
    private final int message;
    public TestEvent(int message) {
        this.message = message;
        System.out.println("event message:"+message);
    }
    public int getMessage() {
        return message;
    }
}
class EventListener {
    public int lastMessage = 0;

    @Subscribe
    public void listen(TestEvent event) {
        lastMessage = event.getMessage();
        System.out.println("Message:"+lastMessage);
    }

    public int getLastMessage() {
        return lastMessage;
    }
}
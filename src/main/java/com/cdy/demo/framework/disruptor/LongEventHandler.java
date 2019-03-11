package com.cdy.demo.framework.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public class LongEventHandler implements EventHandler<LongEvent>, WorkHandler<LongEvent> {
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event: " + event);
    }

    @Override
    public void onEvent(LongEvent event) throws Exception {
        System.out.println("Event: " + event);
    }
}
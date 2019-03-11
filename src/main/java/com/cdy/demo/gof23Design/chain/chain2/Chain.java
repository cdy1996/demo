package com.cdy.demo.gof23Design.chain.chain2;

import java.util.List;

/**
 * 封装调用关系
 * Created by 陈东一
 * 2017/11/4 19:01
 */
public class Chain {

    private List<ChainHandler> handlers;

    private int index = 0;

    public Chain(List<ChainHandler> handlers) {
        this.handlers = handlers;
    }

    public void proceed() {
        if (index >= handlers.size()) {
            return;
        }

        handlers.get(index++).execute(this);
    }
}

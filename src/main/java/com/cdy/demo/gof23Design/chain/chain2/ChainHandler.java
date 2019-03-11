package com.cdy.demo.gof23Design.chain.chain2;

/**
 * Created by 陈东一
 * 2017/11/4 19:02
 */
public abstract class ChainHandler {

    protected abstract void handleProcess();


    public void execute(Chain chain) {
        handleProcess();
        chain.proceed();
    }
}

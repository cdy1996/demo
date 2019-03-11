package com.cdy.demo.gof23Design.chain.chain3;

/**
 * aop如何链式调用
 * Created by 陈东一
 * 2017/11/4 18:53
 */
public abstract class Handler {

    private Handler sucessor;

    public Handler getSucessor() {
        return sucessor;
    }

    public void setSucessor(Handler sucesser) {
        this.sucessor = sucesser;
    }

    public void execute() {
        handleProcess();
        if (sucessor != null) {
            sucessor.execute();
        }
    }

    protected abstract void handleProcess();
}

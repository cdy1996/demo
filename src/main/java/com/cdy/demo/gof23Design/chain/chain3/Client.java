package com.cdy.demo.gof23Design.chain.chain3;

/**
 * Created by 陈东一
 * 2017/11/4 18:58
 */
public class Client {
    static class HandlerA extends Handler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by a");
        }
    }

    static class HandlerB extends Handler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by b");
        }
    }

    static class HandlerC extends Handler {

        @Override
        protected void handleProcess() {
            System.out.println("handle by c");
        }
    }

    public static void main(String[] args) {
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();
        handlerA.setSucessor(handlerB);
        handlerB.setSucessor(handlerC);
        handlerA.execute();
    }
}

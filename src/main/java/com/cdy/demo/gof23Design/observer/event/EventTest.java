package com.cdy.demo.gof23Design.observer.event;

/**
 * Created by viruser on 2018/6/29.
 */
public class EventTest {

    public static void main(String[] args) {
        Owner owner = new Owner();

        MyListerner myListerner = new MyListerner();

        owner.addListerner(myListerner);

        owner.change("!23");

    }
}

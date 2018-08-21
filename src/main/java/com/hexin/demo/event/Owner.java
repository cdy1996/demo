package com.hexin.demo.event;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by viruser on 2018/6/29.
 */
public class Owner {

    Set<MyListerner> listerners = new HashSet<>();

    public void addListerner(MyListerner myListerner) {
        listerners.add(myListerner);
    }

    public Owner() {
        this.listerners = new HashSet<>();
    }

    public void change(String a){
        System.out.println(a);
        listerners.forEach(e->e.change(new MyEvent(a)));
    }

}

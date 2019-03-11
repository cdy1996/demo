package com.cdy.demo.framework;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * shiro
 * Created by viruser on 2018/6/5.
 */
public class ShiroTest {

    public static void main(String[] args) {
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        simpleAccountRealm.addAccount("cdy", "123456", "admin");

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(simpleAccountRealm);
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("cdy", "123456");
        subject.login(token);
        System.out.println("是否登录:" + subject.isAuthenticated());
        subject.checkRoles("admin");    //授权
//        subject.checkPermission("user:delete");

        subject.logout();
        System.out.println("是否登录:" + subject.isAuthenticated());


        Function<String, String> f = a -> a;


    }


    @Test
    public void test() {
//        if( a > 1){
//            a.toString()
//        }

        Predicate<String> filter = a->a.length()>0;
        Function<String, Integer> map = a->Integer.parseInt(a);

        Consumer<String> sink = a-> {
            if(filter.test(a)){
                map.apply(a);
            }

        };


//        f1.apply(1);

//        Consumer<Integer> s = a->p.reactor1(a) ;
//        Consumer<String> s1 = a -> f.apply(a);


//        sink2.accept(1);

        ArrayList<String> integers = new ArrayList<>();
//        integers.spliterator().forEachRemaining(sink2);


        Supplier<List> supplier = ArrayList::new;
        BiConsumer<List, Integer> accumulator = List::add;
        List list = supplier.get();
        Consumer<Integer> sink3 = u->accumulator.accept(list, u);

        Consumer<String> sink2 = a -> {
            sink3.accept(map.apply(a));
        };
        Consumer<String> sink1 = a -> {
            if (filter.test(a)) {
                sink2.accept(a);
            }
        };
        integers.spliterator().forEachRemaining(sink1);
        System.out.println(list);


    }

    public void test(Predicate<String> p){

    }


}

package com.cdy.demo.framework;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;

import java.util.function.Function;

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

    


}

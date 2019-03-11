package com.cdy.demo.framework.spring;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@MyComponment
@Scope(value="session",proxyMode= ScopedProxyMode.TARGET_CLASS)
public class User {

    String username = Math.random()+"";
    Integer age;
    boolean sex;

    public boolean isSex() {
        return sex;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public static void main(String[] args) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        //操作单个属性
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();

        PropertyDescriptor pd = new PropertyDescriptor("sex", User.class);
        Method r = pd.getReadMethod();//获取属性的getter方法
        System.out.println(r.getName());
        r.invoke(user, null);
    }
}

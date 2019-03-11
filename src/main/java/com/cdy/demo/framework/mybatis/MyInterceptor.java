package com.cdy.demo.framework.mybatis;

import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * Created by 陈东一
 * 2018/1/8 14:24
 */
@Intercepts({@Signature(type = IService.class, method = "service", args = {String.class})})
public class MyInterceptor implements org.apache.ibatis.plugin.Interceptor {
    
    public MyInterceptor() {
    }
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("AshanInterceptor before...");
        Object obj = invocation.proceed();
        System.out.println("AshanInterceptor after...");
        return obj;
    }
    
    @Override
    public Object plugin(Object target) {
        
        System.out.println("plugin");
        if (target != null && target instanceof IService) {
            //Plugin是mybatis提供的
            return Plugin.wrap(target, this);
        }
        return target;
    }
    
    @Override
    public void setProperties(Properties properties) {
        System.out.println(properties);
    }
    
    public static void main(String[] args) {
        IService service = new ServiceImpl();
        MyInterceptor aiNoName = new MyInterceptor();
        //直接用Interceptor.plugin生成一个代理
        IService proxy1 = (IService) aiNoName.plugin(service);
        proxy1.service("ashan");

        System.out.println("######################");

        InterceptorChain chain = new InterceptorChain();
        chain.addInterceptor(new MyInterceptor());
        chain.addInterceptor(new MyInterceptor());
        chain.addInterceptor(new MyInterceptor());
        //用InterceptorChain生成一个代理
        IService proxy2 = (IService) chain.pluginAll(service);
        proxy2.service("designPattern/chain");
    }
}




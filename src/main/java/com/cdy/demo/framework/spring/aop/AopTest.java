package com.cdy.demo.framework.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

import java.lang.reflect.Method;

/**
 * Created by 陈东一
 * 2018/2/14 16:38
 */
interface iFoo {
    void fun1();
    
    void fun2();
}

class Foo implements iFoo {
    public void fun1() {
        System.out.println("外部方法");
//        fun2();
    }
    
    public void fun2() {
        System.out.println("内部方法");
    }
}

class Before implements MethodBeforeAdvice {
    
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("before");
    }
}

class Around implements MethodInterceptor {
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Around before");
        Object proceed = invocation.proceed();
        System.out.println("Around after");
        return proceed;
    }
}

class After implements AfterReturningAdvice {
    
    
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("afterReturning");
    }
}

interface iBoo {

}

class Boo implements iBoo, IntroductionInterceptor {
    
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (implementsInterface(methodInvocation.getMethod().getDeclaringClass())) {
            return methodInvocation.getMethod().invoke(this, methodInvocation.getArguments());
        } else {
            return methodInvocation.proceed();
        }
        
    }
    
    @Override
    public boolean implementsInterface(Class<?> intf) {
        boolean assignableFrom = intf.isAssignableFrom(iBoo.class);
        return assignableFrom;
    }
}


public class AopTest {
    
    
    public static void main(String[] args) {
        
        Foo foo = new Foo();
        BeforeAdvice advice = new Before();
        
        ProxyFactory pf = new ProxyFactory();
        pf.setOptimize(true);//启用Cglib2AopProxy创建代理
        pf.setProxyTargetClass(true);
        pf.setTarget(foo);
        pf.addAdvice(advice);
        pf.addAdvice(new After());
        pf.addAdvice(new Around());
        
        /* 使用advisor*/
//        NameMatchMethodPointcutAdvisor pointcutAdvisor = new NameMatchMethodPointcutAdvisor();
//        pointcutAdvisor.setMappedName("fun1");
//        pointcutAdvisor.setAdvice(advice);
//        pf.addAdvisor(pointcutAdvisor);
        
        
        Foo proxy = (Foo) pf.getProxy();
        proxy.fun1();
//        proxy.fun2();
        
        /* pointcut的使用*/
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedName("fun1");
//        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
//        defaultPointcutAdvisor.setPointcut(pointcut);
//        defaultPointcutAdvisor.setAdvice(advice);
    
    
    }
    
    @Test
    public void test() {
        Foo foo = new Foo();
        ProxyFactory pf = new ProxyFactory();
        pf.setProxyTargetClass(true);
//        pf.setOptimize(true);//启用Cglib2AopProxy创建代理
        DelegatingIntroductionInterceptor interceptor = new DelegatingIntroductionInterceptor(foo);
//        DefaultIntroductionAdvisor advisor = new DefaultIntroductionAdvisor(new Boo(), iBoo.class);
//        DefaultIntroductionAdvisor advisor = new DefaultIntroductionAdvisor(interceptor, interceptor);
        pf.addAdvice(interceptor);
//        pf.addAdvisor(advisor);
        pf.setTarget(new Boo());
        Object proxy = pf.getProxy();
//        ((iBoo)proxy).getClass();
        ((iFoo) proxy).fun1();
    }
    
    
    @Test
    public void test2() throws NoSuchMethodException {
        iFoo foo = new Foo();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* fun1())");
        
//        Class<BeforeMethod> beforeMethodClass = BeforeMethod.class;
//        Method before = beforeMethodClass.getMethod("before", null);
//
//        SingletonAspectInstanceFactory singletonAspectInstanceFactory = new SingletonAspectInstanceFactory(foo);
//
//        AspectJMethodBeforeAdvice beforeAdvice = new AspectJMethodBeforeAdvice(before, pointcut, singletonAspectInstanceFactory);
//
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(foo);
        proxyFactory.addAspect(AspectClass.class);
//        proxyFactory.addAdvice(beforeAdvice);
        
        Object proxy = proxyFactory.getProxy();
        ((iFoo) proxy).fun1();
    }
}


package com.cdy.demo.repeatedWheels.myproxy;

import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用javasisst生成class
 *
 * Created by 陈东一
 * 2019/8/16 0016 21:44
 */
public class JavasisstProxy {
    private static String ln = "\r\n";
    private static AtomicInteger integer = new AtomicInteger(0);
    
    private static ClassPool mPool = ClassPool.getDefault();
    
    public static void main(String[] args) {
        IService service = new MyService();
        IService instance = (IService) JavasisstProxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IService.class},
                (proxy, method, args1) -> {
                    System.out.println("before");
                    return method.invoke(service, args1);
                });
    
        instance.doService("hello");
    }
    
    public static Object newProxyInstance(ClassLoader classLoader,Class<?>[] interfaces, InvocationHandler h) {
    
        try {
            int num = integer.incrementAndGet();
            String className = "$Proxy" + num ;
        
            CtClass ctClass = mPool.makeClass(className);
            ctClass.addInterface(mPool.get(interfaces[0].getName()));
            ctClass.addField(CtField.make("com.cdy.demo.repeatedWheels.myproxy.InvocationHandler h;", ctClass));
        
            String constructor = "public " + className + "(" + InvocationHandler.class.getName() + " h) {" + ln +
                    "this.h = h;" + ln +
                    "}" + ln;
            ctClass.addConstructor(CtNewConstructor.make(constructor,ctClass));
        
            for (Method m : interfaces[0].getMethods()) {
                StringBuilder method = new StringBuilder();
                StringBuilder params = new StringBuilder();
                StringBuilder params2 = new StringBuilder();
                StringBuilder classes = new StringBuilder();
                for (Parameter parameter : m.getParameters()) {
                    params.append(parameter.getType().getName()).append(" ").append(parameter.getName()).append(",");
                    classes.append(parameter.getType().getName()).append(".class").append(",");
                    params2.append(parameter.getName()).append(",");
                }
                params.deleteCharAt(params.lastIndexOf(","));
                params2.deleteCharAt(params2.lastIndexOf(","));
                classes.deleteCharAt(classes.lastIndexOf(","));
                String returnType = m.getReturnType().getName();
                method.append("public ").append(returnType).append(" ")
                        .append(m.getName()).append("(").append(params.toString()).append("){").append(ln);
        
                method.append("try{").append(ln);
                method.append("java.lang.reflect.Method m = ").append(interfaces[0].getName()).append(".class.getMethod(\"")
                        .append(m.getName()).append("\",new java.lang.Class[]{").append(classes).append("});").append(ln);
                if (!returnType.contains("void")) {
                    method.append("return ").append("(").append(returnType).append(")");
                }
                method.append("this.h.invoke(this,m,").append("new java.lang.Object[]{").append(params2).append("}").append(");").append(ln);
                method.append("}catch(java.lang.Throwable e){throw new java.lang.RuntimeException(e);}").append(ln);
                method.append("}").append(ln);
                ctClass.addMethod(CtMethod.make(method.toString(), ctClass));
            }
            Class clazz = ctClass.toClass(classLoader, JavasisstProxy.class.getProtectionDomain());
            Constructor c = clazz.getConstructor(InvocationHandler.class);
        
            return c.newInstance(h);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    
    }
    
}

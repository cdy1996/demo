package com.cdy.demo.java.jvm;

import java.lang.invoke.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.invoke.MethodHandles.lookup;

public class InvokeDynamicTest {


    public static void main(String[] args) throws Throwable {
//        INDY_BootstrapMethod().invokeExact("icyfenix");

        GrandFather grandFather = new Son();
        grandFather.thinking();
        Son son = new Son();
        son.thinking();

    }

    public static void testMethod(String s) {
        System.out.println("hello String：" + s);
    }

    public static CallSite BootstrapMethod(
            MethodHandles.Lookup lookup, String name, MethodType mt) throws Throwable {
        return new ConstantCallSite(lookup.findStatic(InvokeDynamicTest.class, name, mt));
    }

    private static MethodType MT_BootstrapMethod() {
        return MethodType.fromMethodDescriptorString(
                "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
                null);
    }

    private static MethodHandle MH_BootstrapMethod() throws Throwable {
        return lookup().findStatic(InvokeDynamicTest.class, "BootstrapMethod", MT_BootstrapMethod());
    }

    private static MethodHandle INDY_BootstrapMethod() throws Throwable {
        CallSite cs = (CallSite) MH_BootstrapMethod().invokeWithArguments(lookup(), "testMethod",
                MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V", null));
        return cs.dynamicInvoker();
    }


    static class GrandFather {
        void thinking() {
            System.out.println("i am grandfather");
        }
    }

    static class Father extends GrandFather {
        void thinking() {
            System.out.println("i am father");
        }
    }

    static class Son extends Father {
        void thinking() {
            //请读者在这里填入适当的代码 (不能修改其他地方的代码)
            //实现调用祖父类的thinking () 方法, 打印"i am grandfather"
            Class<? extends Son> aClass = this.getClass();
            Class clazz = aClass;
            while ((clazz.getSuperclass()) != null) {
                Class superclass = clazz.getSuperclass();
                if (superclass == Object.class) {
                    break;
                }
                clazz = superclass;
            }

            try {
                Method thinking = clazz.getDeclaredMethod("thinking");
                Object o = clazz.getDeclaredConstructor().newInstance();

                thinking.invoke(o);
//                thinking.invoke(this);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            try {
                MethodType mt = MethodType.methodType(void.class);
                MethodHandle mh = lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
                mh.invoke(this);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}


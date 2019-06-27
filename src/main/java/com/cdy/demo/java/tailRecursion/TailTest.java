package com.cdy.demo.java.tailRecursion;

import java.util.function.Function;

/**
 * https://www.cnblogs.com/invoker-/p/7723420.html
 */
public class TailTest {

    /**
     * 阶乘计算 -- 递归解决
     *
     * @param number 当前阶乘需要计算的数值
     * @return number!
     */
    public static int factorialRecursion(final int number) {
        if (number == 1) return number;
        else return number * factorialRecursion(number - 1);
    }

    /**
     * 阶乘计算 -- 尾递归解决
     *
     * 很显然，如果事情这么简单的话，这篇文章也就结束了，和lambda也没啥关系 :) 然而当你调用上文的尾递归写法之后，
     * 发现并没有什么作用，该栈溢出的还是会栈溢出，其实原因我在开头就已经说了，尾递归这样的写法本身并不会有什么用，
     * 依赖的是编译器对尾递归写法的优化，在很多语言中编译器都对尾递归有优化，然而这些语言中并不包括java，
     * 因此在这里我们使用lambda的懒加载(惰性求值)机制来延迟递归的调用，从而实现栈帧的复用。
     *
     * @param factorial 上一轮递归保存的值
     * @param number    当前阶乘需要计算的数值
     * @return number!
     */
    public static int factorialTailRecursion1(final int factorial, final int number) {
        if (number == 1) return factorial;
        else return factorialTailRecursion1(factorial * number, number - 1);
    }

    /**
     * 阶乘计算 -- 使用尾递归接口完成
     * @param factorial 当前递归栈的结果值
     * @param number 下一个递归需要计算的值
     * @return 尾递归接口,调用invoke启动及早求值获得结果
     */
    public static TailRecursion<Integer> factorialTailRecursion(final int factorial, final int number) {
        if (number == 1)
            return TailInvoke.done(factorial);
        else
            return TailInvoke.call(() -> factorialTailRecursion(factorial + number, number - 1));
    }


    public static void main(String[] args) {
        System.out.println(factorialRecursion(100_000));
        System.out.println(factorial(100_000));


        Function<String, String> function = a->a+"";
        F f = a->a+"";
        f= function::apply;
        function = f::apply;
     }

    public static long factorial(final int number) {
        return factorialTailRecursion(1, number).invoke();
    }

    static interface F{
        String apply(String t);
    }

}

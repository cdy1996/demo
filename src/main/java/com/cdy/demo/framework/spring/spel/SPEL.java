package com.cdy.demo.framework.spring.spel;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viruser on 2018/8/1.
 */
public class SPEL {

    @Test
    public void test1() {
        // 1. 构建解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 2. 解析表达式

        String value = parser.parseExpression("'hello'").getValue(String.class);// hello , 注意单引号
        Long value1 = parser.parseExpression("1.024E+3").getValue(Long.class);// 1024  , 指数形式
        Integer value2 = parser.parseExpression("0xFFFF").getValue(Integer.class);// 65535 , 十六进制
        Boolean aTrue = parser.parseExpression("true").getValue(Boolean.class);// true
        Object aNull = parser.parseExpression("null").getValue();
        System.out.println(value);
        System.out.println(value1);
        System.out.println(value2);
        System.out.println(aTrue);
        System.out.println(aNull);

    }

    @Test
    public void test2() {
        // 定义变量
        String name = "Tom";
        EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
        context.setVariable("myName", name);                        // 为了让表达式可以访问该对象, 先把对象放到上下文中
        ExpressionParser parser = new SpelExpressionParser();
        // 访问变量
        String value = parser.parseExpression("#myName+'_'+#myName").getValue(context, String.class);// Tom , 使用变量
//        String value1 = parser.parseExpression("#{myName}").getValue(context, String.class);// Tom , 使用变量
        // 直接使用构造方法创建对象
        String value1 = parser.parseExpression("new String('aaa')").getValue(String.class);// aaa

        System.out.println(value);
        System.out.println(value1);

    }

    @Test
    public void test3() {
        // 准备工作
        Person person = new Person("Tom", 18); // 一个普通的POJO
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        Map<String, String> map = new HashMap<>();
        map.put("A", "1");
        map.put("B", "2");
        EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
        context.setVariable("person", person);                        // 为了让表达式可以访问该对象, 先把对象放到上下文中
        context.setVariable("map", map);
        context.setVariable("list", list);
        ExpressionParser parser = new SpelExpressionParser();
        // 属性
        String value = parser.parseExpression("#person.name").getValue(context, String.class);// Tom , 属性访问
        String value1 = parser.parseExpression("#person.Name").getValue(context, String.class);// Tom , 属性访问, 但是首字母大写了
// 列表
        String value2 = parser.parseExpression("#list[0]").getValue(context, String.class);// a , 下标
// map
        String value3 = parser.parseExpression("#map[A]").getValue(context, String.class);// 1 , key
// 方法
        Integer value4 = parser.parseExpression("#person.getAge()").getValue(context, Integer.class);// 18 , 方法访问

        System.out.println(value);
        System.out.println(value1);
        System.out.println(value2);
        System.out.println(value3);
        System.out.println(value4);

    }


    @Test
    public void test4(){
        EvaluationContext context = new StandardEvaluationContext();  // 表达式的上下文,
        ExpressionParser parser = new SpelExpressionParser();
        // 获取类型
        Class value = parser.parseExpression("T(java.util.Date)").getValue(Class.class);// class java.util.Date
// 访问静态成员(方法或属性)
        Integer value1 = parser.parseExpression("T(Math).abs(-1)").getValue(Integer.class);// 1
// 判断类型
        Boolean value2 = parser.parseExpression("'asdf' instanceof T(String)").getValue(Boolean.class);// true;
        String value3 = parser.parseExpression("T(Person).getTest()").getValue(String.class);// true;
        System.out.println(value);
        System.out.println(value1);
        System.out.println(value2);
        System.out.println(value3);


        String value4 = parser.parseExpression("#name?.toUpperCase()").getValue(context, String.class);// null
        System.out.println(value4);

        List<String> value5 = parser.parseExpression("{1, 3, 5, 7}.?[#this > 3]").getValue(List.class);// [5, 7]
        System.out.println(value5);

        Person person = new Person("Tom", 18); // 一个普通的POJO
        context.setVariable("person", person);
        String value6 = parser.parseExpression("他的名字为${#person.name}", new TemplateParserContext("${","}")).getValue(context, String.class);// 他的名字为Tom
        System.out.println(value6);





    }
}

class Person {
    String name;
    Integer age;

    public static String getTest(){
        return "123";
    }

    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

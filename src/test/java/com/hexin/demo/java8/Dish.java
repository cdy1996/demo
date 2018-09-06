package com.hexin.demo.java8;

/**
 * Created by viruser on 2018/7/9.
 */
public class Dish {

    private final String name;
    private final boolean vegetarian; //素食者
    private final int calories; //卡路里
    private final Type type;
    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public boolean isVegetarian() {
        return vegetarian;
    }
    public int getCalories() {
        return calories;
    }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        return name;
    }
    public enum Type {肉, 鱼, 其他}
}


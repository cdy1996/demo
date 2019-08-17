package com.cdy.demo.repeatedWheels.myproxy;

/**
 * todo
 * Created by 陈东一
 * 2019/8/17 0017 14:30
 */
public class MyService implements IService {
    @Override
    public String doService(String s) {
        return s+"!";
    }
}

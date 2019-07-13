package com.cdy.demo.repeatedWheels.myaop.Test;

/**
 * todo
 * Created by 陈东一
 * 2019/7/13 0013 14:47
 */
public class SleepImpl  implements Sleep{
    
    @Override
    public int sleep(int time) {
        System.out.println(time);
        return time;
    }
}

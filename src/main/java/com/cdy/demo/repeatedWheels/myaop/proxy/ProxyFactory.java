package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;

/**
 * 植入器
 * Created by 陈东一
 * 2019/7/13 0013 13:25
 */
public interface ProxyFactory {
    <T> T generateProxy(T target, Advisor advisor);
}

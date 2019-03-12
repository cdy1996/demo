package com.cdy.demo.framework.spring.spel;

import com.cdy.demo.framework.spring.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by viruser on 2018/8/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SpringTest {

    @Value("${title}")
    public String title;

    @Value("#{new String('aaa')}")
    public String aaa;

    @Value("#{config.title}")
    public String title2;


    @Test
    public void test() {
        System.out.println(title);
        System.out.println(aaa);
        System.out.println(title2);
    }
}

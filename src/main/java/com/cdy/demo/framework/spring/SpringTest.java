package com.cdy.demo.framework.spring;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
//@ContextConfiguration(classes = TestConfig.class) //加载配置文件
public class SpringTest {

    @Test
    public void testLookUp(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        LookupMethodServiceImpl lookupMethodServiceImpl = (LookupMethodServiceImpl) applicationContext.getBean("lookupMethodServiceImpl");
        System.out.println(lookupMethodServiceImpl.getUser());
        System.out.println(lookupMethodServiceImpl.getUser());
        System.out.println(lookupMethodServiceImpl.getUser());
    }
}

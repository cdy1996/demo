package com.hexin.demo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
//@ContextConfiguration(classes = TestConfig.class) //加载配置文件
public class Test {


    User user44;
    @Autowired
    public void setUser44(User user44) {
        this.user44 = user44;
    }

    @org.junit.Test
    public void test(){
        System.out.println(user44.getUsername());
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
//        annotationConfigApplicationContext.scan("com.hexin.demo.spring");
//        annotationConfigApplicationContext.refresh();


        Controller controller = (Controller) applicationContext.getBean("controller");
        System.out.println(controller+""+controller.user);

        Controller controller1 = (Controller) applicationContext.getBean("controller");
        System.out.println(controller1+""+controller1.user);

//        LookupMethodServiceImpl lookupMethodServiceImpl = (LookupMethodServiceImpl) applicationContext.getBean("lookupMethodServiceImpl");
//        System.out.println(lookupMethodServiceImpl.getUser());
//        System.out.println(lookupMethodServiceImpl.getUser());
//        System.out.println(lookupMethodServiceImpl.getUser());
//
//        TransactionAspectSupport.currentTransactionStatus();
//        TransactionSynchronizationManager.getCurrentTransactionName();

    }
}

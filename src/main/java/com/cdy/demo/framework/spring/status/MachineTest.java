package com.cdy.demo.framework.spring.status;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.statemachine.StateMachine;

public class MachineTest {

    public static void main(String... args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(StateMachineConfig.class, EventConfig.class);
    
        StateMachine<States, Events> stateMachine = applicationContext.getBean(StateMachine.class);
        new Thread(stateMachine::start).start();
        
        new Thread(()->{
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stateMachine.sendEvent(Events.PAY);
        }).start();
        
        new Thread(()->{
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stateMachine.sendEvent(Events.RECEIVE);
        }).start();
    }




}



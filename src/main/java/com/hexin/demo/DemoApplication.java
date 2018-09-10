package com.hexin.demo;

import com.hexin.demo.status.Events;
import com.hexin.demo.status.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
//@RestController
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{
            stateMachine.start();
        }).start();
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


    //	@RequestMapping("/test")
//	@ResponseBody
//	public String helloworld(@ModelAttribute Person person){
//		return person.getName() + person.getAge();
//	}


}
//
//class Person {
//    String name;
//    Integer age;
//
//    public static String getTest(){
//        return "123";
//    }
//
//    public Person() {
//    }
//
//    public Person(String name, Integer age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//}
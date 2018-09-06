package com.hexin.demo;

import com.hexin.demo.status.Events;
import com.hexin.demo.status.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SpringBootApplication
@RestController
public class DemoApplication implements CommandLineRunner {

	@RequestMapping("/test")
	@ResponseBody
	public String helloworld(@ModelAttribute Person person){
		return person.getName() + person.getAge();
	}


    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        stateMachine.start();
        stateMachine.sendEvent(Events.PAY);
        stateMachine.sendEvent(Events.RECEIVE);
    }

	public static void main(String[] args) {


		SpringApplication.run(DemoApplication.class, args);
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
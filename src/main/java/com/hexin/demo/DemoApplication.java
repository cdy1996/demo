package com.hexin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SpringBootApplication
@RestController
public class DemoApplication {

	@RequestMapping("/test")
	@ResponseBody
	public String helloworld(@ModelAttribute Person person){
		return person.getName() + person.getAge();
	}


	public static void main(String[] args) {
//		Function<String, Predicate<String>> function = x->y->y.startsWith(x);
//		System.out.println(function.apply("12345").test("123"));
//
//        String[] title = {"!23", "!23"};
//        export(title, ()->{
//            Map<String, String> map = new HashMap<>();
//            map.put("1", "111");
//            map.put("2", "222");
//            List<Map<String, String>> list = new ArrayList<>();
//            list.add(map);
//            list.add(map);
//            list.add(map);
//            return list;
//        });

//
//        TreeMap<Integer, String> tree = new TreeMap<>();
//        tree.put(5, "5");
//        tree.put(4, "4");
//        tree.put(6, "6");
//        tree.put(3, "3");


		SpringApplication.run(DemoApplication.class, args);
	}

	public static void export(String[] title, Supplier<List<Map<String, String>>> supplier){
		System.out.println(title);
        List<Map<String, String>> maps = supplier.get();
        System.out.println(maps);
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
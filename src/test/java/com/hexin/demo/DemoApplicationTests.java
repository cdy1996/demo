package com.hexin.demo;

import org.junit.Test;

import java.util.Calendar;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {

		Calendar instance = Calendar.getInstance();

		System.out.println(instance.get(Calendar.HOUR_OF_DAY));
	}

}

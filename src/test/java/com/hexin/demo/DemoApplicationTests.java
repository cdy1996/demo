package com.hexin.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {

		System.out.println("-----------------------------------");
		List<String> uids = new ArrayList<>();

		for (String s : uids) {

			System.out.println(s);
		}
	}

}

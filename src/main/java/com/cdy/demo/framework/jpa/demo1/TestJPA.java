package com.cdy.demo.framework.jpa.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * 程序入口
 * @author HP
 *
 */
public class TestJPA {
	public static void main(String[] args) {
		ApplicationContext ac = new AnnotationConfigApplicationContext(JpaConfig.class);
		TestRepostry1 repostry1 = ac.getBean(TestRepostry1.class);


		/*
		 * 这个不知道什么情况, 共享的EntityManager 只建议使用查询
		 */
		EntityManager entityManager1 = ac.getBean(EntityManager.class);

		/*
		 * 和spring-data-jpa整合之后 EntityManagerFactory进行了代理,通过它获得的 EntityManager都是会自动放到threadlocal里面,
		 * 并且它的代理方法会拒绝执行getTransaction 方法 而是提示你使用@Transaction
		 *
		 *     EntityTransaction transaction = entityManager.getTransaction();
         *      entityManager.persist(s);
		 */
		EntityManagerFactory bean = ac.getBean(EntityManagerFactory.class);
		Test s = new Test();
		s.setId(2);
		s.setName("cdy");

		TestRepository rr = ac.getBean(TestRepository.class);
		rr.addTest(s);
		Test record = rr.getTestByQuality(1);
		System.out.println(record);




	}
}

package com.cdy.demo.framework.jpa.demo1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * 程序入口
 * @author HP
 *
 */
public class TestJPA {
	public static void main(String[] args) {
		ApplicationContext ac = new AnnotationConfigApplicationContext(JpaConfig.class);
		TestRepostry1 repostry1 = ac.getBean(TestRepostry1.class);

		EntityManager entityManager1 = ac.getBean(EntityManager.class);

		EntityManagerFactory bean = ac.getBean(EntityManagerFactory.class);
		Test s = new Test();
		s.setId(2);
		s.setName("cdy");
		EntityManager entityManager = bean.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		entityManager.persist(s);


		TestRepository rr = ac.getBean(TestRepository.class);
		rr.addTest(s);
		Test record = rr.getTestByQuality(1);
		System.out.println(record);




	}
}

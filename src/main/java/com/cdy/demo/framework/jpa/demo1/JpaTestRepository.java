package com.cdy.demo.framework.jpa.demo1;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Repository
@Transactional
public class JpaTestRepository implements TestRepository {
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	public void addTest(Test record){
		emf.createEntityManager().persist(record);
	}
	
	public Test getTestByQuality(Integer quality){
		return emf.createEntityManager().find(Test.class, quality);
	}
	
	public void saveTest(Test record){
		emf.createEntityManager().merge(record);
	}
}

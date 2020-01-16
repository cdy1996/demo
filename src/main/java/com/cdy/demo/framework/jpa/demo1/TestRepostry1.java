package com.cdy.demo.framework.jpa.demo1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepostry1 extends JpaRepository<Test, Integer> {

}

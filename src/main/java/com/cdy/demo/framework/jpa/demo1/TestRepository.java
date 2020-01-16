package com.cdy.demo.framework.jpa.demo1;

public interface TestRepository {
	public void addTest(Test record);
	public Test getTestByQuality(Integer quality);
	public void saveTest(Test record);
}

package com.cdy.demo.framework.jpa.demo1;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 配置类
 * hibernate https://blog.csdn.net/weixin_37624828/article/details/85234237
 * jpa-hibernate https://www.cnblogs.com/lhuser/p/8996834.html
 * @author HP
 *
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories( //0
		basePackages = "com.cdy.demo.framework.jpa.demo1",
		entityManagerFactoryRef = "entityManageFactory",
		transactionManagerRef = "transactionManager"
)
public class JpaConfig {
	//    static String url = "jdbc:h2:tcp://localhost:9092/mem:h2db";
//    static String url = "jdbc:h2:tcp://localhost:9092//D:/H2/h2db";
    static String url = "jdbc:h2:mem:h2db";
//	static String url = "jdbc:h2:file:D:/H2/h2db";

	@Bean
	public DataSource dataSource(){

		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setDriverClassName("org.h2.Driver");
		hikariDataSource.setJdbcUrl(url);
		hikariDataSource.setUsername("root");
		hikariDataSource.setPassword("");
		hikariDataSource.setMinimumIdle(5);
		hikariDataSource.setMaximumPoolSize(10);

		try(Connection sa = hikariDataSource.getConnection()) {
			sa.createStatement().execute("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255));");
			sa.createStatement().execute("INSERT INTO TEST VALUES(1, 'Hello');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hikariDataSource;
	}
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManageFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter){
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setPackagesToScan("com.cdy.demo.framework.jpa.demo1");
		return emfb;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManageFactory) { //4
		return new JpaTransactionManager(entityManageFactory);
	}


	@Bean
	public JpaVendorAdapter jpaVendorAdapter(){
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.HSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
		return adapter;
	}
}

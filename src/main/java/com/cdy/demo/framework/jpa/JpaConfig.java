package com.cdy.demo.framework.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;


/**
 * 启动类上关闭自动配置 @SpringBootApplication(exclude = {
 *                                        DataSourceAutoConfiguration.class,
 *                                        HibernateJpaAutoConfiguration.class,
 *                                        DataSourceTransactionManagerAutoConfiguration.class})
 * 手动开启事务 @EnableTransactionManagement
 */
@Configuration
@EnableTransactionManagement
public class JpaConfig {

    //https://www.cnblogs.com/qqvvn/p/9503385.html
    //https://www.jianshu.com/p/c185188fa7c8%20
    public void testH2() throws Exception {
        Class.forName("org.h2.Driver");
        Connection sa = DriverManager.getConnection("jdbc:h2:mem:h2db", "sa", "");
        sa.createStatement().execute("CREATE TABLE TEST(ID INT PRIMARY KEY,\n" +
                "   NAME VARCHAR(255));");
        sa.createStatement().execute("INSERT INTO TEST VALUES(1, 'Hello');");
        ResultSet resultSet = sa.createStatement().executeQuery("select * from Test");
        while (resultSet.next()) {
            System.out.print(resultSet.getInt(1)+"-");
            System.out.println(resultSet.getString(2));
        }
    }

    //https://www.cnblogs.com/cxyzyh2017/p/6673089.html
    public static void main(String[] args) {


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(JpaConfig.class);
        EntityManager bean1 = applicationContext.getBean(EntityManager.class);
        ManRepository bean = applicationContext.getBean(ManRepository.class);
        Man s = new Man();
        s.setEmail("123@123.com");
        s.setName("cdy");
        bean.save(s);

        EntityTransaction transaction = bean1.getTransaction();


    }

    /**
     * 注解@EnableJpaRepositories的作用是开启Jpa的支持,因为我们会有多个自定义JPA，就需要单独实现各自的管理类，
     * 其中，entityManagerFactoryRef是实体关联管理工厂类和transactionManagerRef事务管理类，都需要我们自行实现。
     */
    @Configuration
    @EnableJpaRepositories( //0
            basePackages = "com.cdy.demo.framework.jpa",
            entityManagerFactoryRef = "manEntityManagerFactory",
            transactionManagerRef = "manTransactionManager"
    )
    static class MansDBConfig {
        @Autowired
        private Environment env; //1

        @Bean
        @ConfigurationProperties(prefix = "datasource.man")
        public DataSourceProperties manDataSourceProperties() { //2

            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            dataSourceProperties.setUsername("root");
            dataSourceProperties.setDriverClassName("org.h2.Driver");
            dataSourceProperties.setUrl("jdbc:h2:tcp://localhost:9092/mem:h2db");
            return dataSourceProperties;
        }


        /**
         * 获取前缀的DataSourceProperties对象，并创建真正的DataSource数据源,
         * 这里我们使用的是Spring Boot自带的工具类DataSourceBuilder，值来源的就是从前缀对象中读取的值，
         * 换句话说，就是在配置文件里我们写的值；
         * @return
         */
        @Bean
        public DataSource manDataSource() { //3
            DataSourceProperties manDataSourceProperties = manDataSourceProperties();
            return DataSourceBuilder.create()
                    .driverClassName(manDataSourceProperties.getDriverClassName())
                    .url(manDataSourceProperties.getUrl())
                    .username(manDataSourceProperties.getUsername())
                    .password(manDataSourceProperties.getPassword())
                    .build();
        }

        /**
         * 事物管理器的主接口PlatformTransactionManager需要获取到JpaTransactionManager的对象进行事务管理，
         * 这个对象就是由下面//5的工厂方法创建的。
         * @return
         */
        @Bean
        public PlatformTransactionManager manTransactionManager() { //4
            EntityManagerFactory factory = manEntityManagerFactory().getObject();
            return new JpaTransactionManager(factory);
        }

        /**
         * 这是JPA的开发规范，的确很麻烦，这是为了简单而做出的牺牲，简单说，这里会将我们的数据源，适配器，配置策略都全部封装好返回，
         * 让//4调用来创建返回给事务管理器。而同时，Boot会通过加载配置属性来获取配置值。
         * @return
         */
        @Bean
        public LocalContainerEntityManagerFactoryBean manEntityManagerFactory() {//5
            LocalContainerEntityManagerFactoryBean factory =
                    new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(manDataSource());
            factory.setPackagesToScan("com.cdy.demo.framework.jpa");
            factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            Properties jpaProperties = new Properties();
            jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "update"));
            jpaProperties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql","true"));
            jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            factory.setJpaProperties(jpaProperties);
            return factory;
        }

        /**
         * 是否进行DataSource初始化，如果设置，就会调用这里的方法执行对应的sql文件。
         * @return
         */
//        @Bean
        public DataSourceInitializer manDataSourceInitializer() {//6
            DataSourceInitializer dsInitializer = new DataSourceInitializer();
            dsInitializer.setDataSource(manDataSource());
            ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator();
            dbPopulator.addScript(new ClassPathResource("security-data.sql"));
            dsInitializer.setDatabasePopulator(dbPopulator);
            dsInitializer.setEnabled(env.getProperty("datasource.man.initialize",
                    Boolean.class, false));
            return dsInitializer;
        }
    }

//  @Configuration
//  @EnableJpaRepositories( //0
//        basePackages = "com.cdy.demo.framework.axon",
//        entityManagerFactoryRef = "ordersEntityManagerFactory",
//        transactionManagerRef = "ordersTransactionManager"
//)
//    static class OrdersDBConfig {
//
//        @Autowired
//        private Environment env;
//
//        @Bean
//        @ConfigurationProperties(prefix = "datasource.orders")
//        public DataSourceProperties ordersDataSourceProperties() {
//            return new DataSourceProperties();
//        }
//
//        @Bean
//        public DataSource ordersDataSource() {
//            DataSourceProperties primaryDataSourceProperties = ordersDataSourceProperties();
//            return DataSourceBuilder.create()
//                    .driverClassName(primaryDataSourceProperties.getDriverClassName())
//                    .url(primaryDataSourceProperties.getUrl())
//                    .username(primaryDataSourceProperties.getUsername())
//                    .password(primaryDataSourceProperties.getPassword())
//                    .build();
//        }
//
//        @Bean
//        public PlatformTransactionManager ordersTransactionManager() {
//            EntityManagerFactory factory = ordersEntityManagerFactory().getObject();
//            return new JpaTransactionManager(factory);
//        }
//
//        @Bean
//        public LocalContainerEntityManagerFactoryBean ordersEntityManagerFactory() {
//            LocalContainerEntityManagerFactoryBean factory = new
//                    LocalContainerEntityManagerFactoryBean();
//            factory.setDataSource(ordersDataSource());
//            factory.setPackagesToScan("com.cdy.demo.framework.jpa");
//            factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//            Properties jpaProperties = new Properties();
//            jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "true"));
//            jpaProperties.put("hibernate.show-sql", env.getProperty("hibernate.show-sql","true"));
//            factory.setJpaProperties(jpaProperties);
//            return factory;
//        }
//
//        @Bean
//        public DataSourceInitializer ordersDataSourceInitializer() {
//            DataSourceInitializer dsInitializer = new DataSourceInitializer();
//            dsInitializer.setDataSource(ordersDataSource());
//            ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator();
//            dbPopulator.addScript(new ClassPathResource("orders-data.sql"));
//            dsInitializer.setDatabasePopulator(dbPopulator);
//            dsInitializer.setEnabled(env.getProperty("datasource.orders.initialize",
//                    Boolean.class, false));
//            return dsInitializer;
//        }
//    }

}

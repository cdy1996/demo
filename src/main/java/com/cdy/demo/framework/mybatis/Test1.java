package com.cdy.demo.framework.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


/**
 * Created by 陈东一
 * 2017/12/30 16:40
 */
@Slf4j
public class Test1 {
    
    @Test
    public void test() throws IOException {
        // 1. 加载MyBatis的配置文件：mybatis.xml（它也加载关联的映射文件，也就是mappers结点下的映射文件）
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream("mybatis.xml");
//        Console console = System.console();
//        Scanner scanner = new Scanner(System.in);
//        String name = scanner.next();
//        int age = scanner.nextInt();
        
        this.getClass().getResourceAsStream("mybatis.xml");
        Reader reader = Resources.getResourceAsReader("mybatis.xml");
        // 2. SqlSessionFactoryBuidler实例将通过输入流调用build方法来构建 SqlSession 工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 3. 通过工厂获取 SqlSession 实例，SqlSession 完全包含了面向数据库执行 SQL 命令所需的所有方法。
        SqlSession session = sqlSessionFactory.openSession(true);
        // 4. 准备基本信息
        // 4.1) statement: 用来定位映射文件（StudentMapper.xml）中的语句（通过namespace id + select id)
        String statement = "mybatis.User1Dao.insertOne";
        // 4.2) paramter: 传进去的参数，也就是需要获取students表中主键值为1的记录
        User1 user1 = new User1(1, "111");
        // 5. SqlSession 实例来直接执行已映射的 SQL 语句，selectOne表示获取的是一条记录
        session.insert(statement, user1);
        System.out.println(session.getConnection());
        session.commit();

        User1 user2 = new User1(2, "222");
        session.insert(statement, user2);
        System.out.println(session.getConnection());
        session.commit();
        // 6. 关闭输入流和SqlSession实例
//        in.close();
        reader.close();
        session.close();
    }
    
    @Test
    public void test1() throws IOException {
        // 1. 加载MyBatis的配置文件：mybatis.xml（它也加载关联的映射文件，也就是mappers结点下的映射文件）
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("mybatis.xml");
        // 2. SqlSessionFactoryBuidler实例将通过输入流调用build方法来构建 SqlSession 工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        // 3. 通过工厂获取 SqlSession 实例，SqlSession 完全包含了面向数据库执行 SQL 命令所需的所有方法。
        SqlSession session = sqlSessionFactory.openSession();
//        User1Dao mapper = session.getMapper(User1Dao.class);
        UserDao mapper = session.getMapper(UserDao.class);
    
//        User1 user = mapper.selectOne(1);
        User1 user = mapper.getBy(1);
        log.info(user.toString());
        // 6. 关闭输入流和SqlSession实例
        in.close();
        session.close();
    }
    
    @Test
    public void test2() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        User1Dao user1Dao = (User1Dao) context.getBean("user1Dao");
        User1 user1 = user1Dao.selectOne(1);
        log.info(user1.toString());
    }
    
}

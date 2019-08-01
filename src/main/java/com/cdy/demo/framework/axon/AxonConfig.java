package com.cdy.demo.framework.axon;

import com.cdy.demo.framework.jpa.JpaConfig;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.axonframework.modelling.command.GenericJpaRepository;
import org.axonframework.modelling.command.Repository;
import org.axonframework.spring.config.SpringAxonAutoConfigurer;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Slf4j
@Import({SpringAxonAutoConfigurer.ImportSelector.class,JpaConfig.class})
public class AxonConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AxonConfig.class);
        CommandGateway commandGateway = applicationContext.getBean(CommandGateway.class);
        log.info("start");
        AccountId id = new AccountId();
        log.debug("Account id: {}", id.toString());
        commandGateway.send(new CreateAccountCommand(id, "MyAccount",1000));
        commandGateway.send(new WithdrawMoneyCommand(id, 500));
        commandGateway.send(new WithdrawMoneyCommand(id, 300));
        commandGateway.send(new CreateAccountCommand(id, "MyAccount", 1000));
        commandGateway.send(new WithdrawMoneyCommand(id, 500));
    }


    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public TransactionManager axonTransactionManager() {
        return new SpringTransactionManager(transactionManager);
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus.Builder().build();
    }

    /**
     * CommandBus在初始化时，需要提供一个TransactionManager，如果直接调用SimpleCommandBus的无参构造器，
     * 默认是NoTransactionManager.INSTANCE。本例测试时把几个command放在一个线程里串行执行，
     * 如果不提供TransactionManager，那么最终withdraw会失败。
     * 提供TransactionManager的方式有两种：
     *
     * 1. 如上例中直接构造器中指定；
     * 2. 注册一个TransactionManagingInterceptor；
     * @return
     */
    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus build = SimpleCommandBus.builder().transactionManager(axonTransactionManager()).build();
//        SimpleCommandBus commandBus = new SimpleCommandBus(, NoOpMessageMonitor.INSTANCE);
        //commandBus.registerHandlerInterceptor(transactionManagingInterceptor());
        return build;
    }

    @Bean
    public TransactionManagingInterceptor transactionManagingInterceptor() {
        return new TransactionManagingInterceptor(new SpringTransactionManager(transactionManager));
    }

    /**
     * 该Provider会提供具体的EntityManager来管理持久化。
     * @return
     */
    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    /**
     * 这里我们直接声明BankAccount对应的Repository为一个GenericJpaRepository,来直接保存Aggregate的状态。
     * @return
     */
    @Bean
    public Repository<BankAccount> accountRepository() {
        return GenericJpaRepository.builder(BankAccount.class).entityManagerProvider(entityManagerProvider())
                .eventBus(eventBus()).build();
//        return new GenericJpaRepository<BankAccount>(entityManagerProvider(), BankAccount.class, eventBus());
    }
}
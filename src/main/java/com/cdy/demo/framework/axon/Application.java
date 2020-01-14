//package com.cdy.demo.framework.axon;
//
//import lombok.extern.slf4j.Slf4j;
//import org.axonframework.commandhandling.CommandBus;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.axonframework.config.Configuration;
//import org.axonframework.config.DefaultConfigurer;
//import org.axonframework.eventhandling.EventBus;
//import org.axonframework.eventsourcing.eventstore.EventStore;
//import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
//import org.axonframework.queryhandling.QueryBus;
//import org.axonframework.queryhandling.QueryGateway;
//
//@Slf4j
//public class Application {
//    public static void main(String args[]) {
//        Configuration config = DefaultConfigurer.defaultConfiguration()
//                .configureAggregate(BankAccount.class)
//                .configureEmbeddedEventStore(c -> new InMemoryEventStorageEngine())
//                .buildConfiguration();
//        config.start();
//        AccountId id = new AccountId();
//        CommandGateway gateway = config.commandGateway();
//        CommandBus commandBus = config.commandBus();
//        EventBus eventBus = config.eventBus();
//        EventStore eventStore = config.eventStore();
//        config.onStart(()-> System.out.println("start"));
//        QueryBus queryBus = config.queryBus();
//        QueryGateway queryGateway = config.queryGateway();
//        gateway.send(new CreateAccountCommand(id, "MyAccount", 1000));
//        gateway.send(new WithdrawMoneyCommand(id, 500));
//        gateway.send(new WithdrawMoneyCommand(id, 500));
//    }
//}
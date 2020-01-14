package com.cdy.demo.framework.r2dbc;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.h2.H2ConnectionOption;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class R2dbcDemo {

    public static void main(String[] args) {


        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

        DatabaseClient client = DatabaseClient.create(connectionFactory);

        client.execute("CREATE TABLE person" +
                "(id VARCHAR(255) PRIMARY KEY," +
                "name VARCHAR(255)," +
                "age INT)")
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        client.insert()
                .into(Person.class)
                .using(new Person("joe", "Joe", 34))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();

        client.select()
                .from(Person.class)
                .fetch()
                .first()
                .doOnNext(it -> log.info(it.toString()))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }


    public static void testR2dbc(){
        H2ConnectionFactory connectionFactory = new H2ConnectionFactory(H2ConnectionConfiguration.builder()
                .file("D:/H2/h2db")
                .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
                .build());


        connectionFactory.create()
                .flatMapMany(conn ->
                        conn.createStatement("SELECT id FROM TEST ")
                                .execute()
                                .flatMap(result ->
                                        result.map((row, metadata) -> row.get("id")))

                ).subscribe(e -> System.out.println(e));

        connectionFactory.create()
                .flatMapMany(conn ->
                        conn.beginTransaction()
                                .thenMany(conn.createStatement("INSERT INTO TEST VALUES($1,$2)")
                                        .bind("$1", 10000)
                                        .bind("$2", "cdy")
                                        .execute())
                                .delayUntil(p -> conn.commitTransaction())
                                .onErrorResume(t ->
                                        conn.rollbackTransaction()
                                                .then(Mono.error(t)))
                );

    }


}

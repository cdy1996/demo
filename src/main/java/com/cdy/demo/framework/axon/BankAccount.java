package com.cdy.demo.framework.axon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * JPA要求Entity必须有一个ID，GenericJpaRepository默认使用String作为EntityId的类型，而这里并没有直接用String，将会在存储时报
 * java.lang.IllegalArgumentException: Provided id of the wrong type for class com.edi.learn.axon.aggregates.BankAccount. Expected: class com.edi.learn.axon.domain.AccountId, got class java.lang.String
 * 解决方法是把@Id，@Column加在getter方法上。
 */
@Slf4j
@NoArgsConstructor @Getter @Setter
@Aggregate(repository = "accountRepository")
@Entity
public class BankAccount {
    @AggregateIdentifier
    private AccountId accountId;
    private String accountName;
    private BigDecimal balance;

    @Id
    public String getAccountId() {
        return accountId.toString();
    }

    @Column
    public String getAccountName() {
        return accountName;
    }

    @Column
    public BigDecimal getBalance() {
        return balance;
    }

    @CommandHandler
    public BankAccount(CreateAccountCommand command){
        log.info("接收到创建账号的命令, 创建一个新的账号");
        apply(new AccountCreatedEvent(command.getAccountId(), command.getAccountName(), command.getAmount()));
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command){
        log.info("处理钱的操作");
        apply(new MoneyWithdrawnEvent(command.getAccountId(), command.getAmount()));
    }

    @EventHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getAccountId();
        this.accountName = event.getAccountName();
        this.balance = new BigDecimal(event.getAmount());
        log.info("Account {} is created with balance {}", accountId, this.balance);
    }

    @EventHandler
    public void on(MoneyWithdrawnEvent event){
        BigDecimal result = this.balance.subtract(new BigDecimal(event.getAmount()));
        if(result.compareTo(BigDecimal.ZERO)<0)
            log.error("Cannot withdraw more money than the balance!");
        else {
            this.balance = result;
            log.info("Withdraw {} from account {}, balance result: {}", event.getAmount(), accountId, balance);
        }
    }
}
//package com.cdy.demo.framework.axon;
//
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.axonframework.modelling.command.TargetAggregateIdentifier;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class CreateAccountCommand {
//    @TargetAggregateIdentifier
//    private AccountId accountId;
//    private String accountName;
//    private long amount;
//    public CreateAccountCommand(AccountId accountId, String accountName, long amount) {
//        this.accountId = accountId;
//        this.accountName = accountName;
//        this.amount = amount;
//    }
//}
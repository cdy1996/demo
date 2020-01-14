//package com.cdy.demo.framework.axon;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.axonframework.modelling.command.TargetAggregateIdentifier;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class WithdrawMoneyCommand {
//    @TargetAggregateIdentifier
//    private AccountId accountId;
//    private long amount;
//    public WithdrawMoneyCommand(AccountId accountId, long amount) {
//        this.accountId = accountId;
//        this.amount = amount;
//    }
//}
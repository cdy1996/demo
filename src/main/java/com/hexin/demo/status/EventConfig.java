package com.hexin.demo.status;

//@WithStateMachine
//public class EventConfig {
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//
//    @OnTransition(target = "UNPAID")
//    public void create() {
//        logger.info("订单创建，待支付");
//    }
//
//    @OnTransition(source = "UNPAID", target = "WAITING_FOR_RECEIVE")
//    public void pay() {
//        logger.info("用户完成支付，待收货");
//    }
//
//    @OnTransition(source = "WAITING_FOR_RECEIVE", target = "DONE")
//    public void receive() {
//        logger.info("用户已收货，订单完成");
//    }
//}
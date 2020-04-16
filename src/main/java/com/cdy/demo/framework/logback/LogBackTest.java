package com.cdy.demo.framework.logback;

import ch.qos.logback.classic.util.ContextInitializer;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogBackTest {

    @Test
    public void test() {
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "src/main/java/com/cdy/demo/framework/logback/logback.xml");

        org.slf4j.Logger log = LoggerFactory.getLogger(LogBackTest.class);
        MDC.put("REQUEST_ID", "1");
        log.info("----------------------");
        MDC.remove("REQUEST_ID");
    }
}

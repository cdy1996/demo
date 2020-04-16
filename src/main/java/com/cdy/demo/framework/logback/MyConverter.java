package com.cdy.demo.framework.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MyConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {

        String threadName = event.getThreadName();
        StackTraceElement[] callerData = event.getCallerData();
        int lineNumber = callerData[0].getLineNumber();
        String className = callerData[0].getClassName();
        String methodName = callerData[0].getMethodName();

        return threadName + "-" + className + "#" + methodName + ":" + lineNumber;
    }
}

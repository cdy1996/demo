package com.cdy.demo.repeatedWheels.myasyncsemaphore;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface AsyncSemaphoneTask<T> extends Callable<T> {

}

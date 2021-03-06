package com.cdy.demo.framework.rx.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * todo
 * Created by 陈东一
 * 2020/5/14 0014 22:21
 */
public class MonitorDemo {


    public static void main(String[] args) throws InterruptedException {

        UnicastProcessor<MetricMonitor> metricMonitorFlux = SQL.metricMonitorFlux;
        UnicastProcessor<MetricMonitor> metricMonitorFlux1 = Filter.metricMonitorFlux;
        Flux.defer(() -> Flux.merge(metricMonitorFlux, metricMonitorFlux1));

    }

    static class SQL {
        static FluxSink<MetricMonitor> sink = null;
        static AtomicLong open = new AtomicLong(0);
        static UnicastProcessor<MetricMonitor> metricMonitorFlux = UnicastProcessor.create();

        public static void monitorFilter() throws InterruptedException {

            FluxSink<MetricMonitor> sink = metricMonitorFlux.sink(FluxSink.OverflowStrategy.BUFFER);
//        Flux<MetricMonitor> metricMonitorFlux = DirectProcessor.create(e -> sink = e);

            Thread thread = new Thread(() -> {
                while (true) {

                    if (open.decrementAndGet() >= 0) {
                        long start = System.currentTimeMillis();
                        // service()
                        MetricMonitor t = new MetricMonitor();
                        try {
                            TimeUnit.SECONDS.sleep(1L);
                            t.time = System.currentTimeMillis() - start;
                            t.success = true;
                        } catch (InterruptedException e) {
                            t.success = false;
                            e.printStackTrace();
                        } finally {
                            sink.next(t);
                        }
//                    return;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // service()
                }

            });

            metricMonitorFlux.subscribe(new Subscriber<MetricMonitor>() {
                Subscription s;

                @Override
                public void onSubscribe(Subscription s) {
                    this.s = s;
                    s.request(1);

                }

                @Override
                public void onNext(MetricMonitor metricMonitor) {
                    System.out.println(metricMonitor.success + "->" + metricMonitor.time);
                    s.request(1);
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println(t);
                }

                @Override
                public void onComplete() {
                    System.out.println("end");
                }
            });
            sink.onRequest(e -> {
                open.accumulateAndGet(e, Long::sum);
            });

            thread.start();
            thread.join();
        }


    }

    static class Filter {
        static FluxSink<MetricMonitor> sink = null;
        static AtomicLong open = new AtomicLong(0);
        static UnicastProcessor<MetricMonitor> metricMonitorFlux = UnicastProcessor.create();

        public static void monitorFilter() throws InterruptedException {

            FluxSink<MetricMonitor> sink = metricMonitorFlux.sink(FluxSink.OverflowStrategy.BUFFER);
//        Flux<MetricMonitor> metricMonitorFlux = DirectProcessor.create(e -> sink = e);

            Thread thread = new Thread(() -> {
                while (true) {

                    if (open.decrementAndGet() >= 0) {
                        long start = System.currentTimeMillis();
                        // service()
                        MetricMonitor t = new MetricMonitor();
                        try {
                            TimeUnit.SECONDS.sleep(1L);
                            t.time = System.currentTimeMillis() - start;
                            t.success = true;
                        } catch (InterruptedException e) {
                            t.success = false;
                            e.printStackTrace();
                        } finally {
                            sink.next(t);
                        }
//                    return;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // service()
                }

            });

            metricMonitorFlux.subscribe(new Subscriber<MetricMonitor>() {
                Subscription s;

                @Override
                public void onSubscribe(Subscription s) {
                    this.s = s;
                    s.request(1);

                }

                @Override
                public void onNext(MetricMonitor metricMonitor) {
                    System.out.println(metricMonitor.success + "->" + metricMonitor.time);
                    s.request(1);
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println(t);
                }

                @Override
                public void onComplete() {
                    System.out.println("end");
                }
            });
            sink.onRequest(e -> {
                open.accumulateAndGet(e, Long::sum);
            });

            thread.start();
            thread.join();
        }


    }


    static class MetricMonitor {

        long time;
        boolean success;
    }
}

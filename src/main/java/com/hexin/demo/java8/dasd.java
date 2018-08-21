package com.hexin.demo.java8;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

/**
 * Created by viruser on 2018/7/13.
 */
public class dasd {

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("猪肉", false, 800, Dish.Type.肉),
                new Dish("牛肉", false, 700, Dish.Type.肉),
                new Dish("肌肉", false, 400, Dish.Type.肉),
                new Dish("薯条", true, 530, Dish.Type.其他),
                new Dish("米饭", true, 350, Dish.Type.其他),
                new Dish("季果", true, 120, Dish.Type.其他),
                new Dish("披萨", true, 550, Dish.Type.其他),
                new Dish("对虾", false, 300, Dish.Type.鱼),
                new Dish("三文鱼", false, 450, Dish.Type.鱼) );


        Stream<Dish> menuStream = menu.stream();
        Results results = new StreamForker<>(menuStream)
                .fork("菜单列表", s -> s.map(Dish::getName)
                        .collect(joining(", ")))
                .fork("总的卡路里", s -> s.mapToInt(Dish::getCalories).sum())
                .fork("最高的卡路里",
                        s -> s.reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)
                        .get())
                .fork("分类", s -> s.collect(groupingBy(Dish::getType)))
                .getResults();


        String shortMenu = results.get("菜单列表");
        int totalCalories = results.get("总的卡路里");
        Dish mostCaloricDish = results.get("最高的卡路里");
        Map<Dish.Type, List<Dish>> dishesByType = results.get("分类");

        String collect = menu.stream().map(e -> e.getName()).collect(Collectors.joining(","));
        System.out.println("菜单列表: " + collect);
        System.out.println("菜单列表: " + shortMenu);
        int sum = menu.stream().map(e -> e.getCalories()).mapToInt(Integer::intValue).sum();

        System.out.println("总的卡路里: " + totalCalories);
        System.out.println("总的卡路里: " +sum);


        OptionalInt max = menu.stream().map(e -> e.getCalories()).mapToInt(Integer::intValue).max();
        Optional<Integer> collect1 = menu.stream().map(e -> e.getCalories()).max((a, b) -> a - b);
        Optional<Integer> collect2 = menu.stream().map(e -> e.getCalories()).collect(Collectors.maxBy((a, b) -> a - b));
        Optional<Integer> collect3 = menu.stream().map(e -> e.getCalories()).collect(Collectors.maxBy(Comparator.comparingInt(a -> a)));

        Optional<Dish> collect5 = menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(a -> a.getCalories())));
        Dish dish = menu.stream().reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2).get();

        System.out.println("最高的卡路里: " + mostCaloricDish);
        System.out.println("最高的卡路里: " +dish);

        Map<Dish.Type, List<Dish>> collect4 = menu.stream().collect(Collectors.groupingBy(Dish::getType));

        System.out.println("分类: " + dishesByType);
        System.out.println("分类: " + collect4);

    }
}

interface Results {
    public <R> R get(Object key);
}

//复制流
class StreamForker<T> {
    private final Stream<T> stream;
    private final Map<Object, Function<Stream<T>, ?>> forks =
            new HashMap<>();

    public StreamForker(Stream<T> stream) {
        this.stream = stream;
    }

    public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
        forks.put(key, f);
        return this;
    }

    public Results getResults() {
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer);
        } finally {
            consumer.finish();
        }
        return consumer;
    }


    private ForkingStreamConsumer<T> build() {
        List<BlockingQueue<T>> queues = new ArrayList<>();
        Map<Object, Future<?>> actions =
                forks.entrySet().stream().reduce(
                        new HashMap<Object, Future<?>>(),
                        (map, e) -> {
                            map.put(e.getKey(),
                                    getOperationResult(queues, e.getValue()));
                            return map;
                        }, (m1, m2) -> {
                            m1.putAll(m2);
                            return m1;
                        });
        return new ForkingStreamConsumer<>(queues, actions);
    }


    private Future<?> getOperationResult(List<BlockingQueue<T>> queues,
                                         Function<Stream<T>, ?> f) {
        BlockingQueue<T> queue = new LinkedBlockingQueue<>();
        queues.add(queue);
        Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
        Stream<T> source = StreamSupport.stream(spliterator, false);
        return CompletableFuture.supplyAsync(() -> f.apply(source));
    }
}


class ForkingStreamConsumer<T> implements Consumer<T>, Results {
    static final Object END_OF_STREAM = new Object();
    private final List<BlockingQueue<T>> queues;
    private final Map<Object, Future<?>> actions;

    ForkingStreamConsumer(List<BlockingQueue<T>> queues,
                          Map<Object, Future<?>> actions) {
        this.queues = queues;
        this.actions = actions;
    }

    @Override
    public void accept(T t) {
        queues.forEach(q -> q.add(t));
    }

    void finish() {
        accept((T) END_OF_STREAM);
    }

    @Override
    public <R> R get(Object key) {
        try {
            return ((Future<R>) actions.get(key)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class BlockingQueueSpliterator<T> implements Spliterator<T> {
    private final BlockingQueue<T> q;

    BlockingQueueSpliterator(BlockingQueue<T> q) {
        this.q = q;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {

        T t;
        while (true) {
            try {
                t = q.take();
                break;
            } catch (InterruptedException e) {
            }
        }
        if (t != ForkingStreamConsumer.END_OF_STREAM) {
            action.accept(t);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
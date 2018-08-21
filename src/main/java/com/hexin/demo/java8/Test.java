package com.hexin.demo.java8;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by viruser on 2018/7/9.
 */
public class Test {

    public static void main(String[] args) {

//        String[] words =new String[] {"Hello","World"};
//        List<String> a = new ArrayList<>(Arrays.asList(words));
//        List<String> uniqueCharacters =
//                a.stream()
//                        .map(w -> w.split(""))
//                        .flatMap(Arrays::stream)
//                        .distinct()
//                        .collect(Collectors.toList());
//        uniqueCharacters.forEach(System.out::println);


        List<Integer> numbers1 = Arrays.asList(1, 2, 3);


        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs =
                numbers1.stream()
                        .flatMap(i -> numbers2.stream()
                                .map(j -> new int[]{i, j})
                        )
                        .collect(toList());
        List<Stream<int[]>> collect = numbers1.stream()
                .map(i -> numbers2.stream()
                        .map(j -> new int[]{i, j})
                )
                .collect(toList());


        Stream<int[]> pythagoreanTriples =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a ->
                                IntStream.rangeClosed(a, 100)
                                        .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                                        .mapToObj(b ->
                                                new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                        );

//        pythagoreanTriples.forEach(e->{
//            for (int i = 0; i < e.length; i++) {
//
//                System.out.print(e[i]);
//            }
//            System.out.println();
//        });




    }

    @org.junit.Test
    public  void testStream(){
        Stream.iterate(new int[]{0, 1},
                n -> new int[]{n[n.length-1], n[n.length - 2] + n[n.length-1]})
                .limit(20)
                .forEach(e-> System.out.println(Arrays.toString(e)));

        Stream.iterate(new int[]{0, 1},
                n -> new int[]{n[n.length-1], n[n.length - 2] + n[n.length-1]})
                .limit(20)
                .flatMap(a-> Arrays.stream(a).boxed())
                .forEach(e-> System.out.println(e));

        Stream.iterate(new int[]{0, 1},
                n -> new int[]{n[n.length-1], n[n.length - 2] + n[n.length-1]})
                .limit(20)
                .map(t->t[0])
                .forEach(e-> System.out.println(e+","));

    }


    @org.junit.Test
    public  void testCollect(){
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
//
//        String shortMenu = menu.stream().map(Dish::getName).collect(joining(","));
//        System.out.println(shortMenu);
//
//        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel =
                menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return
                            CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                } ));



//        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream().collect(
                        groupingBy(Dish::getType,
                                groupingBy(dish -> {
                                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                    else return CaloricLevel.FAT;
                                } )
                        )
                );

//        Map<Dish.Type, Optional<Dish>> mostCaloricByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                maxBy(comparingInt(Dish::getCalories))));

//        Map<Dish.Type, Dish> mostCaloricByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                collectingAndThen(
                                        maxBy(comparingInt(Dish::getCalories)),
                                        Optional::get)));


        Map<Dish.Type, Integer> collect1 = menu.stream().collect(groupingBy(Dish::getType,
                summingInt(Dish::getCalories)));

        Map<Dish.Type, Integer> collect2 = menu.stream().collect(groupingBy(Dish::getType,
                reducing(0, Dish::getCalories, Integer::sum)));

        Map<Dish.Type, Optional<Dish>> collect = menu.stream().collect(groupingBy(Dish::getType,
                minBy(comparingInt(Dish::getCalories))
        ));

//        Map<Boolean, List<Dish>> partitionedMenu =
                menu.stream().collect(partitioningBy(Dish::isVegetarian));


                menu.stream().collect(
                        partitioningBy(Dish::isVegetarian,
                                collectingAndThen(
                                        maxBy(Comparator.comparingInt(Dish::getCalories)),
                                        Optional::get
                                )
                        )
                );
        Map<Boolean, Map<Boolean, List<Dish>>> collect3 = menu.stream().collect(partitioningBy(Dish::isVegetarian,
                partitioningBy(d -> d.getCalories() > 500)));

        Map<Boolean, Long> collect4 = menu.stream().collect(partitioningBy(Dish::isVegetarian,
                counting()));


    }


    public enum CaloricLevel { DIET, NORMAL, FAT }

    public boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }


    /**
     * 质数的判定
     */
    @org.junit.Test
    public void testStream3(){
        Map<Boolean, List<Integer>> collect = IntStream.rangeClosed(2, 10).boxed()
                .collect(
                        partitioningBy(candidate -> isPrime(candidate)));
        System.out.println(collect);
        Map<Boolean, List<Integer>> collect1 = IntStream.rangeClosed(2, 10).boxed()
                .collect(new PrimeNumbersCollector());
        System.out.println(collect1);
    }


    @org.junit.Test
    public void testCollector(){
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
        List<Dish> collect = menu.stream().collect(new ToListCollector<Dish>());

        ArrayList<Object> collect1 = menu.stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);



    }


    /**
     * 截取列表从0到第一个不符合条件的值的子列表
     * @param list 列表
     * @param p    条件
     * @param <A>  指定泛型
     * @return     符合条件的子列表
     */
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.subList(0, i);
            }
            i++;
        }
        return list;
    }

    /**
     * 判断是否是质数
     * @param primes     质数列表列表
     * @param candidate  指定的数
     * @return 是否是质数
     */
    public static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i <= candidateRoot)
                .stream()
                .noneMatch(p -> candidate % p == 0);
    }

    final String SENTENCE =
            " Nel mezzo del cammin di nostra vita " +
                    "mi ritrovai in una selva oscura" +
                    " ché la dritta via era smarrita ";

    public int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }

    @org.junit.Test
    public void testWords(){
//        System.out.println("Found " + countWordsIteratively(SENTENCE) + " words");

//        Arrays.stream(SENTENCE.toCharArray()).

        Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                .mapToObj(SENTENCE::charAt);
        System.out.println("Found " + countWords(stream) + " words");

        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> stream2 = StreamSupport.stream(spliterator, true);
        System.out.println("Found " + countWords(stream2) + " words");

    }


    private int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
        return wordCounter.getCounter();
    }


    @org.junit.Test
    public void testDebug(){
//        List<Integer> points = Arrays.asList(1,2, 1/0);
//        points.stream().forEach(System.out::println);
        List<Integer> numbers = Arrays.asList(2, 3, 4, 5);
        List<Integer> result =
                numbers.stream()
                        .peek(x -> System.out.println("from stream: " + x))
                        .map(x -> x + 17)
                        .peek(x -> System.out.println("after map: " + x))
                        .filter(x -> x % 2 == 0)
                        .peek(x -> System.out.println("after filter: " + x))
                        .limit(3)
                        .peek(x -> System.out.println("after limit: " + x))
                        .collect(toList());
    }


    @org.junit.Test
    public void testOptional(){
//        Person person= new Person();
//        Optional<Person> optPerson = Optional.of(person);
//        Optional<Optional<Car>> car = optPerson.map(Person::getCar);
//        Optional<Car> car1 = optPerson.flatMap(Person::getCar);
//
//        optPerson.flatMap(Person::getCar)
//                .flatMap(Car::getInsurance)
//                .map(Insurance::getName)
//                .orElseThrow(RuntimeException::new);
//                .orElse("Unknown");



        Properties props = new Properties();
        props.setProperty("a", "5");
        props.setProperty("b", "true");
        props.setProperty("c", "-3");


        assertEquals(5, readDuration(props, "a"));
        assertEquals(0, readDuration(props, "b"));
        assertEquals(0, readDuration(props, "c"));
        assertEquals(0, readDuration(props, "d"));




    }


    public int readDuration(Properties props, String name){
        return Optional.ofNullable(props.getProperty(name))
                .flatMap(e->{
                    try {
                        return Optional.of(Integer.parseInt(e));
                    } catch (NumberFormatException e1) {
                        return Optional.empty();
                    }
                })
                .filter(i->i>0)
                .orElse(0);

    }



    public class Person {
        private Optional<Car> car;
        public Optional<Car> getCar() { return car; }
    }
    public class Car {
        private Optional<Insurance> insurance;
        public Optional<Insurance> getInsurance() { return insurance; }
    }
    public class Insurance {
        private String name;
        public String getName() { return name; }
    }


    public class Shop {

        String name;
        String product;
        double price;

        public Shop(String name) {
            this.name = name;
        }

        public double getPrice(String product) {
            try {
                double v = Math.random() * 5;
                TimeUnit.SECONDS.sleep(Double.valueOf(v).intValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Math.random()*10000;
        }

        public String getName() {
            return name;
        }
    }

    List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    @org.junit.Test
    public void testCompleteFuture(){


        List<String> collect = shops.stream()
                .parallel()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice("123")))
                .collect(toList());
//        System.out.println(collect);


//        List<CompletableFuture<String>> priceFutures =

        long start = System.nanoTime();
        CompletableFuture[] completableFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(), shop.getPrice("123"))))
                .map(f -> f.thenAccept(
                        s -> System.out.println(s + " (done in " +
                                ((System.nanoTime() - start) / 1_000_000) + " msecs)"))
                )
                .toArray(size -> new CompletableFuture[4]);
        CompletableFuture.allOf(completableFutures).join();
//                        .collect(toList());

//     priceFutures.stream()
//                .map(CompletableFuture::join)
//                .collect(toList());
//        System.out.println(collect1);
    }
    private final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                    r -> {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    });


    @org.junit.Test
    public void testLocal(){

        LocalDate date = LocalDate.of(2014, 3, 18);
        int year = date.getYear();
        Month month = date.getMonth();
        int day = date.getDayOfMonth();
        DayOfWeek dow = date.getDayOfWeek();
        int len = date.lengthOfMonth();
        boolean leap = date.isLeapYear();


        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        LocalDate date1 = LocalDate.parse("2014-03-18");
        LocalTime time1= LocalTime.parse("13:45:20");



        LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20);
        LocalDateTime dt2 = LocalDateTime.of(date, time);
        LocalDateTime dt3 = date.atTime(13, 45, 20);
        LocalDateTime dt4 = date.atTime(time);
        LocalDateTime dt5 = time.atDate(date);


        Duration d1 = Duration.between(dt1, dt2);
        Period tenDays = Period.between(LocalDate.of(2014, 3, 8),
                LocalDate.of(2014, 3, 18));


        LocalDate date11 = LocalDate.of(2014, 3, 18);
        LocalDate date2 = date11.withYear(2011);
        LocalDate date3 = date2.withDayOfMonth(25);
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 9);


    }

    static DoubleUnaryOperator curriedConverter(double f, double b){
        return (double x) -> x * f + b;
    }
    static Function<Double, Double> get(double f){
        return x->x+f;
    }

    @org.junit.Test
    public void testKLH(){
        DoubleUnaryOperator convertCtoF = curriedConverter(9.0/5, 32);
        DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
        DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);

        double gbp = convertUSDtoGBP.applyAsDouble(1000);
    }

    class TrainJourney {
        public int price;
        public TrainJourney onward;
        public TrainJourney(int p, TrainJourney t) {
            price = p;
            onward = t;
        }
    }


}

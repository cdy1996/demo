package com.hexin.demo.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by viruser on 2018/6/29.
 */
public class FunctionTest {

    public static void main(String[] args) {

//        MyFunction myFunction = FunctionTest::let;
//        System.out.println(FunctionTest.let1(myFunction, "b"));


        Function<List<TradeRankDto>, List<Object[]>> function = FunctionTest::let2;
        List<TradeRankDto> list = new ArrayList<>();
        list.add(new TradeRankDto().setName("111").setRealname("1111"));
        list.add(new TradeRankDto().setName("222").setRealname("2222"));
        List<String> title = new ArrayList<>();
        title.add("1");
        title.add("2");
        write(title, list,function);


        write(title, list, e->{
            List<Object[]> listListFunction = new ArrayList<>();
            for (TradeRankDto tradeRankDto : e) {
                Object[] line = new Object[2];
                line[0] = tradeRankDto.getName();
                line[1] = tradeRankDto.getRealname();
                listListFunction.add(line);
            }
            return listListFunction;
        });



    }


    public static <T> void write(List<String> title, List<T> list, Function<List<T>, List<Object[]>> function) {
        exportExcel(title,list,function.apply(list));
    }

    private static <T> void exportExcel(List<String> title,List<T> list, List<Object[]> apply) {
        for (T t :  list) {
            TradeRankDto t1 = (TradeRankDto) t;
            System.out.println(t1.getName()+":"+t1.getRealname());
        }
    }

    public static <T> List<Object[]> let2(List<T> a){
        List<Object[]> listListFunction = new ArrayList<>();
        for (T tradeRankDto : a) {
            Object[] line = new Object[2];
            line[0] = ((TradeRankDto)tradeRankDto).getName();
            line[1] = ((TradeRankDto)tradeRankDto).getRealname();
            listListFunction.add(line);
        }
        return listListFunction;
    }

    public static String let(String a){
        return a.toLowerCase();
    }


    public static String let1(MyFunction a, String b){
        return a.get(b);
    }
}

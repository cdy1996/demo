package com.cdy.demo.tool;

import org.apache.commons.math3.stat.descriptive.moment.*;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.junit.Test;

import java.math.BigDecimal;

public class MathTest {

@Test
public void test(){
    BigDecimal a = BigDecimal.ZERO;
    BigDecimal b = BigDecimal.ZERO;
    BigDecimal add = a.add(BigDecimal.ONE);
    BigDecimal add1 = b.add(BigDecimal.ONE);
    System.out.println(add);
    System.out.println(add1);


}

    public static void main(String[] args) {

        double[] values = new double[] { 0.33, 1.33,0.27333, 0.3, 0.501,

                0.444, 0.44, 0.34496, 0.33,0.3, 0.292, 0.667 };

        Min min = new Min();

        Max max = new Max();



        Mean mean = new Mean(); // 算术平均值

        Product product = new Product();//乘积

        Sum sum = new Sum();

        Variance variance = new Variance();//方差

        System.out.println("min: " +min.evaluate(values));

        System.out.println("max: " +max.evaluate(values));

        System.out.println("mean: " +mean.evaluate(values));

        System.out.println("product:" + product.evaluate(values));

        System.out.println("sum: " +sum.evaluate(values));

        System.out.println("variance:" + variance.evaluate(values));



        Percentile percentile = new Percentile(); // 百分位数

        GeometricMean geoMean = new GeometricMean(); // 几何平均数,n个正数的连乘积的n次算术根叫做这n个数的几何平均数

        Skewness skewness = new Skewness(); //Skewness();

        Kurtosis kurtosis = new Kurtosis(); //Kurtosis,峰度

        SumOfSquares sumOfSquares = new SumOfSquares(); // 平方和

        StandardDeviation StandardDeviation =new StandardDeviation();//标准差

        System.out.println("80 percentilevalue: "

                + percentile.evaluate(values,80.0));

        System.out.println("geometricmean: " + geoMean.evaluate(values));

        System.out.println("skewness:" + skewness.evaluate(values));

        System.out.println("kurtosis:" + kurtosis.evaluate(values));

        System.out.println("sumOfSquares:" + sumOfSquares.evaluate(values));

        System.out.println("StandardDeviation: " +StandardDeviation.evaluate(values));



        System.out.println("-------------------------------------");



    }
}

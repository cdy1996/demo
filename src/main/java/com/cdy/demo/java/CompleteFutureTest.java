package com.cdy.demo.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CompleteFutureTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Double> priceAsync = getPriceAsync("!23");
        System.out.println(priceAsync.get());

    }

    public static Future<Double>  getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }

    private static double calculatePrice(String product) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

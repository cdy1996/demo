package com.hexin.demo.java8;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 字符统计分割类
 * Created by viruser on 2018/7/11.
 */
public class WordCounterSpliterator implements Spliterator<Character> {

    private final String string;
    private int currentChar = 0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }



    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        //字符还没数完，就返回true
        return currentChar < string.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        if (currentSize < 10) {
            //不需要拆了
            return null;
        }
        // 选择中间的位置拆
        for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
            //前进到空格为止
            if (Character.isWhitespace(string.charAt(splitPos))) {

                Spliterator<Character> spliterator =
                        new WordCounterSpliterator(string.substring(currentChar,
                                splitPos));
                currentChar = splitPos;
                return spliterator;
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    /**
     * characteristic方法告诉框架这个Spliterator是ORDERED（顺序就是String
     * 中各个Character的次序）、SIZED（estimatedSize方法的返回值是精确的）、
     * SUBSIZED（trySplit方法创建的其他Spliterator也有确切大小）、NONNULL（String
     * 中不能有为null 的Character ） 和IMMUTABLE （ 在解析String 时不能再添加
     * Character，因为String本身是一个不可变类）的
     */
    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}

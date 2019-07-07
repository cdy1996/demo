package com.cdy.demo.algorithm;

/**
 * ip整数和ip字符串互转工具类
 */
public class IpUtil {

    private static final char SPILT = '.';
    private static final char ZERO = '0';

    /**
     * ip字符串转成int
     *
     * @param ip 字符串
     * @return int
     */
    public static int ipToInt(String ip) {
        char[] chars = ip.toCharArray();
        int[] ints = new int[4];
        int result = 0;
        for (int i = chars.length - 1, j = 3, ten = 1; i >= 0; i--) {
            if (SPILT == chars[i]) {
                j--; ten = 1; continue;
            }
            int temp = chars[i] - ZERO;
            ints[j] += temp * ten;
            ten *= 10;
        }
        //每8位存一个ip段
        for (int i = 0; i < ints.length; i++) {
            result <<= 8;
            result ^= ints[i] & 255;
        }
        return result;
    }

    /**
     * ip整数转成字符串
     *
     * @param ip 整数
     * @return string
     */
    public static String ipToStr(int ip) {
        int[] ints = new int[4];
        for (int i = 0; i < ints.length; i++) {
            ints[3 - i] ^= ip & 255;
            ip >>>= 8;
        }
        return ints[0] + "." + ints[1] + "." + ints[2] + "." + ints[3];
    }

    public static void main(String[] args) {
        System.out.println(ipToStr(ipToInt("0.0.0.0")).equals("0.0.0.0"));
        System.out.println(ipToStr(ipToInt("125.125.125.125")).equals("125.125.125.125"));
        System.out.println(ipToStr(ipToInt("255.255.255.255")).equals("255.255.255.255"));
    }

}
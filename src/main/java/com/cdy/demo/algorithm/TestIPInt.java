package com.cdy.demo.algorithm;

import junit.framework.TestCase;

public class TestIPInt extends TestCase {

    public void testIPToInt() {
        int ip1 = 1;
        int ip2 = 1;
        int ip3 = 1;
        int ip4 = 2;
        //1.1.1.2 ==16843010
        //255.255.255.255 == -1
        int ipint = ipToInt(ip1, ip2, ip3, ip4);
        System.out.println("ipint:" + ipint);
        int[] ipArr = intToIp(ipint);
        for (int i = 0; i < 4; i++) {
            System.out.print(ipArr[i] + ".");
        }
        System.out.println();
    }

    /**
     * ip to int
     * sample:192 168 1 2
     *
     * @param ip1
     * @param ip2
     * @param ip3
     * @param ip4
     * @return
     */
    public int ipToInt(int ip1, int ip2, int ip3, int ip4) {
        return ipToInt(new int[]{ip1, ip2, ip3, ip4});
    }

    public int ipToInt(int[] ipArr) {
        int ipInt = 0;
        for (int i = 0; i < ipArr.length; i++) {
            ipInt <<= 8;
            ipInt ^= (byte) ipArr[i] & 255;
        }
        return ipInt;
    }


    public int[] intToIp(int ipInt) {
        int[] ipArr = new int[4];
        for (int i = 0; i < 4; i++) {
            ipArr[3 - i] ^= (byte) ipInt & 255;
            ipInt >>>= 8;
        }
        return ipArr;
    }

    private int byteToInt(byte[] bytes) {
//        return Bytes.toInt(bytes, 0);
        int n = 0;
        int offset = 0;
        int length = 4;
        for (int i = offset; i < offset + length; ++i) {
            n <<= 8;
            n ^= bytes[i] & 255;
        }
        return n;

    }

    private byte[] intToByte(int val) {
        int offset = 0;
        byte[] bytes = new byte[4];
        for (int i = offset + 3; i > offset; --i) {
            bytes[i] = (byte) val;
            val >>>= 8;
        }
        bytes[offset] = (byte) val;
        return bytes;
    }
}
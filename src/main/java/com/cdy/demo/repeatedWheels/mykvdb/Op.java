package com.cdy.demo.repeatedWheels.mykvdb;

/**
 * todo
 * Created by 陈东一
 * 2020/4/19 0019 15:46
 */
public enum Op {
    Put((byte) 0),
    Delete((byte) 1);
    
    private byte code;
    
    Op(byte code) {
        this.code = code;
    }
    
    public static Op code2Op(byte code) {
        switch (code) {
            case 0:
                return Put;
            case 1:
                return Delete;
            default:
                throw new IllegalArgumentException("Unknown code: " + code);
        }
    }
    
    public byte getCode() {
        return this.code;
    }
}
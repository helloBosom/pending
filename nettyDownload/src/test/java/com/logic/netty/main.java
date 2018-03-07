package com.logic.netty;

public class main {
    public static void main(String[] args) {
        String str = "D:\\netty.txt";
        System.out.println(str.substring(str.lastIndexOf("\\")).substring(1));
    }
}

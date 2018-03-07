package com.logic.reflect.application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunOtherZMain {
    /**
     * 调用其他类的main方法
     *
     * @throws NoSuchMethodException     无法找到某一特定方法时，抛出该异常。
     * @throws InvocationTargetException 是一种包装由调用方法或构造方法所抛出异常的经过检查的异常。
     * @throws IllegalAccessException    当应用程序试图反射性地创建一个实例（而不是数组）、设置或获取一个字段，或者调用一个方法，但当前正在执行的
     *                                   方法无法访问指定类、字段、方法或构造方法的定义时，抛出 IllegalAccessException。
     */
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ReplaceChar> replaceCharClass = ReplaceChar.class;
        Method mainMethod = replaceCharClass.getMethod("main", String[].class);
        //这里传入Num的main方法中时，会将string[]拆为3个string对象。报参数个数错误。
        //mainmethod.invoke(null,new String[]{"111","222","333"});
        //将string[]对象转为以object对象
        mainMethod.invoke(null, (Object) new String[]{"111", "222", "333"});
        //将string[]对象封装为一个object对象数组。
        mainMethod.invoke(null, new Object[]{new String[]{"111", "222", "333"}});
    }
}


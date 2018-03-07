package com.logic.reflect.application;

import com.logic.reflect.service.A;
import com.logic.reflect.model.HiddenC;

import java.lang.reflect.Method;

public class HiddenImplementation {
    public static void main(String[] args) throws Exception {
        A a = HiddenC.makeA();
        a.f();
        System.out.println(a.getClass().getName());
        // Oops! Reflection still allows us to call g():
        callHiddenMethod(a, "g");
        // And even methods that are less accessible!
        callHiddenMethod(a, "u");
        callHiddenMethod(a, "v");
        callHiddenMethod(a, "w");
    }

    /**
     * 任何修饰符下的方法或者field都可以通过反射获取
     *
     * @param a
     * @param methodName
     * @throws Exception
     */
    static void callHiddenMethod(Object a, String methodName) throws Exception {
        Method g = a.getClass().getDeclaredMethod(methodName);
        /**
         * 将此对象的 accessible 标志设置为指示的布尔值。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
         首先，如果存在安全管理器，则在 ReflectPermission("suppressAccessChecks") 权限下调用 checkPermission 方法。
         如果 flag 为 true，并且不能更改此对象的可访问性（例如，如果此元素对象是 Class 类的 Constructor 对象），则会引发 SecurityException。
         如果此对象是 java.lang.Class 类的 Constructor 对象，并且 flag 为 true，则会引发 SecurityException。
         */
        g.setAccessible(true);
        g.invoke(a);
    }
}

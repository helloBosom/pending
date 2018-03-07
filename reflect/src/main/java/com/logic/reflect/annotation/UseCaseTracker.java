package com.logic.reflect.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UseCaseTracker {
    public static void trackUseCases(List<Integer> useCases, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            UserCase userCase = m.getAnnotation(UserCase.class);
            if (userCase != null) {
                System.out.println("Found Use Case: " + userCase.id() + " " + userCase.description());
                useCases.remove(new Integer(userCase.id()));
            }
        }
        for (int i : useCases) {
            System.out.println("Warning: Missing use case  " + i);
        }
    }

    public static void main(String[] args) {
        List<Integer> useCases = new ArrayList<Integer>();
        Collections.addAll(useCases, 47, 48, 49, 50);
        trackUseCases(useCases, PasswordUtils.class);
    }
}
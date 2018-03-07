package com.logic.reflect.model;

import com.logic.reflect.service.A;

public class HiddenC {
    public static A makeA() {
        return new C();
    }
}
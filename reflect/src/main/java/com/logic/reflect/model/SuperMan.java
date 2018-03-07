package com.logic.reflect.model;

import com.logic.reflect.service.ActionInterface;

public class SuperMan implements ActionInterface {
    private boolean BlueBriefs;

    public void fly() {
        System.out.println("superman can fly!!～～");
    }

    public boolean isBlueBriefs() {
        return BlueBriefs;
    }

    public void setBlueBriefs(boolean blueBriefs) {
        BlueBriefs = blueBriefs;
    }

    @Override
    public void walk(int m) {
        System.out.println("superman walk～～" + m + "super man tired！");
    }
}

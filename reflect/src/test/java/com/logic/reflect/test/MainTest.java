package com.logic.reflect.test;

import com.logic.reflect.model.Person;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class MainTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Person person = new Person();
        person.produce();
        File file = new File("resources\\special.properties");
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(file);
        /**
         * 需要使用reader接口，可以指定编码方式。注意调整编码
         */
        properties.load(new InputStreamReader(inputStream, "utf-8"));
        for (int i = 0; i < properties.size(); i++) {
            String className = properties.getProperty("special" + (i + 1));
            Class<?> clazz = Class.forName(className);
            Person person1 = (Person) clazz.newInstance();
            person1.produce();
        }
        inputStream.close();
    }
}

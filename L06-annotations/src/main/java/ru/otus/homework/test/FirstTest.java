package ru.otus.homework.test;

import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.DisplayName;
import ru.otus.homework.annotations.Test;

public class FirstTest {

    @Before
    @DisplayName(name = "инициализация")
    void init(){
        System.out.println("init method");
    }

    @Test
    @DisplayName(name = "Первый успешный тест")
    void test1(){
        System.out.println("test1 method");

    }

    @Test
    @DisplayName(name = "Второй успешный тест")
    void test2(){
        System.out.println("test2 method");

    }

    @Test
    @DisplayName(name = "Тест завершается ошибкой")
    void test3(){
        System.out.println("test3");
        throw new RuntimeException();
    }

    @Test
    @DisplayName(name = "Третий успешный тест")
    void test4(){
        System.out.println("test4");
    }

    @Test
    void test5(){
        System.out.println("test5");
    }

    @Test
    void test6(){
        System.out.println("test6");
        throw new NullPointerException();
    }

    @After
    void tearDown(){
        System.out.println("after");
    }
}

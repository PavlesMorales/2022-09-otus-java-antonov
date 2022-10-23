package ru.otus.homework.test;

import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.DisplayName;
import ru.otus.homework.annotations.Test;

public class SecondTest {
    int x;

    @Before
    void init() {
        x = 13;
    }

    @Test
    @DisplayName(name = "ошибка когда х = 13")
    void test1() {
        if (x == 13) throw new RuntimeException();
    }

    @Test
    @DisplayName(name = "просто успешный тест")
    void test2() {
        x = 12;
    }

    @After
    void tearDown() {
        x = 0;
    }
}

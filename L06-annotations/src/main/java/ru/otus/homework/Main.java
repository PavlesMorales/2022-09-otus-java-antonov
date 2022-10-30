package ru.otus.homework;

import ru.otus.homework.test.FirstTest;
import ru.otus.homework.test.SecondTest;

import static ru.otus.homework.runners.Runner.run;

public class Main {
    public static void main(String[] args) {
        run(FirstTest.class);

        run(SecondTest.class);
    }
}

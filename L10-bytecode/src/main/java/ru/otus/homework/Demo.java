package ru.otus.homework;

public class Demo {

    public static void main(String[] args) {

        TestLoggingInterface testLoggingInterface = Ioc.createProxy(new TestLogging());
        testLoggingInterface.calculation(1);
        testLoggingInterface.calculation(2, 3);
        testLoggingInterface.calculation(4, 5, "String");
        testLoggingInterface.calculation();
    }
}

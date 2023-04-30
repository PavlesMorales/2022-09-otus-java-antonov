package ru.otus.homework;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String firstThreadName = "Thread-1";
        String secondThreadName = "Thread-2";
        Task task = new Task(secondThreadName);

        Thread t1 = new Thread(task::countedToTenAndBack);
        t1.setName(firstThreadName);

        Thread t2 = new Thread(task::countedToTenAndBack);
        t2.setName(secondThreadName);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("completed");

    }
}
package ru.otus.homework;

public class Task {

    private static final int TOP_LIMIT = 10;
    private String currentThread;
    private String secondThread;
    private int adder = 1;
    private int currentIdx = 1;


    public Task(String secondThreadName) {
        this.secondThread = secondThreadName;
        this.currentThread = secondThreadName;
    }

    public synchronized void countedToTenAndBack() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (currentThread.equals(Thread.currentThread().getName())) {
                    wait();
                }
                if (currentIdx == 0) {
                    currentThread = Thread.currentThread().getName();
                    notifyAll();
                    Thread.currentThread().interrupt();
                    break;
                }

                System.out.println(Thread.currentThread().getName() + ": " + currentIdx);

                if (secondThread.equals(Thread.currentThread().getName())) {
                    currentIdx = currentIdx + adder;
                }

                if (currentIdx == TOP_LIMIT) {
                    adder = -1;
                }

                currentThread = Thread.currentThread().getName();
                Thread.sleep(500);
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}

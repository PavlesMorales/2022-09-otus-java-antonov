package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class EvenSecondExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        long second = dateTimeProvider.getSecond();
        if (second % 2 == 0) {
            throw new EvenSecondException("even seconds: " + second);
        } else {
            System.out.println("Odd seconds: " + second);
        }
        return message;
    }
}

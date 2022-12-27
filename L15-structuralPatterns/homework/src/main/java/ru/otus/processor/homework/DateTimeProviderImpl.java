package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public long getSecond() {
        return LocalDateTime.now().getSecond();
    }
}

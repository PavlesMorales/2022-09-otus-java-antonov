package ru.otus.exception;

public class ReflectionException extends RuntimeException {

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message) {
        super(message);
    }
}

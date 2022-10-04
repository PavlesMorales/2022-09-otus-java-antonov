package ru.otus;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloOtus {
    private static final Logger logger = LoggerFactory.getLogger(HelloOtus.class);

    public static void main(String[] args) {

        List<Integer> integerList = IntStream
                .range(0, 100)
                .boxed()
                .collect(Collectors.toList());

        logger.info("reverse list - {}", Lists.reverse(integerList));
    }
}

package ru.otus.homework.runners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.DisplayName;
import ru.otus.homework.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void run(Class<?> clazz) {

        int testsCount;
        int successfullyTestsCount = 0;
        int failureTestsCount = 0;

        logger.info("Run tests in class: [{}]", clazz.getSimpleName());

        Method[] declaredMethods = clazz.getDeclaredMethods();

        var beforeMethods = new ArrayList<Method>();
        var testMethods = new ArrayList<Method>();
        var afterMethods = new ArrayList<Method>();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Before.class)) beforeMethods.add(method);
            else if (method.isAnnotationPresent(Test.class)) testMethods.add(method);
            else if (method.isAnnotationPresent(After.class)) afterMethods.add(method);
        }

        testsCount = testMethods.size();

        for (Method method : testMethods) {
            try {
                Object instance = clazz.getConstructor().newInstance();

                invokeAll(instance, beforeMethods);

                if (invokeMethod(instance, method)) {
                    ++successfullyTestsCount;
                } else {
                    ++failureTestsCount;
                }

                invokeAll(instance, afterMethods);
            } catch (Exception e) {
                logger.error("Error new instance class. Message - [{}]", e.getMessage(), e);
            }
        }

        logger.info("Results: tests - [{}], success - [{}], failure - [{}]", testsCount, successfullyTestsCount, failureTestsCount);
    }

    private static void invokeAll(Object testClass, List<Method> methodList) {
        methodList.forEach(method -> invokeMethod(testClass, method));
    }

    private static boolean invokeMethod(Object testClass, Method method) {
        String testName = method.isAnnotationPresent(DisplayName.class) ?
                method.getAnnotation(DisplayName.class).name() : method.getName();

        logger.info("Run test: [{}]", testName);

        try {
            method.setAccessible(true);
            method.invoke(testClass);
            logger.info("Test passed success: [{}]", testName);
            return true;
        } catch (Exception e) {
            logger.error("Test failure: [{}]. Message - [{}]", testName, e.getMessage(), e);
            return false;
        }
    }
}

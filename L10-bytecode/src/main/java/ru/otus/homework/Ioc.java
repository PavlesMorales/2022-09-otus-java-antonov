package ru.otus.homework;

import ru.otus.homework.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {

    public static TestLoggingInterface createProxy(TestLoggingInterface instance) {
        DemoInvocationHandler handler = new DemoInvocationHandler(instance);
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }


    private static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLogging;
        private final Set<String> methods;

        DemoInvocationHandler(TestLoggingInterface testLogging) {
            this.testLogging = testLogging;
            this.methods = getMethodsWithLogAnnotation(testLogging);
        }

        private Set<String> getMethodsWithLogAnnotation(TestLoggingInterface testLogging) {
            var methodsWithAnnotation = new HashSet<String>();
            Class<? extends TestLoggingInterface> testLoggingClass = testLogging.getClass();
            Method[] testLoggingMethods = testLoggingClass.getMethods();
            for (Method method : testLoggingMethods) {
                if (method.isAnnotationPresent(Log.class)) {
                    methodsWithAnnotation.add(getKey(method));
                }
            }
            return methodsWithAnnotation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methods.contains(getKey(method))) {
                printLog(method, args);
            }
            return method.invoke(testLogging, args);
        }

        private String getKey(Method method) {
            return method.getName() + Arrays.toString(method.getParameters());
        }

        private void printLog(Method method, Object[] args) {
            String params;
            if (args != null) {
                params = Arrays.stream(args)
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
            } else {
                params = "method without params";
            }
            System.out.printf("executed method: %s. param: %s\n", method.getName(), params);
        }

    }
}

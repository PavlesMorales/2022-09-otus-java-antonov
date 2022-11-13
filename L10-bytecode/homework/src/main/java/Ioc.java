import annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Ioc {

    public static TestLoggingInterface createProxy() {
        DemoInvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }


    private static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;
        private final Map<String, Method> methods;

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
            this.methods = getMethodsWithLogAnnotation(testLogging);
        }

        private Map<String, Method> getMethodsWithLogAnnotation(TestLoggingInterface testLogging) {
            var methodsWithAnnotation = new HashMap<String, Method>();
            Class<? extends TestLoggingInterface> testLoggingClass = testLogging.getClass();
            Method[] testLoggingMethods = testLoggingClass.getMethods();
            for (Method method : testLoggingMethods) {
                if (method.isAnnotationPresent(Log.class)) {
                    methodsWithAnnotation.put(getKey(method), method);
                }
            }
            return methodsWithAnnotation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methods.containsKey(getKey(method))) {
                printLog(method, args);
            }
            return method.invoke(testLogging, args);
        }

        private String getKey(Method method) {
            return method.getName() + Arrays.toString(method.getParameters());
        }

        private void printLog(Method method, Object[] args) {
            String params = Arrays.stream(args)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            System.out.printf("executed method: %s. param: %s\n", method.getName(), params);
        }

    }
}

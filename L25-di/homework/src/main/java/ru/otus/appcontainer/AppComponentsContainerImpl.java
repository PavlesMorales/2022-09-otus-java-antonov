package ru.otus.appcontainer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exception.ComponentException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger log = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);
        Map<String, List<Method>> sortedOrderMethods = getSortedMethods(configClass.getMethods());

        var configInstance = configClass.getDeclaredConstructor().newInstance();
        for (Map.Entry<String, List<Method>> stringListEntry : sortedOrderMethods.entrySet()) {
            for (Method method : stringListEntry.getValue()) {
                String name = getBeanName(method);

                var parameterCount = method.getParameterCount();
                if (parameterCount == 0) {
                    var invoke = method.invoke(configInstance);
                    appComponentsByName.put(name, invoke);
                    appComponents.add(invoke);

                } else {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int i = 0; i < parameterTypes.length; i++) {
                        Class<?> currentParamType = parameterTypes[i];

                        parameters[i] = appComponents.stream()
                                .filter(component -> currentParamType.isAssignableFrom(component.getClass()))
                                .findFirst()
                                .orElseThrow(() -> new ComponentException(String.format("%s not found in context", currentParamType)));
                    }

                    var invoke = method.invoke(configInstance, parameters);
                    appComponentsByName.put(name, invoke);
                    appComponents.add(invoke);
                }

            }
        }
    }

    private String getBeanName(Method method) {
        var annotation = method.getAnnotation(AppComponent.class);
        var componentName = annotation.name();

        if (appComponentsByName.containsKey(componentName)) {
            throw new ComponentException(String.format("%s - component already exist", componentName));
        }

        return componentName;
    }

    private Map<String, List<Method>> getSortedMethods(Method[] methods) {
        Map<String, List<Method>> sortedOrderMethods = new TreeMap<>();
        for (Method method : methods) {
            var annotation = method.getAnnotation(AppComponent.class);
            if (annotation != null) {
                var order = annotation.order();

                sortedOrderMethods
                        .compute(String.valueOf(order), (k, v) -> {
                            if (v == null) {
                                v = new ArrayList<>();
                                v.add(method);
                            } else {
                                v.add(method);
                            }
                            return v;
                        });
            }
        }
        return sortedOrderMethods;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> objects = appComponents.stream().filter(component -> componentClass.isAssignableFrom(component.getClass())).toList();
        if (objects.isEmpty()) {
            log.error("[{}] component not found in context", componentClass.getName());
            throw new ComponentException(String.format("%s component not found in context", componentClass));
        } else if (objects.size() > 1) {
            objects.forEach(component -> log.error("[{}] component found", component));
            throw new ComponentException("contains more than one components");
        } else {
            return (C) objects.get(0);
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component != null) {
            return (C) component;
        } else {
            throw new ComponentException(String.format("%s component not found in context", componentName));
        }
    }
}

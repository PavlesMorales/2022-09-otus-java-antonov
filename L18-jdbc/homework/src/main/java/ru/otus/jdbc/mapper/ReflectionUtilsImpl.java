package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exception.ReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtilsImpl<T> implements ReflectionUtils<T> {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtilsImpl.class);
    private final EntityClassMetaData<T> entityClassMetaData;

    public ReflectionUtilsImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public T createObject(ResultSet resultSet) {
        List<T> allObjects = getAllObjects(resultSet);
        return allObjects.isEmpty() ? null : allObjects.get(0);
    }

    @Override
    public List<T> getAllObjects(ResultSet resultSet) {
        var listObjects = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                Constructor<T> constructor = entityClassMetaData.getConstructor();
                T obj = constructor.newInstance();
                for (Field field : entityClassMetaData.getAllFields()) {
                    field.setAccessible(true);
                    field.set(obj, resultSet.getObject(field.getName()));
                }
                listObjects.add(obj);
            }
        } catch (Exception e) {
            logger.error("Error when create object. Ex: ", e);
            throw new ReflectionException("Error when create object", e);
        }
        return listObjects;
    }

    @Override
    public List<Object> getValuesWithoutId(T type) {
        List<Object> values = new ArrayList<>();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fieldsWithoutId) {
            values.add(getValue(field, type));
        }
        return values;
    }

    @Override
    public List<Object> getAllValues(T type) {
        List<Object> values = new ArrayList<>();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fieldsWithoutId) {
            values.add(getValue(field, type));
        }
        values.add(getValue(entityClassMetaData.getIdField(), type));
        return values;
    }

    private Object getValue(Field field, T type) {
        field.setAccessible(true);
        try {
            return field.get(type);
        } catch (IllegalAccessException e) {
            logger.error("Error when get value in field: [{}]. Ex: ", field.getName(), e);
            throw new ReflectionException("Error when get value in field", e);
        }
    }
}

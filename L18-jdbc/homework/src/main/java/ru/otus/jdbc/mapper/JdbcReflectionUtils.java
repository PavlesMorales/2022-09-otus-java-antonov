package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.util.List;

public interface JdbcReflectionUtils<T> {
    T createObject(ResultSet resultSet);

    List<T> getAllObjects(ResultSet resultSet);

    List<Object> getValuesWithoutId(T type);

    List<Object> getAllValues(T type);
}

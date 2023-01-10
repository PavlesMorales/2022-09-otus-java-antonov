package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_TEMPLATE = "UPDATE %s SET %s WHERE %s = ?";
    private static final String SELECT_BY_ID_TEMPLATE = "SELECT * FROM %s WHERE %s = ?";
    private static final String SELECT_ALL_FIELDS_TEMPLATE = "SELECT %s FROM %s";
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        var fieldsDelimiter = new StringJoiner(", ");
        List<Field> fieldsWithoutId = entityClassMetaData.getAllFields();
        for (Field field : fieldsWithoutId) {
            fieldsDelimiter.add(field.getName());
        }
        return String.format(
                SELECT_ALL_FIELDS_TEMPLATE,
                fieldsDelimiter,
                entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {


        return String.format(
                SELECT_BY_ID_TEMPLATE,
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        var fieldsDelimiter = new StringJoiner(", ");
        var valuesDelimiter = new StringJoiner(", ");
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();

        for (Field field : fieldsWithoutId) {
            fieldsDelimiter.add(field.getName());
            valuesDelimiter.add("?");
        }

        return String.format(INSERT_TEMPLATE,
                entityClassMetaData.getName(),
                fieldsDelimiter,
                valuesDelimiter);
    }

    @Override
    public String getUpdateSql() {
        var fieldsDelimiter = new StringJoiner(", ");
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fieldsWithoutId) {
            fieldsDelimiter.add(field.getName() + "=?");
        }

        return String.format(UPDATE_TEMPLATE,
                entityClassMetaData.getName(),
                fieldsDelimiter,
                entityClassMetaData.getIdField().getName());

    }
}

package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private String selectAllSql;
    private String selectById;
    private String insertSql;
    private String updateSql;
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        if (selectAllSql == null) {
            var fieldsDelimiter = new StringJoiner(", ");
            List<Field> fieldsWithoutId = entityClassMetaData.getAllFields();

            for (Field field : fieldsWithoutId) {
                fieldsDelimiter.add(field.getName());
            }

            selectAllSql = String.format(
                    "SELECT %s FROM %s",
                    fieldsDelimiter,
                    entityClassMetaData.getName());
        }
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        if (selectById == null) {
            selectById = String.format(
                    "SELECT * FROM %s WHERE %s = ?",
                    entityClassMetaData.getName(),
                    entityClassMetaData.getIdField().getName());
        }
        return selectById;
    }

    @Override
    public String getInsertSql() {
        if (insertSql == null) {
            var fieldsDelimiter = new StringJoiner(", ");
            var valuesDelimiter = new StringJoiner(", ");
            List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();

            for (Field field : fieldsWithoutId) {
                fieldsDelimiter.add(field.getName());
                valuesDelimiter.add("?");
            }

            insertSql = String.format(
                    "INSERT INTO %s (%s) VALUES (%s)",
                    entityClassMetaData.getName(),
                    fieldsDelimiter,
                    valuesDelimiter);
        }
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        if (updateSql == null) {
            var fieldsDelimiter = new StringJoiner(", ");
            List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();

            for (Field field : fieldsWithoutId) {
                fieldsDelimiter.add(field.getName() + "=?");
            }

            updateSql = String.format(
                    "UPDATE %s SET %s WHERE %s = ?",
                    entityClassMetaData.getName(),
                    fieldsDelimiter,
                    entityClassMetaData.getIdField().getName());
        }
        return updateSql;
    }
}

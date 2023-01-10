package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.annotation.Id;
import ru.otus.exception.ReflectionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);
    private final Class<T> clazz;
    private List<Field> fields;
    private List<Field> fieldsWithoutId;
    private Constructor<T> constructor;
    private Field id;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        if (constructor == null) {
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                log.error("NoSuchMethodException, ex: ", e);
                throw new ReflectionException("Can't get default constructor", e);
            }
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        return getId();
    }

    @Override
    public List<Field> getAllFields() {
        return getFields();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = getFields()
                    .stream()
                    .filter(field -> !field.equals(getId()))
                    .toList();
        }
        return fieldsWithoutId;
    }

    private List<Field> getFields() {
        if (fields == null) {
            fields = List.of(clazz.getDeclaredFields());
        }
        return fields;
    }

    private Field getId() {
        if (id == null) {
            id = Stream.of(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElseThrow(()-> new ReflectionException("Field with annotation @Id not present in class: " + clazz.getName()));
        }
        return id;
    }
}

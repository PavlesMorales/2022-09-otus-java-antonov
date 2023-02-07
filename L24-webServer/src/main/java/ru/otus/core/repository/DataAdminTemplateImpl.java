package ru.otus.core.repository;

import org.hibernate.Session;

import java.util.List;

public class DataAdminTemplateImpl<T> implements DataAdminTemplate<T> {

    private final Class<T> clazz;

    public DataAdminTemplateImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> findByLoginAndPassword(Session session, String login, String password) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz).getResultList();
    }
}

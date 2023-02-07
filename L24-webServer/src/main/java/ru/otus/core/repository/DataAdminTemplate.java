package ru.otus.core.repository;

import org.hibernate.Session;

import java.util.List;

public interface DataAdminTemplate<T> {

    List<T> findByLoginAndPassword(Session session, String login, String password);
}

package ru.otus.services;


import ru.otus.core.repository.DataAdminTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.model.Admin;

public class AdminAuthServiceImpl implements AdminAuthService {

    private final DataAdminTemplate<Admin> adminDataTemplate;
    private final TransactionManager transactionManager;

    public AdminAuthServiceImpl(TransactionManager transactionManager, DataAdminTemplate<Admin> adminDataTemplate) {
        this.transactionManager = transactionManager;
        this.adminDataTemplate = adminDataTemplate;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var adminList = adminDataTemplate.findByLoginAndPassword(session, login, password);
            for (Admin admin : adminList) {
                return login.equals(admin.getLogin()) && password.equals(admin.getPassword());
            }
            return false;
        });
    }

}

package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataAdminTemplate;
import ru.otus.core.repository.DataAdminTemplateImpl;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.model.Address;
import ru.otus.model.Admin;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.*;

@Slf4j
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/web/templates/";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure("db/hibernate.cfg.xml");

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,Admin.class, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        DataAdminTemplate<Admin> adminDataTemplate = new DataAdminTemplateImpl<>(Admin.class);
        AdminAuthService authService = new AdminAuthServiceImpl(transactionManager, adminDataTemplate);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, dbServiceClient, templateProcessor, authService);

        usersWebServer.start();
        usersWebServer.join();
    }
}

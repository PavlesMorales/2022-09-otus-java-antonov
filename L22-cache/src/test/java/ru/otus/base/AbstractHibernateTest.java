package ru.otus.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.jpqlhw.core.repository.DataTemplateHibernate;
import ru.otus.jpqlhw.core.repository.HibernateUtils;
import ru.otus.jpqlhw.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.jpqlhw.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.jpqlhw.crm.model.Address;
import ru.otus.jpqlhw.crm.model.Client;
import ru.otus.jpqlhw.crm.model.Phone;
import ru.otus.jpqlhw.crm.service.DBServiceClient;
import ru.otus.jpqlhw.crm.service.DBServiceClientCacheImpl;
import ru.otus.jpqlhw.crm.service.DbServiceClientImpl;


public abstract class AbstractHibernateTest {
    protected SessionFactory sessionFactory;
    protected TransactionManagerHibernate transactionManager;
    protected DataTemplateHibernate<Client> clientTemplate;
    protected DBServiceClient dbServiceClient;
    protected DBServiceClient dbServiceClientCached;

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientTemplate = new DataTemplateHibernate<>(Client.class);
        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        MyCache<String, Client> cache = new MyCache<>();
        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                System.out.printf("key: {%s}, value: {%s}, action: {%s} \n", key, value, action);
            }
        };

        cache.addListener(listener);
        dbServiceClientCached = new DBServiceClientCacheImpl(transactionManager, clientTemplate, cache);

    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(Client.class.getName());
    }
}

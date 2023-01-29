package ru.otus.homework;

import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.jpqlhw.crm.model.Address;
import ru.otus.jpqlhw.crm.model.Client;
import ru.otus.jpqlhw.crm.model.Phone;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    void shouldCorrectSaveClient() {

        Client client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333")));

        var savedClient = dbServiceClientCached.saveClient(client);

        long startDB = System.currentTimeMillis();
        var clientFromDB = dbServiceClient.getClient(savedClient.getId());
        long endDB = System.currentTimeMillis();
        long timeToDb = endDB - startDB;

        long startCache = System.currentTimeMillis();
        var clientFromCache = dbServiceClientCached.getClient(savedClient.getId());
        long endCache = System.currentTimeMillis();

        long timeToCache = endCache - startCache;
        System.out.println("time from db: " + timeToDb);
        System.out.println("time from cache: " + timeToCache);

        assertTrue(timeToDb > timeToCache);
        assertThat(clientFromDB)
                .usingRecursiveComparison().isEqualTo(clientFromCache);
    }
}
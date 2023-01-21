package ru.otus.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.jpqlhw.crm.model.Address;
import ru.otus.jpqlhw.crm.model.Client;
import ru.otus.jpqlhw.crm.model.Phone;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    void shouldCorrectSaveClient() {

        MyCache<Long, Client> cache = new MyCache<>();
        HwListener<Long, Client> listener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                System.out.printf("key: {%s}, value: {%s}, action: {%s} \n", key, value, action);
            }
        };

        cache.addListener(listener);


        Client client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"),
                new Phone(null, "14-666-333")));


        var savedClient = dbServiceClient.saveClient(client);
        Client cloneClient = savedClient.clone();
        cache.put(cloneClient.getId(), cloneClient);

        long startDB = System.currentTimeMillis();
        var clientFromDB = dbServiceClient.getClient(savedClient.getId());
        long endDB = System.currentTimeMillis();
        long timeToDb = endDB - startDB;

        long startCache = System.currentTimeMillis();
        Client clientFromCache = cache.get(cloneClient.getId());
        long endCache = System.currentTimeMillis();

        long timeToCache = endCache - startCache;
        System.out.println("time from db: " + timeToDb);
        System.out.println("time from cache: " + timeToCache);

        assertTrue(timeToDb > timeToCache);
        assertThat(clientFromDB.orElse(null))
                .usingRecursiveComparison().isEqualTo(clientFromCache);
    }
}
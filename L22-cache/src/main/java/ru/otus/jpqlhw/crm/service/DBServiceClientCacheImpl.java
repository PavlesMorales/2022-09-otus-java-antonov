package ru.otus.jpqlhw.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.jpqlhw.core.repository.DataTemplate;
import ru.otus.jpqlhw.core.sessionmanager.TransactionManager;
import ru.otus.jpqlhw.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DBServiceClientCacheImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DBServiceClientCacheImpl.class);
    private final HwCache<String, Client> cache;
    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DBServiceClientCacheImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, HwCache<String, Client> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clonedClient = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clonedClient);
                log.info("created client: {}", clonedClient);
            } else {
                clientDataTemplate.update(session, clonedClient);
                log.info("updated client: {}", clonedClient);
            }
            cache.put(String.valueOf(clonedClient.getId()), clonedClient.clone());
            return clonedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var cachedClient = cache.get(String.valueOf(id));
        if (cachedClient != null) {
            return Optional.of(cachedClient.clone());
        } else {
            return transactionManager.doInReadOnlyTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                log.info("client: {}", clientOptional);
                clientOptional.ifPresent(client -> cache.put(String.valueOf(id), client.clone()));
                return clientOptional;
            });
        }
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            clientList.forEach(client -> cache.put(String.valueOf(client.getId()), client.clone()));
            return clientList;
        });
    }
}

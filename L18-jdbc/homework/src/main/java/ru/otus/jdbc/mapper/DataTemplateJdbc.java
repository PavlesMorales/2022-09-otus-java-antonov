package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private static final Logger logger = LoggerFactory.getLogger(DataTemplateJdbc.class);
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final JdbcReflectionUtils<T> jdbcReflectionUtils;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, JdbcReflectionUtils<T> jdbcReflectionUtils) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.jdbcReflectionUtils = jdbcReflectionUtils;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String query = entitySQLMetaData.getSelectByIdSql();
        logger.debug("SQL: [{}]", query);

        return dbExecutor.executeSelect(connection,
                query,
                List.of(id),
                jdbcReflectionUtils::createObject);
    }

    @Override
    public List<T> findAll(Connection connection) {

        String query = entitySQLMetaData.getSelectAllSql();
        logger.debug("SQL: [{}]", query);

        return dbExecutor.executeSelect(
                        connection,
                        query,
                        Collections.emptyList(),
                        jdbcReflectionUtils::getAllObjects)
                .orElse(new ArrayList<>());

    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> values = jdbcReflectionUtils.getValuesWithoutId(client);
        String query = entitySQLMetaData.getInsertSql();
        logger.debug("SQL: [{}]", query);

        return dbExecutor.executeStatement(
                connection,
                query,
                values);
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> values = jdbcReflectionUtils.getAllValues(client);
        String query = entitySQLMetaData.getUpdateSql();
        logger.debug("SQL: [{}]", query);

        dbExecutor.executeStatement(
                connection,
                query,
                values);
    }
}

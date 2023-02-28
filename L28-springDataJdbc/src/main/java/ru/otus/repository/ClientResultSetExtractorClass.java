package ru.otus.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clients = new ArrayList<Client>();
        Long prevClientId = null;
        Client prevClient = null;
        while (rs.next()) {
            Long clientId = rs.getLong("client_id");
            Client client = null;
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                var clientAddress = Address.builder()
                        .clientId(clientId)
                        .clientAddress(rs.getString("client_address"))
                        .build();

                prevClient = client = Client.builder()
                        .id(clientId)
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .address(clientAddress)
                        .phones(new HashSet<>()).build();

                clients.add(client);
                prevClientId = clientId;
            }
            client = client != null ? client : prevClient;

            if (client != null) {
                Phone phone = Phone.builder()
                        .id(rs.getLong("phone_id"))
                        .clientId(clientId)
                        .number(rs.getString("number"))
                        .build();

                client.phones().add(phone);
            }
        }
        return clients;
    }
}

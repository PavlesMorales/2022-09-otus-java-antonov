package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.controller.dto.ClientDto;
import ru.otus.controller.dto.PhoneDto;
import ru.otus.converter.ClientDtoConverterImpl;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientRepository;
import ru.otus.exception.ClientNotFoundException;
import ru.otus.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;
    private final ClientDtoConverterImpl converter;

    @Override
    public ClientDto updateClient(long id, ClientDto clientDto) {

        var client = converter.convertDtoToClient(id, clientDto);
        var clientFromBd = transactionManager.doInTransaction(() -> clientRepository.save(client));
        return converter.convertClientToDto(clientFromBd);

    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        var client = converter.convertDtoToClient(clientDto);

        var clientFromDb = transactionManager
                .doInTransaction(() -> clientRepository.save(client));

        return converter.convertClientToDto(clientFromDb);

    }

    @Override
    public ClientDto getClientById(long id) {
        Client client = transactionManager
                .doInReadOnlyTransaction(() -> clientRepository.findById(id))
                .orElseThrow(ClientNotFoundException::new);

        return converter.convertClientToDto(client);
    }

    @Override
    public List<ClientDto> findAll() {
        List<Client> clients = transactionManager.
                doInReadOnlyTransaction(clientRepository::findAllByCustomQuery);

        return clients.stream()
                .map(converter::convertClientToDto)
                .toList();
    }

    @Override
    public ClientDto addPhone(long id, PhoneDto phoneDto) {
        var phone = Phone.builder().number(phoneDto.number()).clientId(id).build();

        Client client = transactionManager.doInTransaction(() -> {
            var clientFromDb = clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
            Set<Phone> phones = clientFromDb.phones();
            phones.add(phone);
            return clientRepository.save(clientFromDb);
        });

        return converter.convertClientToDto(client);
    }

    @Override
    public Long deleteClient(long id) {
        return transactionManager.doInTransaction(() -> {
            clientRepository.deleteById(id);
            return id;
        });
    }
}

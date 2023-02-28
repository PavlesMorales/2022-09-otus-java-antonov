package ru.otus.converter;

import ru.otus.controller.dto.ClientDto;
import ru.otus.model.Client;

public interface ClientDtoConverter {

    ClientDto convertClientToDto(Client client);

    Client convertDtoToClient(Long id, ClientDto clientDto);

    Client convertDtoToClient(ClientDto clientDto);
}

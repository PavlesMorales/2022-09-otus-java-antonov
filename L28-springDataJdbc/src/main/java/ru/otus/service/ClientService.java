package ru.otus.service;

import ru.otus.controller.dto.ClientDto;
import ru.otus.controller.dto.PhoneDto;

import java.util.List;

public interface ClientService {

    ClientDto createClient(ClientDto clientDto);

    ClientDto getClientById(long id);

    Long deleteClient(long id);

    ClientDto updateClient(long id, ClientDto clientDto);

    List<ClientDto> findAll();

    ClientDto addPhone(long id, PhoneDto phoneDto);
}

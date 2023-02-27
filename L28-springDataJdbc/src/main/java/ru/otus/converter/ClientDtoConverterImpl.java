package ru.otus.converter;

import org.springframework.stereotype.Component;
import ru.otus.controller.dto.ClientDto;
import ru.otus.controller.dto.PhoneDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.stream.Collectors;

@Component
public class ClientDtoConverterImpl implements ClientDtoConverter {

    @Override
    public ClientDto convertClientToDto(Client client) {

        return ClientDto.builder()
                .id(client.id())
                .firstName(client.firstName())
                .lastName(client.lastName())
                .address(client.address().clientAddress())
                .phones(client.phones().stream()
                        .map(phone -> PhoneDto.builder()
                                .id(phone.id())
                                .number(phone.number())
                                .build())
                        .toList()).build();
    }

    @Override
    public Client convertDtoToClient(Long id, ClientDto clientDto) {
        var phones = clientDto.phones().stream()
                .map(number -> Phone.builder().number(number.number()).build())
                .collect(Collectors.toSet());

        var address = Address.builder().clientAddress(clientDto.address()).build();

        return Client.builder()
                .id(id)
                .firstName(clientDto.firstName())
                .lastName(clientDto.lastName())
                .address(address)
                .phones(phones)
                .build();
    }

    @Override
    public Client convertDtoToClient(ClientDto clientDto) {
        return convertDtoToClient(null, clientDto);
    }
}

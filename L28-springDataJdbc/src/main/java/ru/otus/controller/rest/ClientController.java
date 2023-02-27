package ru.otus.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.dto.ClientDto;
import ru.otus.controller.dto.PhoneDto;
import ru.otus.service.ClientService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {

    public final ClientService service;

    @GetMapping("/api/client/")
    public List<ClientDto> getAllClients() {
        return service.findAll();
    }

    @GetMapping("/api/client/{id}")
    public ClientDto getClient(@PathVariable(name = "id") long id) {
        return service.getClientById(id);
    }

    @PostMapping("/api/client/")
    public ClientDto createClient(@RequestBody ClientDto client) {
        return service.createClient(client);
    }
    @PutMapping("/api/client/{id}")
    public ClientDto updatePhoneClient(@PathVariable(name = "id") long id, @RequestBody PhoneDto phoneDto) {
        return service.addPhone(id, phoneDto);
    }

    @PatchMapping("/api/client/{id}")
    public ClientDto updateClient(@PathVariable(name = "id") long id, @RequestBody ClientDto clientDto) {
        return service.updateClient(id, clientDto);
    }

    @DeleteMapping("/api/client/{id}")
    public Long deleteClient(@PathVariable(name = "id") long id) {
       return service.deleteClient(id);
    }
}

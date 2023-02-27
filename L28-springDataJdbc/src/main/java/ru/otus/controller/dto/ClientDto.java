package ru.otus.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ClientDto(Long id, String firstName, String lastName, String address, List<PhoneDto> phones) {
}

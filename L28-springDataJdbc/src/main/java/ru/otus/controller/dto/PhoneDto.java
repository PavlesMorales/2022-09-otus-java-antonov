package ru.otus.controller.dto;

import lombok.Builder;

@Builder
public record PhoneDto(Long id, String number) {
}

package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("phone")
public record Phone(
        @Id Long id,
        @Nonnull String number,
        Long clientId) {
}

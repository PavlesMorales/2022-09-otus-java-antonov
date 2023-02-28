package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("address")
public record Address(
        @Id Long clientId,
        @Nonnull String clientAddress) {
}

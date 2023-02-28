package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;


@Builder
@Table("client")
public record Client(@Id Long id,
                     @Nonnull String firstName,
                     @Nonnull String lastName,
                     @MappedCollection(idColumn = "client_id") Address address,
                     @MappedCollection(idColumn = "client_id") Set<Phone> phones) {

}

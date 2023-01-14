package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "address_id", nullable = false)
    private Long id;

    @Column(name = "client_address")
    private String clientAddress;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + clientAddress + '\'' +
                '}';
    }
}

package ru.otus.homework.model;

import java.util.Objects;

public class Banknote {

    private final Denomination denomination;
    private final String serialNumber;

    public Banknote(Denomination denomination, String serialNumber) {
        Objects.requireNonNull(denomination, "Denomination must not be null");
        Objects.requireNonNull(serialNumber, "SerialNumber must not be null");
        this.denomination = denomination;
        this.serialNumber = serialNumber;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "Banknote{" + denomination + " №: " + serialNumber +'}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return denomination == banknote.denomination && Objects.equals(serialNumber, banknote.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination, serialNumber);
    }
}

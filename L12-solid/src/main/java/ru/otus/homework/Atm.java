package ru.otus.homework;

import ru.otus.homework.model.Banknote;

import java.util.List;

public interface Atm {

    List<Banknote> withdraw(int amount);

    void downloadBanknotes(List<Banknote> banknotes);

    int getTotalBalance();
}

package ru.otus.homework;

import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.List;
import java.util.Map;

public interface BanknotesStorage {

    void download(Map<Denomination, List<Banknote>> banknotes);

    List<Banknote> withdraw(Map<Denomination, Integer> requiredCountBanknotes);

    int getBalance();

    String getCurrentCountBanknotesAsString();

    List<Banknote> getBanknotes(Denomination denomination);
}

package ru.otus.homework;

import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BanknotesStorageImpl implements BanknotesStorage {

    private final Map<Denomination, List<Banknote>> cellsOfBanknotes = new EnumMap<>(Denomination.class);

    @Override
    public void download(Map<Denomination, List<Banknote>> banknotes) {
        cellsOfBanknotes.putAll(banknotes);
    }

    @Override
    public List<Banknote> withdraw(Map<Denomination, Integer> requiredCountBanknotes) {
        var banknotes = new ArrayList<Banknote>();
        for (Map.Entry<Denomination, Integer> denomination : requiredCountBanknotes.entrySet()) {
            List<Banknote> banknotesInAtm = cellsOfBanknotes.get(denomination.getKey());
            List<Banknote> tempBanknotes = banknotesInAtm.subList(banknotesInAtm.size() - denomination.getValue(), banknotesInAtm.size());
            banknotes.addAll(tempBanknotes);
            tempBanknotes.clear();
        }
        return banknotes;
    }

    @Override
    public int getBalance() {

        return cellsOfBanknotes
                .values()
                .stream()
                .flatMap(List::stream)
                .map(banknote -> banknote.getDenomination().getValue())
                .reduce(0, Integer::sum);

    }

    @Override
    public String getCurrentCountBanknotesAsString() {

        return cellsOfBanknotes
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors
                        .groupingBy(Banknote::getDenomination, Collectors.summingInt(b -> 1)))
                .toString();
    }

    @Override
    public List<Banknote> getBanknotes(Denomination denomination) {
        List<Banknote> banknotes = cellsOfBanknotes.get(denomination);
        if (banknotes == null) {
            return new ArrayList<>();
        }
        return banknotes;
    }
}

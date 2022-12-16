package ru.otus.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BanknotesStorageImplTest {

    @InjectMocks
    BanknotesStorageImpl subj;

    @Test
    void getBalance2500() {
        List<Banknote> banknotes1000 = List.of(
                new Banknote(Denomination.RUB1000, "1"),
                new Banknote(Denomination.RUB1000, "2"));

        List<Banknote> banknotes500 = List.of(
                new Banknote(Denomination.RUB500, "3"));

        Map<Denomination, List<Banknote>> banknotes = new EnumMap<>(Denomination.class);

        banknotes.put(Denomination.RUB1000, banknotes1000);
        banknotes.put(Denomination.RUB500, banknotes500);
        subj.download(banknotes);
        int expected = 2500;

        int actual = subj.getBalance();

        assertEquals(expected, actual);
    }

    @Test
    void getBalanceAllDenomination() {
        int expected = 8800;

        subj.download(Map.of(Denomination.RUB100, List.of(new Banknote(Denomination.RUB100, "1"))));
        subj.download(Map.of(Denomination.RUB200, List.of(new Banknote(Denomination.RUB200, "2"))));
        subj.download(Map.of(Denomination.RUB500, List.of(new Banknote(Denomination.RUB500, "3"))));
        subj.download(Map.of(Denomination.RUB1000, List.of(new Banknote(Denomination.RUB1000, "4"))));
        subj.download(Map.of(Denomination.RUB2000, List.of(new Banknote(Denomination.RUB2000, "5"))));
        subj.download(Map.of(Denomination.RUB5000, List.of(new Banknote(Denomination.RUB5000, "6"))));

        int actual = subj.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void getBalanceEmpty() {
        int expected = 0;
        subj.download(Map.of(Denomination.RUB1000, new ArrayList<>()));
        int actual = subj.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void withdraw() {

        var requiredSum = new EnumMap<Denomination, Integer>(Denomination.class);
        requiredSum.put(Denomination.RUB1000, 2);

        var expected = List.of(
                new Banknote(Denomination.RUB1000, "1"),
                new Banknote(Denomination.RUB1000, "2"));

        List<Banknote> banknotes1000 = new ArrayList<>();
        banknotes1000.add(new Banknote(Denomination.RUB1000, "1"));
        banknotes1000.add(new Banknote(Denomination.RUB1000, "2"));

        List<Banknote> banknotes500 = new ArrayList<>();
        banknotes500.add(new Banknote(Denomination.RUB500, "3"));

        subj.download(Map.of(Denomination.RUB1000, banknotes1000));
        subj.download(Map.of(Denomination.RUB500, banknotes500));

        List<Banknote> actual = subj.withdraw(requiredSum);
        assertEquals(expected, actual);
    }

    @Test
    void currentCountBanknotes() {
        List<Banknote> banknotes1000 = List.of(
                new Banknote(Denomination.RUB1000, "1"),
                new Banknote(Denomination.RUB1000, "2"));

        List<Banknote> banknotes500 = List.of(
                new Banknote(Denomination.RUB500, "3"));

        subj.download(Map.of(Denomination.RUB1000, banknotes1000));
        subj.download(Map.of(Denomination.RUB500, banknotes500));

        assertTrue(subj.getCurrentCountBanknotesAsString().contains("RUB1000=2"));
        assertTrue(subj.getCurrentCountBanknotesAsString().contains("RUB500=1"));
    }

    @Test
    void getBanknotes() {
        List<Banknote> expected = List.of(
                new Banknote(Denomination.RUB1000, "1"),
                new Banknote(Denomination.RUB1000, "2"));

        List<Banknote> banknotes1000 = List.of(
                new Banknote(Denomination.RUB1000, "1"),
                new Banknote(Denomination.RUB1000, "2"));

        List<Banknote> banknotes500 = List.of(
                new Banknote(Denomination.RUB500, "3"));

        subj.download(Map.of(Denomination.RUB1000, banknotes1000));
        subj.download(Map.of(Denomination.RUB500, banknotes500));

        assertEquals(expected, subj.getBanknotes(Denomination.RUB1000));
    }
}
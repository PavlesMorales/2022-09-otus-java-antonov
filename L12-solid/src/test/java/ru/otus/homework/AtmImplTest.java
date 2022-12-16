package ru.otus.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.exception.NoAvailableBanknotesToDraftException;
import ru.otus.homework.exception.NoEnoughBanknotesInAtmException;
import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtmImplTest {

    @Mock
    BanknotesStorage banknotesStorage;

    @InjectMocks
    AtmImpl subj;

    @Test
    void withdraw() {
        var expected = List.of(
                new Banknote(Denomination.RUB1000, "4"),
                new Banknote(Denomination.RUB500, "3"));

        var requiredBanknotes = new EnumMap<Denomination, Integer>(Denomination.class);
        requiredBanknotes.put(Denomination.RUB1000, 1);
        requiredBanknotes.put(Denomination.RUB500, 1);

        when(banknotesStorage.getBalance()).thenReturn(2000);
        when(banknotesStorage.getBanknotes(Denomination.RUB5000)).thenReturn(new ArrayList<>());
        when(banknotesStorage.getBanknotes(Denomination.RUB2000)).thenReturn(new ArrayList<>());
        when(banknotesStorage.getBanknotes(Denomination.RUB1000)).thenReturn(List.of(new Banknote(Denomination.RUB1000, "4")));
        when(banknotesStorage.getBanknotes(Denomination.RUB500)).thenReturn(List.of(
                new Banknote(Denomination.RUB500, "5"),
                new Banknote(Denomination.RUB500, "3")));
        when(banknotesStorage.withdraw(requiredBanknotes)).thenReturn(expected);
        when(banknotesStorage.getCurrentCountBanknotesAsString()).thenReturn("{RUB500}");

        List<Banknote> actual = subj.withdraw(1500);

        assertEquals(expected, actual);
        verify(banknotesStorage, times(1)).getBalance();
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB5000);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB2000);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB1000);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB500);
        verify(banknotesStorage, times(1)).withdraw(requiredBanknotes);
        verify(banknotesStorage, times(1)).getCurrentCountBanknotesAsString();

    }

    @Test
    void withdrawNoAvailableBanknotes() {
        when(banknotesStorage.getBalance()).thenReturn(3000);
        when(banknotesStorage.getBanknotes(Denomination.RUB5000)).thenReturn(new ArrayList<>());
        when(banknotesStorage.getBanknotes(Denomination.RUB2000)).thenReturn(List.of(new Banknote(Denomination.RUB2000, "5")));
        when(banknotesStorage.getBanknotes(Denomination.RUB1000)).thenReturn(List.of(new Banknote(Denomination.RUB1000, "4")));
        when(banknotesStorage.getBanknotes(Denomination.RUB500)).thenReturn(new ArrayList<>());
        when(banknotesStorage.getCurrentCountBanknotesAsString()).thenReturn("{RUB1000=1, RUB2000=1}");

        assertThrows(NoAvailableBanknotesToDraftException.class, () -> subj.withdraw(1500));

        verify(banknotesStorage, times(1)).getBalance();
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB5000);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB2000);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB1000);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB500);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB200);
        verify(banknotesStorage, times(1)).getBanknotes(Denomination.RUB100);
        verify(banknotesStorage, times(1)).getCurrentCountBanknotesAsString();

    }

    @Test
    void withdrawNoEnoughBanknotesInAtm() {
        when(banknotesStorage.getBalance()).thenReturn(1000);

        assertThrows(NoEnoughBanknotesInAtmException.class, () -> subj.withdraw(1500));

        verify(banknotesStorage, times(2)).getBalance();

    }

    @Test
    void downloadBanknotes() {
        var requiredSum = Map.of(Denomination.RUB1000, List.of(
                new Banknote(Denomination.RUB1000, "4")));

        doNothing().when(banknotesStorage).download(requiredSum);

        subj.downloadBanknotes(List.of(new Banknote(Denomination.RUB1000, "4")));

        verify(banknotesStorage, times(1)).download(requiredSum);
    }

    @Test
    void getTotalBalance() {

        when(banknotesStorage.getBalance()).thenReturn(5000);

        int totalBalance = subj.getTotalBalance();
        assertEquals(5000, totalBalance);
    }
}
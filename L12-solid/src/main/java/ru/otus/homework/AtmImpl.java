package ru.otus.homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.exception.NoAvailableBanknotesToDraftException;
import ru.otus.homework.exception.NoEnoughBanknotesInAtmException;
import ru.otus.homework.model.Banknote;
import ru.otus.homework.model.Denomination;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmImpl implements Atm {
    private static final int MINIMAL_DENOMINATION_BANKNOTES = 100;
    private static final Logger logger = LoggerFactory.getLogger(AtmImpl.class);

    private final BanknotesStorage banknotesStorage;

    public AtmImpl(BanknotesStorage banknotesStorage) {
        this.banknotesStorage = banknotesStorage;
    }

    @Override
    public List<Banknote> withdraw(int amount) {
        validateRequestedAmount(amount);
        Map<Denomination, Integer> requiredBanknotes = calculateRequiredBanknotes(amount);
        logger.info("Required count of banknotes for withdraw: {}", requiredBanknotes);
        List<Banknote> banknotes = banknotesStorage.withdraw(requiredBanknotes);
        String currentCountBanknotesInAtm = banknotesStorage.getCurrentCountBanknotesAsString();
        logger.info("Remaining banknotes in ATM: {}", currentCountBanknotesInAtm);
        return banknotes;
    }

    @Override
    public void downloadBanknotes(List<Banknote> banknotes) {

        logger.info("Download banknotes in ATM. Banknotes: [{}]", banknotes);
        banknotesStorage
                .download(banknotes
                        .stream()
                        .collect(Collectors.groupingBy(Banknote::getDenomination)));
    }

    @Override
    public int getTotalBalance() {
        return banknotesStorage.getBalance();
    }

    private void validateRequestedAmount(int amount) {
        if (amount % MINIMAL_DENOMINATION_BANKNOTES != 0) {
            logger.error("Minimal available denomination - RUB100. Requested amount: [{}]", amount);
            throw new IllegalArgumentException();
        } else if (amount > getTotalBalance()) {
            logger.error("No enough amount in ATM. Total: [{}]. Requested amount: [{}]", banknotesStorage.getBalance(), amount);
            throw new NoEnoughBanknotesInAtmException();
        }
    }

    private Map<Denomination, Integer> calculateRequiredBanknotes(int requestedAmount) {
        var tempStorage = new EnumMap<Denomination, Integer>(Denomination.class);

        for (int i = Denomination.values().length - 1; i >= 0; i--) {
            if (requestedAmount == 0) break;

            Denomination denomination = Denomination.values()[i];
            List<Banknote> banknotes = banknotesStorage.getBanknotes(denomination);
            int countBanknotes = banknotes.size();
            int tempCountBanknotes = 0;

            while (countBanknotes != 0 && requestedAmount >= denomination.getValue()) {
                countBanknotes--;
                tempCountBanknotes++;
                requestedAmount -= denomination.getValue();
            }

            if (tempCountBanknotes != 0) {
                tempStorage.put(denomination, tempCountBanknotes);
            }
        }

        if (requestedAmount != 0) {
            String s = banknotesStorage.getCurrentCountBanknotesAsString();
            logger.error("No available banknotes in ATM. Total: [{}]. Required: [{}]", s, tempStorage);
            throw new NoAvailableBanknotesToDraftException();
        }

        return tempStorage;
    }
}

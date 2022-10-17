package homework;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final TreeMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = map.firstEntry();
        return newEntry(entry);

    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = map.higherEntry(customer);
        return newEntry(entry);
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

    private Map.Entry<Customer, String> newEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) return null;

        return new Map.Entry<>() {
            @Override
            public Customer getKey() {
                return new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores());
            }

            @Override
            public String getValue() {
                return entry.getValue();
            }

            @Override
            public String setValue(String value) {
                return value;
            }
        };
    }
}

package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.homework.DateTimeProviderImpl;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;
import ru.otus.processor.homework.SwapFieldProcessor;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        var processors = List.of(new SwapFieldProcessor(),
                new EvenSecondExceptionProcessor(new DateTimeProviderImpl()));

        var complexProcessor = new ComplexProcessor(processors, ex -> System.out.println("[ERROR] Even seconds " + ex.getMessage()));
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .build();

        Message result = complexProcessor.handle(message);
        System.out.println("field11: " + result.getField11());
        System.out.println("field12: " + result.getField12());

        complexProcessor.removeListener(historyListener);

    }
}

package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Message;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvenSecondExceptionProcessorTest {

    @Mock
    DateTimeProvider dateTimeProvider;
    @InjectMocks
    EvenSecondExceptionProcessor subj;

    Message message;

    @BeforeEach
    void init() {
        message = new Message.Builder(1)
                .field11("Field11")
                .field12("Field12")
                .build();
    }

    @Test
    @DisplayName("Четное количество секунд")
    void processOk() {
        when(dateTimeProvider.getSecond()).thenReturn(12L);

        assertThrows(EvenSecondException.class, () -> subj.process(message));
    }

    @Test
    @DisplayName("Не четное количество секунд")
    void processThrowEx() {
        when(dateTimeProvider.getSecond()).thenReturn(11L);

        Message expected = subj.process(message);

        assertNotNull(expected);
    }
}
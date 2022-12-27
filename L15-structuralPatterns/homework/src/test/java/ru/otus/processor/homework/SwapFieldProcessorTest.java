package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SwapFieldProcessorTest {

    @InjectMocks
    SwapFieldProcessor subj;

    @Test
    void process() {
        Message message = new Message.Builder(1)
                .field11("Field11")
                .field12("Field12")
                .build();

        Message actual = subj.process(message);
        assertEquals("Field12", actual.getField11());
        assertEquals("Field11", actual.getField12());
    }
}
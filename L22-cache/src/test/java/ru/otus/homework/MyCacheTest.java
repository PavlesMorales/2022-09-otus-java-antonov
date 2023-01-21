package ru.otus.homework;

import org.junit.jupiter.api.Test;
import ru.otus.cachehw.MyCache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyCacheTest {
    @Test
    void clearCache() throws InterruptedException {
        var myCache = new MyCache<String, String>();

        int size = 100000;
        for (int i = 0; i < size; i++) {
            myCache.put("key" + i, "value" + i);
        }

        assertEquals(size, myCache.size());

        System.gc();
        Thread.sleep(500);

        assertTrue(size > myCache.size());
    }
}

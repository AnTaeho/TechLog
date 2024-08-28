package com.example.techlog.aop.querycounter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryCounterTest {

    private QueryCounter queryCounter;

    @BeforeEach
    void setUp() {
        // given
        queryCounter = new QueryCounter();
    }

    @Test
    void increase_shouldIncrementCount() {
        // when
        queryCounter.increase();

        // then
        assertEquals(1, queryCounter.getCount());
    }

    @Test
    void isWarn_shouldReturnFalseWhenCountIs10OrLess() {
        // given
        for (int i = 0; i < 10; i++) {
            queryCounter.increase();
        }

        // when
        boolean warn = queryCounter.isWarn();

        // then
        assertFalse(warn);
    }

    @Test
    void isWarn_shouldReturnTrueWhenCountIsGreaterThan10() {
        // given
        for (int i = 0; i < 11; i++) {
            queryCounter.increase();
        }

        // when
        boolean warn = queryCounter.isWarn();

        // then
        assertTrue(warn);
    }
}

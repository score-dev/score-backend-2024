package com.score.backend;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeTest {

    @Test
    public void testLocalDateTime() {
        LocalDateTime startDateTime = LocalDateTime.of(2024, 5, 3, 23, 59, 0).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 5, 4, 0, 0, 0).truncatedTo(ChronoUnit.DAYS);

        assertThat(ChronoUnit.DAYS.between(startDateTime, endDateTime)).isEqualTo(1);
    }
}

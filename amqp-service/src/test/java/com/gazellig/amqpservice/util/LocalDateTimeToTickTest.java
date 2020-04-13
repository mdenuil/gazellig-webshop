package com.gazellig.amqpservice.util;

import java.time.LocalDateTime;
import java.util.TimeZone;
import com.gazellig.amqpservice.amqp.util.LocalDateTimeToTick;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocalDateTimeToTickTest {

    @BeforeEach
    void init() {
        // Have to set th Timezone manually to ensure that both Jenkins and Locally the test passes
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    }

    @CsvSource({"621355968000000000,1970-01-01T00:00","636482961425298000,2017-12-08T02:15:42.529"})
    @ParameterizedTest
    void toLocalDateTime(long ticks, LocalDateTime expectedLocalDateTime) {
        // Arrange
        // Act
        // Assert
        Assertions.assertThat(LocalDateTimeToTick.toLocalDateTime(ticks)).isEqualTo(expectedLocalDateTime);
    }

    @CsvSource({"621355968000000000,1970-01-01T00:00","636482961425290000,2017-12-08T02:15:42.529"})
    @ParameterizedTest
    void toTicks(long ticks, LocalDateTime expectedLocalDateTime) {
        // Arrange
        // Act
        // Assert
        Assertions.assertThat(LocalDateTimeToTick.toTick(expectedLocalDateTime)).isEqualTo(ticks);
    }

}
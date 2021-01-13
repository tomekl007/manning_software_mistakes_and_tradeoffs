package com.tomekl007.CH07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OrderItemTest {
    private static Stream<Arguments> provideGetFinalReturnsDateArguments() {
        return Stream.of(
            // Simple case: UTC to make the date obvious, and no overflow.
            Arguments.of("2021-01-01T00:00:00Z", "Etc/UTC", "2021-04-01"),
            // America/New_York is UTC-5 in winter, so shipping time is on 2020-12-31.
            Arguments.of("2021-01-01T00:00:00Z", "America/New_York", "2021-03-31"),
            // Day-of-month overflow, example specified in requirements document.
            Arguments.of("2020-11-30T12:00:00Z", "Etc/UTC", "2021-03-01"),
            // Check destination time zone usage: America/New_York moves from
            // UTC-5 to UTC-4 at 2021-03-14 07:00:00Z. First test below ships
            // on 2021-03-13, and the second ships on 2021-03-15 despite being
            // exactly 24 hours apart. 
            Arguments.of("2021-03-14T04:30:00Z", "America/New_York", "2021-06-13"),
            Arguments.of("2021-03-15T04:30:00Z", "America/New_York", "2021-06-15")
        );
    }
    
    @ParameterizedTest
    @MethodSource("provideGetFinalReturnsDateArguments")
    void getFinalReturnsDate(String shippingText, String zoneText, String expectedText) {
        Instant shippingInstant = Instant.parse(shippingText);
        ZoneId zoneId = ZoneId.of(zoneText);
        LocalDate expectedDate = LocalDate.parse(expectedText);
        LocalDate actualDate = OrderItem.getFinalReturnsDate(shippingInstant, zoneId);
        assertEquals(expectedDate, actualDate);
    }
}

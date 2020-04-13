package com.gazellig.amqpservice.amqp.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class to convert between Unix Epoch time and C# Ticks
 */
public final class LocalDateTimeToTick {
    private static final long TICKS_AT_EPOCH = 621_355_968_000_000_000L;
    private static final long TICKS_PER_MILLISECOND = 10_000;


    private LocalDateTimeToTick() {
        // Private constructor to hide implicit one
    }

    public static LocalDateTime toLocalDateTime(long ticks) {
        var msSinceEpoch = (ticks - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(msSinceEpoch), Clock.systemDefaultZone().getZone());
    }

    public static long getTicksNow() {
        return LocalDateTimeToTick.toTick(LocalDateTime.now());
    }

    public static long toTick(LocalDateTime localDateTime) {
        long msSinceEpoch = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() * TICKS_PER_MILLISECOND;
        return msSinceEpoch + TICKS_AT_EPOCH;
    }
}

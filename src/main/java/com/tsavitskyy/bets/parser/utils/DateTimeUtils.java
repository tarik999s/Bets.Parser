package com.tsavitskyy.bets.parser.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");

    public static String formatMillis(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
        return DATE_TIME_FORMATTER.format(zonedDateTime);
    }
}

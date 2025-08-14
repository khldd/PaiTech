package com.example.paitech.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    private DateUtils() {}

    public static String formatIso(LocalDate date) {
        return date == null ? null : date.format(DateTimeFormatter.ISO_DATE);
    }

    public static LocalDate parseIso(String iso) {
        return iso == null ? null : LocalDate.parse(iso, DateTimeFormatter.ISO_DATE);
    }

    public static Instant nowUtc() {
        return Instant.now().atZone(ZoneOffset.UTC).toInstant();
    }
}


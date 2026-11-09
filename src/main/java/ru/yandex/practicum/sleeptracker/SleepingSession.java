package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final LocalDateTime start;
    private final LocalDateTime end;
    private final SleepQuality qualityOfSleep;

    public SleepingSession(String start, String end, String qualityOfSleep) {
        this.start = LocalDateTime.parse(start.trim(), FORMATTER);
        this.end = LocalDateTime.parse(end.trim(), FORMATTER);
        this.qualityOfSleep = SleepQuality.valueOf(qualityOfSleep.trim());
        if (!this.end.isAfter(this.start)) {
            throw new IllegalArgumentException("Пробуждение должно быть позже засыпания");
        }
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQualityOfSleep() {
        return qualityOfSleep;
    }

    public long getDurationMinutes() {
        return Duration.between(start, end).toMinutes();
    }
}

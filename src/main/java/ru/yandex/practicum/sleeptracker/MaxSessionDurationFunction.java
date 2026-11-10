package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class MaxSessionDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long durationMinutes = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);
        return new SleepAnalysisResult<>("Максимальная продолжительность сессии (в минутах)", durationMinutes);
    }
}

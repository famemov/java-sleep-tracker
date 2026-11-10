package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class AverageSessionDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        // Дробная часть среднего отбрасывается намеренно.
        long durationMinutes = (long) sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0);
        return new SleepAnalysisResult<>("Средняя продолжительность сессии (в минутах)", durationMinutes);
    }
}

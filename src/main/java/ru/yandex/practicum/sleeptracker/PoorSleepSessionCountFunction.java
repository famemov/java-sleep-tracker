package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class PoorSleepSessionCountFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна", sessions.stream()
                .filter(session -> session.getQualityOfSleep() == SleepQuality.BAD)
                .count());
    }
}

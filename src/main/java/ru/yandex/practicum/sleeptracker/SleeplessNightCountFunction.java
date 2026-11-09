package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class SleeplessNightCountFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long count = NightAnalysis.dates(sessions).stream()
                .filter(night -> sessions.stream().noneMatch(session -> NightAnalysis.overlaps(session, night)))
                .count();
        return new SleepAnalysisResult<>("Количество бессонных ночей", count);
    }
}

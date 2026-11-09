package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult<?>> {
    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {
        List<LocalDate> nights = NightAnalysis.dates(sessions);

        // для каждой ночи выбираем сессии, пересекающие интервал 00:00–06:00
        List<List<SleepingSession>> sessionsByNight = nights.stream()
                .map(night -> sessions.stream()
                        .filter(session -> NightAnalysis.overlaps(session, night))
                        .toList())
                .filter(nightSessions -> !nightSessions.isEmpty())
                .toList();

        // бессонные ночи уже исключены
        List<Chronotype> nightTypes = sessionsByNight.stream()
                .map(nightSessions -> classify(nightSessions))
                .toList();

        long owlCount = nightTypes.stream().filter(type -> type == Chronotype.OWL).count();
        long larkCount = nightTypes.stream().filter(type -> type == Chronotype.LARK).count();
        long pigeonCount = nightTypes.stream().filter(type -> type == Chronotype.PIGEON).count();

        // при равенстве и при отсутствии ночного сна оставляем голубя
        Chronotype result = Chronotype.PIGEON;
        if (owlCount > larkCount && owlCount > pigeonCount) {
            result = Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            result = Chronotype.LARK;
        }

        return new SleepAnalysisResult<>("Хронотип пользователя", result);
    }

    private Chronotype classify(List<SleepingSession> nightSessions) {
        // если сон прерывался, берём первое засыпание и последнее пробуждение
        LocalDateTime start = nightSessions.stream()
                .map(session -> session.getStart())
                .min(Comparator.naturalOrder())
                .orElseThrow();
        LocalDateTime end = nightSessions.stream()
                .map(session -> session.getEnd())
                .max(Comparator.naturalOrder())
                .orElseThrow();

        LocalTime bedtime = start.toLocalTime();
        LocalTime wakeUpTime = end.toLocalTime();

        // засыпание после полуночи (например, в 01:00) тоже считается поздним
        boolean lateBedtime = bedtime.isAfter(LocalTime.of(23, 0))
                || bedtime.isBefore(LocalTime.NOON);
        boolean earlyBedtime = !bedtime.isBefore(LocalTime.NOON)
                && bedtime.isBefore(LocalTime.of(22, 0));

        if (lateBedtime && wakeUpTime.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }
        if (earlyBedtime && wakeUpTime.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }
        return Chronotype.PIGEON;
    }
}

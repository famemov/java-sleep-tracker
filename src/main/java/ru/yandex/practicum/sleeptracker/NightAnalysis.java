package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

final class NightAnalysis {
    private NightAnalysis() {
    }

    // возвращает список дат. Каждая дата обозначает ночь с 00:00 до 06:00.
    static List<LocalDate> dates(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return List.of();
        }

        // по условию ТЗ сессии в файле уже упорядочены по времени поэтому можно взять первую и последнюю сессию
        LocalDateTime start = sessions.get(0).getStart();
        LocalDateTime end = sessions.get(sessions.size() - 1).getEnd();

        LocalDate firstNight = start.toLocalDate();
        if (!start.toLocalTime().isBefore(LocalTime.NOON)) {
            firstNight = firstNight.plusDays(1);
        }

        return firstNight.datesUntil(end.toLocalDate().plusDays(1)).toList();
    }

    static boolean overlaps(SleepingSession session, LocalDate night) {
        LocalDateTime nightStart = night.atStartOfDay();
        LocalDateTime nightEnd = night.atTime(6, 0);

        return session.getStart().isBefore(nightEnd)
                && session.getEnd().isAfter(nightStart);
    }
}

package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {
    @Test
    void shouldCountAllSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 01:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 02:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 03:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 04:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 05:00", "GOOD")
        );

        var result = new SessionCountFunction().apply(sessions);

        assertEquals(5, result.getValue());
    } // проверка на то что функция считает все сессии сна

    @Test
    void shouldReturnZeroForEmptyList() {
        List<SleepingSession> sessions = List.of();

        var result = new SessionCountFunction().apply(sessions);

        assertEquals(0, result.getValue());
    } // проверка на то что функция считает все сессии сна и возвращает 0 если список пустой

    @Test
    void shouldFindMinimumDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 08:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 06:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 10:00", "GOOD")
        );

        var result = new MinSessionDurationFunction().apply(sessions);

        assertEquals(360L, result.getValue());
    } // проверка на то что функция считает минимальную продолжительность сна

    @Test
    void shouldHandleSingleSessionForMinimum() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 07:00", "GOOD")
        );

        var result = new MinSessionDurationFunction().apply(sessions);

        assertEquals(420L, result.getValue());
    } // проверка на то что функция считает минимальную продолжительность сна и работает с одним сеансом сна

    @Test
    void shouldFindMaximumDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 08:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 06:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 10:00", "GOOD")
        );

        var result = new MaxSessionDurationFunction().apply(sessions);

        assertEquals(600L, result.getValue());
    } // проверка на то что функция считает максимальную продолжительность сна

    @Test
    void shouldHandleSingleSessionForMaximum() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 07:00", "GOOD")
        );

        var result = new MaxSessionDurationFunction().apply(sessions);

        assertEquals(420L, result.getValue());
    } // проверка на то что функция считает максимальную продолжительность сна и работает с одним сеансом сна

    @Test
    void shouldCalculateAverageDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 05:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 10:00", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 07:30", "GOOD")
        );

        var result = new AverageSessionDurationFunction().apply(sessions);

        assertEquals(450L, result.getValue());
    } // проверка на то что функция считает среднюю продолжительность сна

    @Test
    void shouldHandleSingleSessionForAverage() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 08:00", "GOOD")
        );

        var result = new AverageSessionDurationFunction().apply(sessions);

        assertEquals(480L, result.getValue());
    } // проверка на то что функция считает среднюю продолжительность сна и работает с одним сеансом сна

    @Test
    void shouldHandleFractionalAverage() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 00:00", "01.10.26 06:40", "GOOD"),
                new SleepingSession("01.10.26 00:00", "01.10.26 06:41", "GOOD")
        );

        var result = new AverageSessionDurationFunction().apply(sessions);

        assertEquals(400L, result.getValue());
    } // проверка на то что функция считает среднюю продолжительность сна и округляет до целого числа

    @Test
    void shouldCountBadSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 23:00", "02.10.26 07:00", "GOOD"),
                new SleepingSession("02.10.26 23:00", "03.10.26 07:00", "BAD"),
                new SleepingSession("03.10.26 23:00", "04.10.26 07:00", "NORMAL"),
                new SleepingSession("04.10.26 23:00", "05.10.26 07:00", "BAD")
        );

        var result = new PoorSleepSessionCountFunction().apply(sessions);

        assertEquals(2L, result.getValue());
    } // проверка на то что функция считает количество плохих сессий сна

    @Test
    void shouldReturnZeroWhenNoBadSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession("01.10.26 23:00", "02.10.26 07:00", "GOOD"),
                new SleepingSession("02.10.26 23:00", "03.10.26 07:00", "NORMAL"),
                new SleepingSession("03.10.26 23:00", "04.10.26 07:00", "GOOD")
        );

        var result = new PoorSleepSessionCountFunction().apply(sessions);

        assertEquals(0L, result.getValue());
    } // проверка на то что функция считает количество плохих сессий сна и возвращает 0 если нет плохих сессий

    @Test
    void shouldReturnZeroWhenEveryNightHasSleep() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:00", "02.10.26 07:00"),
                session("02.10.26 23:00", "03.10.26 07:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(0L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и возвращает 0 если каждая ночь имеет сон

    @Test
    void shouldCountSleeplessNight() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:00", "02.10.26 07:00"),
                session("03.10.26 23:00", "04.10.26 07:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(1L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и возвращает 1 если есть одна бессонная ночь

    @Test
    void shouldHandleMonthTransition() {
        List<SleepingSession> sessions = List.of(
                session("30.10.26 23:00", "31.10.26 07:00"),
                session("01.11.26 23:00", "02.11.26 07:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(1L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и работает с переходом месяца

    @Test
    void shouldCountSleepAfterSixAsSleepless() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 07:00", "01.10.26 11:00"),
                session("02.10.26 07:00", "02.10.26 11:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(2L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и считает сон после 6 утра как бессонную ночь

    @Test
    void shouldCountSleepCrossingMidnight() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:00", "02.10.26 03:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(0L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей
    // и считает сон пересекающий полночь как не бессонную ночь

    @Test
    void shouldCountSleepStartingAfterMidnight() {
        List<SleepingSession> sessions = List.of(
                session("02.10.26 02:00", "02.10.26 07:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(0L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и считает сон
    // начинающийся после полуночи как не бессонную ночь

    @Test
    void shouldCountSleepStartingAtMidnight() {
        List<SleepingSession> sessions = List.of(
                session("02.10.26 00:00", "02.10.26 06:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(0L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и
    // считает сон начинающийся в полночь как не бессонную ночь

    @Test
    void shouldCountSleepStartingAtSixAsSleepless() {
        List<SleepingSession> sessions = List.of(
                session("02.10.26 06:00", "02.10.26 11:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(1L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и
    // считает сон начинающийся в 6 утра как бессонную ночь

    @Test
    void shouldCountSleepEndingAtMidnightAsSleepless() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:00", "02.10.26 00:00")
        );

        var result = new SleeplessNightCountFunction().apply(sessions);

        assertEquals(1L, result.getValue());
    } // проверка на то что функция считает количество бессонных ночей и

    @Test
    void shouldIdentifyOwl() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:30", "02.10.26 09:30")
        );

        var result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    } // проверка на то что функция определяет хронотип совы

    @Test
    void shouldIdentifyLark() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 21:30", "02.10.26 06:30")
        );

        var result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.LARK, result.getValue());
    } // проверка на то что функция определяет хронотип жаворонка

    @Test
    void shouldIdentifyPigeon() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 22:30", "02.10.26 08:00")
        );

        var result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    } // проверка на то что функция определяет хронотип голубя

    @Test
    void shouldReturnPigeonOnTie() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:30", "02.10.26 09:30"),
                session("02.10.26 21:30", "03.10.26 06:30")
        );

        var result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    } // проверка на то что функция определяет хронотип голубя при равенстве количества ночей совы и жаворонка

    @Test
    void shouldIgnoreDaytimeSessions() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:30", "02.10.26 09:30"),
                session("02.10.26 12:00", "02.10.26 13:00"),
                session("02.10.26 14:00", "02.10.26 15:00"),
                session("02.10.26 23:30", "03.10.26 09:30"),
                session("03.10.26 12:00", "03.10.26 13:00")
        );

        var result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    } // проверка на то что функция определяет хронотип совы и игнорирует дневные сессии сна

    private SleepingSession session(String start, String end) {
        return new SleepingSession(start, end, "GOOD");
    }
} // для тестов я сделал отдельный класс SleepTrackerAppTest.java и написал тесты на все функции, которые были в проекте

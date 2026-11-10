package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChronotypeFunctionTest {
    @Test
    void shouldIdentifyOwl() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:30", "02.10.26 09:30")
        );

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    } // проверка на то что функция определяет хронотип совы

    @Test
    void shouldIdentifyLark() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 21:30", "02.10.26 06:30")
        );

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.LARK, result.getValue());
    } // проверка на то что функция определяет хронотип жаворонка

    @Test
    void shouldIdentifyPigeon() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 22:30", "02.10.26 08:00")
        );

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    } // проверка на то что функция определяет хронотип голубя

    @Test
    void shouldReturnPigeonOnTie() {
        List<SleepingSession> sessions = List.of(
                session("01.10.26 23:30", "02.10.26 09:30"),
                session("02.10.26 21:30", "03.10.26 06:30")
        );

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

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

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    } // проверка на то что функция определяет хронотип совы и игнорирует дневные сессии сна

    @Test
    void shouldClassifyEmptyList() {
        List<SleepingSession> sessions = List.of();

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyOnlyDaytimeSleep() {
        List<SleepingSession> sessions = List.of(session("01.10.26 10:00", "01.10.26 11:00"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyOnlySleeplessNights() {
        List<SleepingSession> sessions = List.of(session("01.10.26 23:00", "02.10.26 00:00"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyBedtimeExactlyAt23() {
        List<SleepingSession> sessions = List.of(session("01.10.26 23:00", "02.10.26 09:30"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyBedtimeAfter23() {
        List<SleepingSession> sessions = List.of(session("01.10.26 23:01", "02.10.26 09:30"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    void shouldClassifyWakeUpExactlyAt9() {
        List<SleepingSession> sessions = List.of(session("01.10.26 23:30", "02.10.26 09:00"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyWakeUpAfter9() {
        List<SleepingSession> sessions = List.of(session("01.10.26 23:30", "02.10.26 09:01"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    void shouldClassifyBedtimeExactlyAt22() {
        List<SleepingSession> sessions = List.of(session("01.10.26 22:00", "02.10.26 06:30"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyBedtimeBefore22() {
        List<SleepingSession> sessions = List.of(session("01.10.26 21:59", "02.10.26 06:30"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    void shouldClassifyWakeUpExactlyAt7() {
        List<SleepingSession> sessions = List.of(session("01.10.26 21:30", "02.10.26 07:00"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldClassifyWakeUpBefore7() {
        List<SleepingSession> sessions = List.of(session("01.10.26 21:30", "02.10.26 06:59"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    void shouldClassifyBedtimeAfterMidnight() {
        List<SleepingSession> sessions = List.of(session("02.10.26 01:00", "02.10.26 09:30"));

        SleepAnalysisResult<Chronotype> result = new ChronotypeFunction().apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    }

    //крч добавил тесты на границы и отсутствие ночного сна
    private SleepingSession session(String start, String end) {
        return new SleepingSession(start, end, "GOOD");
    }
}

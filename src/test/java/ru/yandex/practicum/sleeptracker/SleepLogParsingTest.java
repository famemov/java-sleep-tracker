package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SleepLogParsingTest {
    @TempDir
    Path tempDir;

    @Test
    void shouldParseSessionsInFileOrder() throws IOException {
        Path file = writeLog("01.10.26 23:00;02.10.26 07:00;GOOD\n"
                + "02.10.26 23:30;03.10.26 06:00;BAD\n"
                + "03.10.26 22:00;04.10.26 06:00;NORMAL\n");

        List<SleepingSession> sessions = SleepTrackerApp.readSessions(file.toString());

        assertEquals(3, sessions.size());
        assertEquals(LocalDateTime.of(2026, 10, 1, 23, 0), sessions.get(0).getStart());
        assertEquals(LocalDateTime.of(2026, 10, 2, 7, 0), sessions.get(0).getEnd());
        assertEquals(SleepQuality.GOOD, sessions.get(0).getQualityOfSleep());
        assertEquals(LocalDateTime.of(2026, 10, 2, 23, 30), sessions.get(1).getStart());
        assertEquals(390L, sessions.get(1).getDurationMinutes());
        assertEquals(SleepQuality.BAD, sessions.get(1).getQualityOfSleep());
        assertEquals(SleepQuality.NORMAL, sessions.get(2).getQualityOfSleep());
    }

    @Test
    void shouldIgnoreBlankLinesAndTrimFields() throws IOException {
        Path file = writeLog("\n   \n 01.10.26 23:00 ; 02.10.26 07:00 ; GOOD \n\n");

        List<SleepingSession> sessions = SleepTrackerApp.readSessions(file.toString());

        assertEquals(1, sessions.size());
        assertEquals(480L, sessions.get(0).getDurationMinutes());
        assertEquals(SleepQuality.GOOD, sessions.get(0).getQualityOfSleep());
    }

    @Test
    void shouldReturnEmptyListForEmptyFile() throws IOException {
        Path file = writeLog("");

        assertTrue(SleepTrackerApp.readSessions(file.toString()).isEmpty());
    }

    @Test
    void shouldRejectMissingField() throws IOException {
        Path file = writeLog("01.10.26 23:00;02.10.26 07:00");

        assertThrows(IllegalArgumentException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    @Test
    void shouldRejectExtraField() throws IOException {
        Path file = writeLog("01.10.26 23:00;02.10.26 07:00;GOOD;extra");

        assertThrows(IllegalArgumentException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    @Test
    void shouldRejectEmptyQuality() throws IOException {
        Path file = writeLog("01.10.26 23:00;02.10.26 07:00;");

        assertThrows(IllegalArgumentException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    @Test
    void shouldRejectInvalidDateFormat() throws IOException {
        Path file = writeLog("2026-10-01 23:00;02.10.26 07:00;GOOD");

        assertThrows(DateTimeParseException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    @Test
    void shouldRejectUnknownQuality() throws IOException {
        Path file = writeLog("01.10.26 23:00;02.10.26 07:00;UNKNOWN");

        assertThrows(IllegalArgumentException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    @Test
    void shouldRejectWakeUpBeforeBedtime() throws IOException {
        Path file = writeLog("02.10.26 07:00;01.10.26 23:00;GOOD");

        assertThrows(IllegalArgumentException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    @Test
    void shouldReportMissingFile() {
        Path file = tempDir.resolve("missing.txt");

        assertThrows(NoSuchFileException.class, () -> SleepTrackerApp.readSessions(file.toString()));
    }

    //тесты на парсинг файлов
    private Path writeLog(String content) throws IOException {
        return Files.writeString(tempDir.resolve("sleep_log.txt"), content);
    }
}

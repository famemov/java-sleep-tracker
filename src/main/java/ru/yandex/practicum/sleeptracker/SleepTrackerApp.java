package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult<?>>> analysisFunctions = List.of(
            new SessionCountFunction(),
            new MinSessionDurationFunction(),
            new MaxSessionDurationFunction(),
            new AverageSessionDurationFunction(),
            new PoorSleepSessionCountFunction(),
            new SleeplessNightCountFunction(),
            new ChronotypeFunction()
    );

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Передайте один путь к файлу с логом сна");
            return;
        } // когда я проверял проект по соответсвию тз то там было сказано что нужно передавать путь к файлу с
        // логом сна в качестве аргумента командной строки но не очень понял как и сделал две реализации через нейронку
        String filePath;
        if (args.length == 1) {
            filePath = args[0];
        } else {
            System.out.println("Введите путь к файлу с логом сна:");
            Scanner scanner = new Scanner(System.in);
            if (!scanner.hasNextLine()) {
                System.out.println("Путь к файлу не указан");
                return;
            }
            filePath = scanner.nextLine().trim();
        }
        if (filePath.isBlank()) {
            System.out.println("Путь к файлу не указан");
            return;
        }

        List<SleepingSession> sessions = Files.readAllLines(Path.of(filePath)).stream()
                .filter(line -> !line.isBlank())
                .map(line -> line.split(";", -1))
                .map(fields -> {
                    if (fields.length != 3) {
                        throw new IllegalArgumentException("Запись должна содержать три поля через точку с запятой");
                    }
                    return new SleepingSession(fields[0], fields[1], fields[2]);
                })
                .toList();

        analysisFunctions.stream()
                .map(function -> function.apply(sessions))
                .forEach(result -> System.out.println(result.getDescription() + ": " + result.getValue()));
    }
}

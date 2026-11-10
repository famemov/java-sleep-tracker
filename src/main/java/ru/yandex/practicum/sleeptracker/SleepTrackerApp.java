package ru.yandex.practicum.sleeptracker;
//нейронками я пользуюсь для генерации похожего кода или примера метода который я сам не могу придумать как реализовать,
//а так беру все методы из obsidian брейна своего. а еще скелет приложеня когда тз прочитал могу сгенерировать
//основываясь что понял и какие классы в голове с методами возникли(не кидаю тз а сам пытаюсь понять ну и в подсказки
// заглядываю). также иногда коментарии делаю через копилот и тесты так комментирую чтобы не путаться.
// я всетаки не весь проект закидываю и говорю нейронке сделать его :p
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
        if (args.length == 0 || args[0].isBlank()) { // убрал scanner и путь берется из args[0]
            System.out.println("Путь к файлу не указан");
            return;
        }

        List<SleepingSession> sessions = readSessions(args[0]);

        analysisFunctions.stream()
                .map(function -> function.apply(sessions))
                .forEach(result -> System.out.println(result.getDescription() + ": " + result.getValue()));
    }

    static List<SleepingSession> readSessions(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath)).stream()
                .filter(line -> !line.isBlank())
                .map(line -> line.split(";", -1))
                .map(fields -> {
                    if (fields.length != 3) {
                        throw new IllegalArgumentException("Запись должна содержать три поля через точку с запятой");
                    }
                    return new SleepingSession(fields[0], fields[1], fields[2]);
                })
                .toList();
        //вынес в отдельный метод парсер
    }
}

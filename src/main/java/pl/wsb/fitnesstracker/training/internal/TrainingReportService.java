package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingProvider;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingReportService {

    private final TrainingProvider trainingProvider;

    public void generateAndPrintReport() {
        // Pobranie wszystkich treningów
        List<Training> allTrainings = trainingProvider.findAllTrainings();

        if (allTrainings.isEmpty()) {
            System.out.println("Brak treningów w systemie.");
            return;
        }

        // Grupowanie: User -> Numer Tygodnia -> Lista Treningów
        Map<User, Map<Integer, List<Training>>> report = allTrainings.stream()
                .collect(Collectors.groupingBy(
                        Training::getUser, // Grupowanie po użytkowniku
                        Collectors.groupingBy(training -> {
                            // Konwersja Date -> LocalDate, aby wyciągnąć numer tygodnia
                            return training.getStartTime().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .get(WeekFields.of(Locale.getDefault()).weekOfYear());
                        })
                ));

        // Wyświetlanie sformatowanego raportu
        printReportToConsole(report);
    }

    private void printReportToConsole(Map<User, Map<Integer, List<Training>>> report) {
        System.out.println("\n=======================================================");
        System.out.println("       TYGODNIOWE PODSUMOWANIE TRENINGÓW UŻYTKOWNIKÓW");
        System.out.println("=======================================================");

        report.forEach((user, weeks) -> {
            System.out.printf("| Użytkownik: %s %s (ID: %d)%n",
                    user.getFirstName(), user.getLastName(), user.getId());

            weeks.forEach((weekNumber, trainings) -> {
                System.out.println("|--- Tydzień nr " + weekNumber);
                System.out.println("|    Liczba treningów: " + trainings.size());

                // suma dystansu
                double totalDistance = trainings.stream().mapToDouble(Training::getDistance).sum();
                System.out.printf("|    Łączny dystans: %.2f km%n", totalDistance);

                // Wypisanie szczegółów każdego treningu
                trainings.forEach(t -> System.out.printf("|      - %s: %.2f km%n",
                        t.getActivityType().getDisplayName(), t.getDistance()));
            });
            System.out.println("|------------------------------------------------------\n");
        });
    }
}
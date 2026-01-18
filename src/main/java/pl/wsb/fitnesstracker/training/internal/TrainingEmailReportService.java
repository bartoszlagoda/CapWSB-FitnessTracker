package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.mail.api.EmailDto;
import pl.wsb.fitnesstracker.mail.api.EmailSender;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingProvider;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingEmailReportService {

    private final TrainingProvider trainingProvider;
    private final EmailSender emailSender;
    private final UserProvider userProvider;

    @Scheduled(cron = "0 */10 * * * *") // 1 minuta 1 sekunda
    public void sendWeeklySummaryToUsers() {
        log.info("Rozpoczynam generowanie raportów e-mail...");
        List<User> users = userProvider.findAllUsers();

        for (User user : users) {
            // KROK 1: Try-Catch wewnątrz pętli - błąd u jednego usera nie blokuje innych!
            try {
                List<Training> userTrainings = trainingProvider.getTrainingsByUserId(user.getId());
                String emailContent = generateEmailContent(user, userTrainings);
                String title = "Podsumowanie treningów - FitnessTracker";
                String recipient = user.getEmail();

                if (recipient != null && !recipient.isEmpty()) {
                    EmailDto emailDto = new EmailDto(recipient, "admin@fitnesstracker.pl", title, emailContent);

                    emailSender.send(emailDto);
                    log.info("Wysłano email do użytkownika: {}", user.getId());

                    // KROK 2: Sztuczne opóźnienie (Rate Limiting)
                    Thread.sleep(61000); // 1 minuta 1 sekunda
                } else {
                    log.warn("Użytkownik ID: {} nie ma adresu e-mail!", user.getId());
                }

            } catch (Exception e) {
                // Łapiemy błąd (np. Rate Limit), logujemy go, ale pętla idzie do następnego usera!
                log.error("Nie udało się wysłać maila do użytkownika ID: " + user.getId(), e);
            }
        }
        log.info("Zakończono wysyłanie raportów.");
    }

    private String generateEmailContent(User user, List<Training> trainings) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cześć ").append(user.getFirstName()).append(",\n\n");
        sb.append("Oto Twoje statystyki treningowe:\n");

        // Główne wymaganie: łączna liczba treningów
        sb.append("Łączna liczba zarejestrowanych treningów: ").append(trainings.size()).append("\n");

        // Opcjonalnie: Dodatkowe statystyki (np. dystans)
        double totalDistance = trainings.stream().mapToDouble(Training::getDistance).sum();
        sb.append("Łącznie pokonany dystans: ").append(totalDistance).append(" km\n\n");

        sb.append("Pozdrawiamy,\nZespół FitnessTracker");
        return sb.toString();
    }
}

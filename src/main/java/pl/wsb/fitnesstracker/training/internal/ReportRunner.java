package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportRunner implements CommandLineRunner {
    private final TrainingReportService trainingReportService;

    @Override
    public void run(String... args) {
        // Po uruchomieniu aplikacji Spring wywoła tę metodę
        trainingReportService.generateAndPrintReport();
    }
}

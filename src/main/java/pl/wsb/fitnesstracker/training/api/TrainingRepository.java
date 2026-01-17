package pl.wsb.fitnesstracker.training.api;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    // Spring wygeneruje SQL: SELECT * FROM trainings WHERE user_id = ?
    List<Training> findAllByUserId(Long userId);
}

package pl.wsb.fitnesstracker.user.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wsb.fitnesstracker.user.api.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
//    default Optional<User> findByEmail(String email) {
//        return findAll().stream()
//                .filter(user -> Objects.equals(user.getEmail(), email))
//                .findFirst();
//    }
    // 1. To służy do szukania konkretnego usera (np. przy logowaniu/tworzeniu)
    // Spring sam wygeneruje SQL: WHERE email = ?
    Optional<User> findByEmail(String email);

    // 2. To służy do wyszukiwania po fragmencie (case-insensitive)
    // Spring sam wygeneruje SQL: WHERE lower(email) LIKE lower(%?%)
    List<User> findAllByEmailContainingIgnoreCase(String emailFragment);

    /**
     * Finds users born before a specific date.
     * Spring generates SQL: SELECT * FROM users WHERE birthdate < ?
     */
    List<User> findAllByBirthdateBefore(LocalDate date);

}

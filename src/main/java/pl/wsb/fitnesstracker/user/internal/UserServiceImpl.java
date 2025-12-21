package pl.wsb.fitnesstracker.user.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;

import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(final User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        // Możesz najpierw sprawdzić, czy użytkownik istnieje, jeśli chcesz rzucić błąd 404:
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }

        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User userWithNewData) {
        // 1. Pobieramy użytkownika z bazy (jeśli nie ma -> błąd)
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));

        // 2. Aktualizujemy pola (nadpisujemy stare dane nowymi)
        existingUser.setFirstName(userWithNewData.getFirstName());
        existingUser.setLastName(userWithNewData.getLastName());
        existingUser.setBirthdate(userWithNewData.getBirthdate());
        existingUser.setEmail(userWithNewData.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> searchUsersByEmail(String emailFragment) {
        return userRepository.findAllByEmailContainingIgnoreCase(emailFragment);
    }
    @Override
    public List<User> findAllUsersOlderThan(LocalDate date) {
        // urodzeni przed daną datą
        return userRepository.findAllByBirthdateBefore(date);
    }
}
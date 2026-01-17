package pl.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserProvider {

    /**
     * Retrieves a user based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param userId id of the user to be searched
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUser(Long userId);

    /**
     * Retrieves a user based on their email.
     * If the user with given email is not found, then {@link Optional#empty()} will be returned.
     *
     * @param email The email of the user to be searched
     * @return An {@link Optional} containing the located user, or {@link Optional#empty()} if not found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Searches for users whose email contains the given fragment (case-insensitive).
     *
     * @param emailFragment the fragment of email to search for
     * @return list of matching users
     */
    List<User> searchUsersByEmail(String emailFragment);

    /**
     * Retrieves all users.
     *
     * @return An {@link Optional} containing the all users,
     */
    List<User> findAllUsers();
    /**
     * Retrieves a user by id (alias / explicit name).
     *
     * @param id user id
     * @return optional user
     */
    Optional<User> findUserById(Long id);
    /**
     * Retrieves users older than the specified age.
     * @param age the minimum age
     * @return list of users older than age
     */
    List<User> findAllUsersOlderThan(LocalDate age);
}

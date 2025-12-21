package pl.wsb.fitnesstracker.user.internal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.fitnesstracker.user.api.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wsb.fitnesstracker.user.api.UserDto;

import java.time.LocalDate;
import java.util.List;

/**
 * UserController is responsible for handling HTTP requests related to user operations.
 * It provides endpoints for retrieving and creating users.
 */
@RestController
@RequestMapping("/v1/users")
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    private final UserProvider userProvider;
    public UserController(UserServiceImpl userService, UserMapper userMapper, UserProvider userProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userProvider = userProvider;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/simple")
    public List<SimpleUserDto> getAllSimpleUsers() {
        return userService.findAllUsers()
                .stream()
                .map(userMapper::toSimpleDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        var user = userMapper.fromDto(userDto); // dodaj tę metodę w mapperze jeśli brak
        var created = userService.createUser(user);
        return userMapper.toDto(created);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        // 1. Zamieniamy DTO na encję (mappera już masz)
        User userWithNewData = userMapper.fromDto(userDto);

        // 2. Przekazujemy do serwisu ID oraz nowe dane
        User updatedUser = userService.updateUser(id, userWithNewData);

        // 3. Zwracamy zaktualizowanego użytkownika
        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Zwraca kod 204 (sukces, brak treści)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/email")
    public List<UserEmailDto> searchUsers(@RequestParam String email) {
        return userProvider.searchUsersByEmail(email)
                .stream()
                .map(userMapper::toEmailDto)
                .toList();
    }

    @GetMapping("/older/{time}")
    public List<UserDto> getUsersOlderThan(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate time) {
        return userProvider.findAllUsersOlderThan(time)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

}


package pl.wsb.fitnesstracker.user.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.user.api.SimpleUserDto;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserEmailDto;

import java.time.LocalDate;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdate(),
                user.getEmail());
    }

    public SimpleUserDto toSimpleDto(User user) {
        return new SimpleUserDto(user.getId(),
                user.getFirstName(),
                user.getLastName());
    }

    // DTO -> encja, używane przy tworzeniu nowego użytkownika
    User fromDto(UserDto dto) {
        return new User(dto.firstName(),
                dto.lastName(),
                dto.birthdate(),
                dto.email());
    }

    UserEmailDto toEmailDto(User user) {
        return new UserEmailDto(
                user.getId(),
                user.getEmail()
        );
    }
}

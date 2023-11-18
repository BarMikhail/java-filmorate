package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
//@AllArgsConstructor
@Builder
public class User {
    @NotNull
    private Integer id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void deleteFriends(Integer id) {
        if (!friends.contains(id)) {
            throw new NotFoundException(String.format("У пользователя %s нет друга с таким id %s", getName(), id));
        }
        friends.remove(id);
    }
}

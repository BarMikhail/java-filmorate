package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @Override
    public List<User> getAllUser() {
        log.info("Вывод всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(generateId++);
        log.info("Пользователь с id {} добавлен", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Id %s нет", user.getId()));
        }
        validate(user);
        users.get(user.getId()).setName(user.getName());
        users.get(user.getId()).setBirthday(user.getBirthday());
        users.get(user.getId()).setEmail(user.getEmail());
        users.get(user.getId()).setLogin(user.getLogin());
        log.info("Пользователь с id {}  обновлен", user.getId());
        return user;
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
        users.remove(id);
        log.info("Пользователь с id {} удален", id);
    }

    @Override
    public User getById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
        log.info("Вывод пользователя с id {}", id);
        return users.get(id);
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Email у пользователя с id {} пуст", user.getId());
            throw new ValidationException("Проверь Email");
        }
        if (!(user.getLogin() == null || user.getLogin().isBlank())) {
            String[] login = user.getLogin().split(" ");
            if (login.length > 1) {
                log.warn("У пользователя с id {} что-то не так с login", user.getId());
                throw new ValidationException("Проверь login");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения {} не может быть в будущем", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}

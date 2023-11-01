package ru.yandex.practicum.filmorate.controller;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private int generateId = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        validate(user);

        user.setId(generateId++);
        log.info("Пользователь добавлен");
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUser() {
        log.info("Все пользователи выведены");
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Нет такого Id");
        }
        validate(user);
        log.info("Пользователь обновлен");
        users.put(user.getId(), user);
        return user;
    }

    public void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Email пуст");
            throw new ValidationException("Проверь Email");
        }
        if (!(user.getLogin() == null || user.getLogin().isBlank())) {
            String[] login = user.getLogin().split(" ");
            if (login.length > 1) {
                log.warn("Что-то не так с login");
                throw new ValidationException("Проверь login");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}

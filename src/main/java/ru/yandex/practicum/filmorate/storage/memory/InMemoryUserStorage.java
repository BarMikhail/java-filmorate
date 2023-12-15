package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("user")
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
        Validate.validateUser(user);
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
        Validate.validateUser(user);
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
}

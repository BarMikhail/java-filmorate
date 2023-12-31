package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUser();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    User getById(int id);
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage inMemoryUserStorage;


    public void addFriend(int id, int friendId) {
        if (id == friendId) {
            throw new ValidationException("Ты не можешь быть своим же другом");
        }
        User firstUser = inMemoryUserStorage.getById(id);
        User secondUser = inMemoryUserStorage.getById(friendId);
        firstUser.addFriends(friendId);
        secondUser.addFriends(id);
        log.debug("Пользователь с id {} добавил в друзья пользователя с id {}", id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        if (id == friendId) {
            throw new ValidationException("Ты не можешь быть своим же другом");
        }
        inMemoryUserStorage.getById(id).deleteFriends(friendId);
        inMemoryUserStorage.getById(friendId).deleteFriends(id);
        log.debug("Пользователь с id {} удалил из друзей пользователя с id {}", id, friendId);
    }

    public List<User> getFriend(int id) {
        Set<Integer> friends = inMemoryUserStorage.getById(id).getFriends();
        log.info("Вывод друзей пользователя {}", id);
        return friends.stream()
                .map(inMemoryUserStorage::getById)
                .collect(Collectors.toList());
    }

    public final List<User> getCommonFriend(int id, int otherId) {
        Set<Integer> user = inMemoryUserStorage.getById(id).getFriends();
        Set<Integer> userFriend = inMemoryUserStorage.getById(otherId).getFriends();
        List<User> users = new ArrayList<>();
        for (int friend : user) {
            if (userFriend.contains(friend)) {
                users.add(inMemoryUserStorage.getById(friend));
            }
        }
        log.debug("Вывод общих друзей {} с {}", id, otherId);
        return users;
    }

    public User createUser(User user) {
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        inMemoryUserStorage.deleteUser(id);
    }

    public User getById(int id) {
        return inMemoryUserStorage.getById(id);
    }

    public List<User> getAllUser() {
        return inMemoryUserStorage.getAllUser();
    }


}

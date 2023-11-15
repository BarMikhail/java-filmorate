package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage inMemoryUserStorage;


    public void addFriend(int id, int friendId) {
//        log.info("alarm");
        if (id == friendId) {
            throw new ValidationException("Ты не можешь быть своим же другом");
        }
//        User u1 = inMemoryUserStorage.getById(id);
//        User u2 = inMemoryUserStorage.getById(friendId);
//        u1.addFriends(friendId);
//        u2.addFriends(id);
        inMemoryUserStorage.getById(id).addFriends(friendId);
        inMemoryUserStorage.getById(friendId).addFriends(id);
        log.info("Пользователь с id {} добавил в друзья пользователя с id {}", id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
//        if (id == friend) {
//            throw new ValidationException("Ты не можешь быть своим же другом");
//        }
        inMemoryUserStorage.getById(id).deleteFriends(friendId);
        inMemoryUserStorage.getById(friendId).deleteFriends(id);
        log.info("Пользователь с id {} удалил из друзей пользователя с id {}", id, friendId);
    }

    public List<User> getFriend(int id) {
        Set<Integer> friends = inMemoryUserStorage.getById(id).getFriends();
        log.info("Вывод друзей пользователя {}", id);
        return friends.stream()
                .map(inMemoryUserStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriend(int id, int otherId) {
        Set<Integer> user = inMemoryUserStorage.getById(id).getFriends();
        Set<Integer> userFriend = inMemoryUserStorage.getById(otherId).getFriends();
        List<User> users = new ArrayList<>();
        for (int friend : user) {
            if (userFriend.contains(friend)) {
                users.add(inMemoryUserStorage.getById(friend));
            }
        }
        log.info("Вывод общих друзей {} с {}", id, otherId);
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

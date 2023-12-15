package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(@Qualifier("user2") UserStorage userStorage, @Qualifier("friend") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }


    public void addFriend(int id, int friendId) {
        User user = getById(id);
        User friendUser = getById(friendId);
        Collection<User> users = getAllUser();
        if (!users.contains(user) && users.contains(friendUser)) {
            throw new ValidationException("User not found");
        } else {
            friendStorage.addFriend(id, friendId);
            log.info("Добавление в друзья");
        }
    }

    public void deleteFriend(int id, int friendId) {
        friendStorage.deleteFriend(id, friendId);
        log.info("Удаление из друзья");
    }

    public List<User> getFriend(int id) {
        List<User> userFriends = new ArrayList<>();
        List<Integer> friendsId = friendStorage.getFriend(id);
        for (Integer userId : friendsId) {
            userFriends.add(getById(userId));
        }
        log.info("Вывод друзей");
        return userFriends;
    }

    public final List<User> getCommonFriend(int id, int otherId) {
        List<User> user = getFriend(id);
        List<User> anotherUser = getFriend(otherId);
        log.info("Вывод общих друзей");
        return user.stream().filter(anotherUser::contains).collect(Collectors.toList());
    }

    public User createUser(User user) {
        Validate.validateUser(user);
        log.info("Создание пользователя");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        Validate.validateUser(user);
        log.info("Обновления пользователя");
        return userStorage.updateUser(user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
        log.info("Удаление пользователя");
    }

    public User getById(int id) {
        log.info("Вывод определенного пользователя");
        return userStorage.getById(id);
    }

    public List<User> getAllUser() {
        log.info("Вывод всех пользователя");
        return userStorage.getAllUser();
    }

}

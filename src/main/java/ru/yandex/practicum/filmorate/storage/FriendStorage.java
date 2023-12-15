package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FriendStorage {
    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<Integer> getFriend(int id);

}

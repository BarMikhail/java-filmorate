package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("friendDBStorage")
@RequiredArgsConstructor
public class FriendStorageImpl implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int id, int friendId) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) " +
                "VALUES (?, ?)", id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", id, friendId);
    }

    @Override
    public List<User> getFriends(int id){
        return jdbcTemplate.query("SELECT u.* FROM users AS u " +
                "RIGHT JOIN friends AS f ON u.user_id = f.friend_id WHERE f.user_id = ?", UserStorageImpl::mapRowToUser, id);
    }

    private static Integer rowMapToLongIdFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }

}

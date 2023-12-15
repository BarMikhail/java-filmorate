package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("friend")
@RequiredArgsConstructor
public class FriendImpl implements FriendStorage {
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
    public List<Integer> getFriend(int id) {
        return jdbcTemplate.query("SELECT friend_id FROM friends WHERE user_id = ?", FriendImpl::rowMapToLongIdFriends, id);
    }

    private static Integer rowMapToLongIdFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }

}

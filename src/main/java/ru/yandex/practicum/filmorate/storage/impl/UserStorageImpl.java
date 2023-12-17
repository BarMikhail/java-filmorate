package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component("userDBStorage")
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAllUser() {
        log.info("Вывод всех пользователей");
        return jdbcTemplate.query("SELECT * FROM users", UserStorageImpl::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        if (getAllUser().contains(user)) {
            throw new ValidationException("Пользователь уже существует");
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("users")
                    .usingGeneratedKeyColumns("user_id");
            int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
            user.setId(id);
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if (getById(user.getId()) != null) {
            jdbcTemplate.update(
                    "UPDATE users SET email = ?, login = ?, name  = ?, birthday = ? WHERE user_id = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteUser(int id) {
        if (getById(id) == null) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
        log.info("Пользователь с id {} удален", id);
    }

    @Override
    public User getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", UserStorageImpl::mapRowToUser, id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
    }

    public static User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("user_id");
        String name = rs.getString("name");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .login(login)
                .birthday(birthday)
                .build();
    }
}

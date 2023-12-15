package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.FriendImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {
    private User user;
    private User user1;
    private final JdbcTemplate jdbcTemplate;
    private UserService userService;


    @BeforeEach
    public void setUp() {
        userService = new UserService(new UserImpl(jdbcTemplate), new FriendImpl(jdbcTemplate));
        user = User.builder()
                .id(1)
                .email("na@s")
                .name("dss")
                .login("asd")
                .birthday(LocalDate.of(2000, 2, 10))
                .build();
        user1 = User.builder()
                .id(2)
                .email("ksld@sfdpl")
                .name("qwer")
                .login("asdf")
                .birthday(LocalDate.of(1994, 12, 20))
                .build();
    }

    @DisplayName("Проверка создания пользователя")
    @Test
    void createUser() {
        assertNotNull(userService.createUser(user));
    }

    @DisplayName("Создание пользователя с не верным email")
    @Test
    void createUserWithoutEmail() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userService.createUser(user));
    }

    @DisplayName("Создание пользователя с не верным именем")
    @Test
    void createUserWithoutName() {
        user.setName("");
        userService.createUser(user);
        assertEquals(userService.getById(1).getName(), user.getLogin());
    }

    @DisplayName("Создание пользователя с не верным login")
    @Test
    void createUserWithoutLogin() {
        user.setLogin(" ad min ");
        assertThrows(ValidationException.class, () -> userService.createUser(user));
    }

    @DisplayName("Создание пользователя с неверной датой")
    @Test
    void createUserWithoutData() {
        user.setBirthday(LocalDate.of(2040, 2, 10));
        assertThrows(ValidationException.class, () -> userService.createUser(user));
    }

    @DisplayName("Проверка вывода всех пользователей")
    @Test
    void getAllUsers() {
        userService.createUser(user);
        userService.createUser(user1);
        assertEquals(userService.getAllUser().size(), 2);
    }

    @DisplayName("Проверка обновления пользователя")
    @Test
    void updateUser() {
        userService.createUser(user);
        userService.updateUser(User.builder()
                .id(1)
                .email("na@s")
                .name("name")
                .login("asd")
                .birthday(LocalDate.of(2000, 2, 10))
                .build());
        assertEquals(userService.getById(1).getName(), "name");
    }

    @DisplayName("Проверка вывода 1 пользователя")
    @Test
    void getByIdTest() {
        userService.createUser(user);
        userService.createUser(user1);
        assertEquals(userService.getById(2), user1);
    }

    @DisplayName("Проверка удаления 1 пользователя")
    @Test
    void getDeleteTest() {
        userService.createUser(user);
        userService.createUser(user1);
        userService.deleteUser(1);
        assertEquals(userService.getAllUser().size(), 1);
    }

    @DisplayName("Добавления друга к пользователю")
    @Test
    public void addFriendByUserIdTest() {
        userService.createUser(user);
        User friend = userService.createUser(user1);
        userService.addFriend(1, 2);
        assertEquals(List.of(friend), userService.getFriend(1));
    }

    @DisplayName("Удаления друга у пользователя")
    @Test
    public void deleteFriendByUserIdTest() {
        userService.createUser(user);
        User friend = userService.createUser(user1);
        userService.addFriend(1, 2);
        assertEquals(List.of(friend), userService.getFriend(1));
        userService.deleteFriend(1, 2);
        assertEquals(List.of(), userService.getFriend(1));
    }

    @DisplayName("Тест получения всех Friends у существующего User по Id")
    @Test
    public void getAllFriendByUserIdTest() {
        User user2 = User.builder()
                .id(3)
                .email("qwer@s")
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2001, 4, 16))
                .build();
        userService.createUser(user);
        User friend = userService.createUser(user1);
        User friend2 = userService.createUser(user2);
        userService.addFriend(1, 2);
        userService.addFriend(1, 3);
        assertEquals(List.of(friend, friend2), userService.getFriend(1));
    }
}
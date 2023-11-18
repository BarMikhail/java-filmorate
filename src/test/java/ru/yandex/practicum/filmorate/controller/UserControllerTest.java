package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserControllerTest {
    private User user;
    private User user1;
    private InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        user = User.builder()
                .email("na@s")
                .name("dss")
                .login("asd")
                .birthday(LocalDate.of(2000, 2, 10))
                .build();
        user1 = User.builder()
                .email("ksld@sfdpl")
                .name("qwer")
                .login("asdf")
                .birthday(LocalDate.of(1994, 12, 20))
                .build();
    }

    @DisplayName("Проверка создания пользователя")
    @Test
    void createUser() {
        assertNotNull(inMemoryUserStorage.createUser(user));
    }

    @DisplayName("Создание пользователя с не верным email")
    @Test
    void createUserWithoutEmail() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(user));
    }

    @DisplayName("Создание пользователя с не верным именем")
    @Test
    void createUserWithoutName() {
        user.setName("");
        inMemoryUserStorage.createUser(user);
        assertEquals(inMemoryUserStorage.getById(1).getName(),user.getLogin());
    }

    @DisplayName("Создание пользователя с не верным login")
    @Test
    void createUserWithoutLogin() {
        user.setLogin(" ad min ");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(user));
    }

    @DisplayName("Создание пользователя с неверной датой")
    @Test
    void createUserWithoutData() {
        user.setBirthday(LocalDate.of(2040, 2, 10));
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(user));
    }
//
    @DisplayName("Проверка вывода всех пользователей")
    @Test
    void getAllUsers() {
        inMemoryUserStorage.createUser(user);
        inMemoryUserStorage.createUser(user1);
        assertEquals(inMemoryUserStorage.getAllUser().size(), 2);
    }
//
    @DisplayName("Проверка обновления пользователя")
    @Test
    void updateUser() {
        inMemoryUserStorage.createUser(user);
        inMemoryUserStorage.updateUser(User.builder()
                .id(1)
                .email("na@s")
                .name("name")
                .login("asd")
                .birthday(LocalDate.of(2000, 2, 10))
                .build());
        assertEquals(inMemoryUserStorage.getById(1).getName(),"name");
    }
    @DisplayName("Проверка вывода 1 пользователя")
    @Test
    void getByIdTest() {
        inMemoryUserStorage.createUser(user);
        inMemoryUserStorage.createUser(user1);
        assertEquals(inMemoryUserStorage.getById(2), user1);
    }

    @DisplayName("Проверка удаления 1 пользователя")
    @Test
    void getDeleteTest() {
        inMemoryUserStorage.createUser(user);
        inMemoryUserStorage.createUser(user1);
        inMemoryUserStorage.deleteUser(1);
        assertEquals(inMemoryUserStorage.getAllUser().size(), 1);
    }
}
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private InMemoryUserStorage inMemoryUserStorage;

//    @BeforeEach
//    public void setUp() {
//        inMemoryUserStorage = new InMemoryUserStorage();
//    }
//
//    @DisplayName("Проверка создания юзиров")
//    @Test
//    void createUser() {
//        User create = inMemoryUserStorage.createUser(new User("na@", "fdg", "sd", LocalDate.of(2000, 2, 10)));
//        assertNotNull(create);
//    }
//
//    @DisplayName("Создание юзера с не верным именем")
//    @Test
//    void createUserWithoutName() {
//        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User("", "fdg",
//                "sd", LocalDate.of(2000, 2, 10))));
//    }
//
//    @DisplayName("Создание юзера с не верным именем")
//    @Test
//    void createUserWithoutLogin() {
//        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User("na@", "asd sa ",
//                "sd", LocalDate.of(2000, 2, 10))));
//    }
//
//    @DisplayName("Создание юзера с не датой")
//    @Test
//    void createUserWithoutData() {
//        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User("na@", "sdf",
//                "sd", LocalDate.of(2040, 2, 10))));
//    }
//
//    @DisplayName("Проверка вывода всех юзеров")
//    @Test
//    void getAllUsers() {
//        User create = inMemoryUserStorage.createUser(new User("na@", "fdg", "sd", LocalDate.of(2000, 2, 10)));
//        assertEquals(inMemoryUserStorage.getAllUser(), List.of(create));
//    }
//
//    @DisplayName("Проверка обновления юзиров")
//    @Test
//    void updateUser() {
//        User create = inMemoryUserStorage.createUser(new User("na@", "fdg", "sd", LocalDate.of(2000, 2, 10)));
//        inMemoryUserStorage.updateUser(new User(1, "na@na", "fdg", "sd", LocalDate.of(2000, 2, 10)));
//        assertEquals(inMemoryUserStorage.getAllUser(), List.of(new User(1, "na@na", "fdg", "sd", LocalDate.of(2000, 2, 10))));
//    }
}
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp(){
        userController = new UserController();
    }

    @DisplayName("Проверка создания юзиров")
    @Test
    void createUser() {
        User create = userController.createUser(new User("na@","fdg","sd", LocalDate.of(2000,2,10)));
        assertNotNull(create);
    }
    @DisplayName("Создание юзера с не верным именем")
    @Test
    void createUserWithoutName() {
        assertThrows(ValidationException.class,()-> userController.createUser(new User("na","fdg",
                "sd", LocalDate.of(2000,2,10))));
    }

    @DisplayName("Создание юзера с не верным именем")
    @Test
    void createUserWithoutLogin() {
        assertThrows(ValidationException.class,()-> userController.createUser(new User("na@","",
                "sd", LocalDate.of(2000,2,10))));
    }

    @DisplayName("Создание юзера с не датой")
    @Test
    void createUserWithoutData() {
        assertThrows(ValidationException.class,()-> userController.createUser(new User("na@","sdf",
                "sd", LocalDate.of(2040,2,10))));
    }

    @DisplayName("Проверка вывода всех юзеров")
    @Test
    void getAllUsers() {
        User create = userController.createUser(new User("na@","fdg", "sd",LocalDate.of(2000,2,10)));
        assertEquals(userController.getAllUser(), List.of(create));
    }

    @DisplayName("Проверка обновления юзиров")
    @Test
    void updateUser() {
        User create = userController.updateUser(new User("na@","fdg", "sd",LocalDate.of(2000,2,10)));
        assertEquals(userController.getAllUser(), List.of(create));
    }
}
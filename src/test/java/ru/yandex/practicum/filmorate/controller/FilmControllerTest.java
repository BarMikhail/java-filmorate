package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {


    private InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void setUp() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
    }

    @DisplayName("Обычное создание фильмов")
    @Test
    void createFilm() {
        Film create = inMemoryFilmStorage.createFilm(Film.builder().name("na").description("sasd").releaseDate(LocalDate.of(2000, 2, 10)).duration(50).build());
//        (new Film("na", "fdg", LocalDate.of(2000, 2, 10), 50));
        assertNotNull(create);
    }

//    @DisplayName("Создание фильмов без имени")
//    @Test
//    void createFilmWithoutName() {
//        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.createFilm(new Film("", "fdg",
//                LocalDate.of(2000, 2, 10), 50)));
//    }
//
//    @DisplayName("Создание фильма с неправильной датой")
//    @Test
//    void createFilmWithWrongDate() {
//        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.createFilm(new Film("ds", "fdg",
//                LocalDate.of(1800, 2, 10), 50)));
//    }
//
//    @DisplayName("Проверка вывода всех фильмов")
//    @Test
//    void getAllFilms() {
//        Film create = inMemoryFilmStorage.createFilm(new Film("na", "fdg", LocalDate.of(2000, 2, 10), 50));
//        assertEquals(inMemoryFilmStorage.getAllFilms(), List.of(create));
//    }
//
//    @DisplayName("Проверка обновления фильмов")
//    @Test
//    void updateFilm() {
//        Film create = inMemoryFilmStorage.createFilm(new Film("na", "fdg", LocalDate.of(2000, 2, 10), 50));
//        inMemoryFilmStorage.updateFilm(new Film(1, "asdff", "qwe", LocalDate.of(2000, 2, 10), 50));
//        assertEquals(inMemoryFilmStorage.getAllFilms(), List.of(new Film(1, "asdff", "qwe", LocalDate.of(2000, 2, 10), 50)));
//    }
}
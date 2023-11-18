package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    private Film film;
    private Film film1;


    private InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void setUp() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        film = Film.builder()
                .name("na")
                .description("sasd")
                .releaseDate(LocalDate.of(2000, 2, 10))
                .duration(50)
                .build();
        film1 = Film.builder()
                .name("Час пик")
                .description("Фильм с Джеки, что ещё нужно")
                .releaseDate(LocalDate.of(1999, 2, 10))
                .duration(98)
                .build();
    }

    @DisplayName("Обычное создание фильмов")
    @Test
    void createFilm() {
        assertNotNull(inMemoryFilmStorage.createFilm(film));
    }

    @DisplayName("Создание фильмов без имени")
    @Test
    void createFilmWithoutName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.createFilm(film));
    }

    @DisplayName("Создание фильма с неправильной датой")
    @Test
    void createFilmWithWrongDate() {
        film.setReleaseDate(LocalDate.of(1800, 2, 10));
        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.createFilm(film));
    }

    @DisplayName("Проверка вывода всех фильмов")
    @Test
    void getAllFilms() {
        inMemoryFilmStorage.createFilm(film);
        inMemoryFilmStorage.createFilm(film1);
        assertEquals(inMemoryFilmStorage.getAllFilms().size(), 2);
    }

    @DisplayName("Проверка обновления фильмов")
    @Test
    void updateFilm() {
        inMemoryFilmStorage.createFilm(film);
        inMemoryFilmStorage.updateFilm(Film.builder()
                .id(1)
                .name("Доспехи бога")
                .description("sasd")
                .releaseDate(LocalDate.of(2000, 2, 10))
                .duration(50)
                .build());
        assertEquals(inMemoryFilmStorage.getById(1).getName(),"Доспехи бога");
    }

    @DisplayName("Проверка вывода 1 фильма")
    @Test
    void getByIdFilms() {
        inMemoryFilmStorage.createFilm(film);
        inMemoryFilmStorage.createFilm(film1);
        assertEquals(inMemoryFilmStorage.getById(2), film1);
    }

    @DisplayName("Проверка удаления 1 фильмов")
    @Test
    void getDeleteFilms() {
        inMemoryFilmStorage.createFilm(film);
        inMemoryFilmStorage.createFilm(film1);
        assertEquals(inMemoryFilmStorage.getAllFilms().size(), 2);
        inMemoryFilmStorage.deleteFilm(1);
        assertEquals(inMemoryFilmStorage.getAllFilms().size(), 1);
    }
}
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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.FilmImpl;
import ru.yandex.practicum.filmorate.storage.impl.GenreImpl;
import ru.yandex.practicum.filmorate.storage.impl.LikeImpl;
import ru.yandex.practicum.filmorate.storage.impl.MPAImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserImpl;
import ru.yandex.practicum.filmorate.storage.impl.FriendImpl;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmServiceTest {
    private Film film;
    private Film film1;
    private User user;

    private final JdbcTemplate jdbcTemplate;
    private FilmService filmService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        filmService = new FilmService(new FilmImpl(jdbcTemplate), new LikeImpl(jdbcTemplate),
                new GenreImpl(jdbcTemplate), new MPAImpl(jdbcTemplate));
        userService = new UserService(new UserImpl(jdbcTemplate), new FriendImpl(jdbcTemplate));
        film = Film.builder()
                .id(1)
                .name("na")
                .description("sasd")
                .releaseDate(LocalDate.of(2000, 2, 10))
                .duration(50)
                .genres(Set.of(Genre.builder().id(1).name("Комедия").build()))
                .like(new HashSet<>())
                .mpa(MPA.builder().id(1).name("G").build())
                .build();
        film1 = Film.builder()
                .id(1)
                .name("Час пик")
                .description("Фильм с Джеки, что ещё нужно")
                .releaseDate(LocalDate.of(1999, 2, 10))
                .duration(98)
                .genres(Set.of(Genre.builder().id(1).name("Комедия").build(), Genre.builder().id(6).name("Боевик").build()))
                .mpa(MPA.builder().id(1).name("G").build())
                .like(new HashSet<>())
                .build();
        user = User.builder()
                .id(1)
                .email("na@s")
                .name("dss")
                .login("asd")
                .birthday(LocalDate.of(2000, 2, 10))
                .build();
    }

    @DisplayName("Обычное создание фильмов")
    @Test
    void createFilm() {
        assertNotNull(filmService.createFilm(film));
    }

    @DisplayName("Создание фильмов без имени")
    @Test
    void createFilmWithoutName() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @DisplayName("Создание фильма с неправильной датой")
    @Test
    void createFilmWithWrongDate() {
        film.setReleaseDate(LocalDate.of(1800, 2, 10));
        assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @DisplayName("Проверка вывода всех фильмов")
    @Test
    void getAllFilms() {
        filmService.createFilm(film);
        filmService.createFilm(film1);
        assertEquals(filmService.getAllFilms().size(), 2);
    }

    @DisplayName("Проверка обновления фильмов")
    @Test
    void updateFilm() {
        filmService.createFilm(film);
        filmService.updateFilm(Film.builder()
                .id(1)
                .name("Доспехи бога")
                .description("sasd")
                .releaseDate(LocalDate.of(2000, 2, 10))
                .duration(50)
                .genres(Set.of(Genre.builder().id(1).name("Комедия").build()))
                .mpa(MPA.builder().id(1).name("G").build())
                .build());
        assertEquals(filmService.getById(1).getName(), "Доспехи бога");
    }

    @DisplayName("Проверка вывода 1 фильма")
    @Test
    void getByIdFilms() {
        filmService.createFilm(film);
        filmService.createFilm(film1);
        assertEquals(filmService.getById(2), film1);
    }

    @DisplayName("Проверка удаления 1 фильмов")
    @Test
    void getDeleteFilms() {
        filmService.createFilm(film);
        filmService.createFilm(film1);
        assertEquals(filmService.getAllFilms().size(), 2);
        filmService.deleteFilm(1);
        assertEquals(filmService.getAllFilms().size(), 1);
    }


    @DisplayName("Проверка получения жанра фильма")
    @Test
    public void findGenreByFilmIdTest() {
        filmService.createFilm(film);
        assertEquals(filmService.getById(1).getGenres(), Set.of(Genre.builder().id(1).name("Комедия").build()));
    }


    @DisplayName("Проверка получения рейтинга фильма")
    @Test
    public void findMpaByFilmIdTest() {
        filmService.createFilm(film);
        assertEquals(filmService.getById(1).getMpa(), MPA.builder().id(1).name("G").build());
    }


    @DisplayName("Проверка создания лайка")
    @Test
    public void addLikesByFilmId() {
        filmService.createFilm(film);
        userService.createUser(user);
        filmService.addLike(1, 1);
        assertEquals(filmService.getById(1).getLike(), Set.of(1));
    }

    @DisplayName("Проверка удаление  лайка")
    @Test
    public void deleteLikesByFilmId() {
        filmService.createFilm(film);
        userService.createUser(user);
        filmService.addLike(1, 1);
        assertEquals(filmService.getById(1).getLike(), Set.of(1));
        filmService.deleteLike(1, 1);
        assertEquals(filmService.getById(1).getLike(), Set.of());
    }

    @DisplayName("Проверка добавления и вывода нескольких лайков")
    @Test
    public void getAllLikesTestByFilmId() {
        filmService.createFilm(film);
        userService.createUser(user);
        filmService.addLike(1, 1);
        assertEquals(filmService.getById(1).getLike(), Set.of(1));
        user = User.builder()
                .id(2)
                .email("qwer@s")
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2012, 5, 14))
                .build();
        userService.createUser(user);
        filmService.addLike(1, 2);
        assertEquals(filmService.getById(1).getLike(), Set.of(1, 2));
    }
}
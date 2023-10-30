package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int generateId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        film.setId(generateId++);
        if (film.getName().isBlank()) {
            log.warn("Name не может быть пустым");
            throw new ValidationException();
        } else if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания - 200 символов.");
            throw new ValidationException();
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            log.warn("Дата релиза - не раньше 28 декабря 1895 года.");
            throw new ValidationException();
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной.");
            throw new ValidationException();
        }
        log.info("Фильм добавлен");
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Все фильмы выведены");
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getName().isBlank()) {
            log.warn("Name не может быть пустым");
            throw new ValidationException();
        } else if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания - 200 символов.");
            throw new ValidationException();
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            log.warn("Дата релиза - не раньше 28 декабря 1895 года.");
            throw new ValidationException();
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной.");
            throw new ValidationException();
        }
        log.info("Фильм обновлен");
        films.put(film.getId(), film);
        return film;
    }
}

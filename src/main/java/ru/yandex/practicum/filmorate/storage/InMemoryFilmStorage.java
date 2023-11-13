package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int generateId = 1;

    @Override
    public List<Film> getAllFilms() {
        log.info("Все фильмы выведены");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);

        film.setId(generateId++);
        log.info("Фильм добавлен");
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Нет такого Id");
        }
        validate(film);
        log.info("Фильм обновлен");
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        return null;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Name пустой");
            throw new ValidationException("Проверь name");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Слишком большое описание");
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата выходит за пределы");
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность меньше 0");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}

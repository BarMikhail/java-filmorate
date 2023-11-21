package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
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
        log.info("Фильм {} добавлен", film.getName());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException(String.format("Id %s нет", film.getId()));
        }
        validate(film);
        films.get(film.getId()).setName(film.getName());
        films.get(film.getId()).setDescription(film.getDescription());
        films.get(film.getId()).setDuration(film.getDuration());
        films.get(film.getId()).setReleaseDate(film.getReleaseDate());
        log.info("Фильм {} обновлен", film.getName());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
        log.info("Фильм с {} удален", films.get(id).getName());
        films.remove(id);
    }

    @Override
    public Film getById(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
        return films.get(id);
    }


    private void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Name пустой");
            throw new ValidationException("Проверь name");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Слишком большое описание, {}", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата {} выходит за пределы", film.getReleaseDate());
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность меньше 0");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}

package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("film")
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
        Validate.validateFilm(film);
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
        Validate.validateFilm(film);
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
}

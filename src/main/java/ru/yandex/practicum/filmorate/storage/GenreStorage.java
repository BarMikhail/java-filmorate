package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    Genre getById(int id);

    List<Genre> getAllGenre();

    Set<Genre> getFilmGenre(int id);

    void addGenreToFilm(int id, Set<Genre> genres);

    void saveGenresByFilm(Film film);
}

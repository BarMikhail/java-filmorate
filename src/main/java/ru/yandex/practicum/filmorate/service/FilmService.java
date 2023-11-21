package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;

    public void addLike(int id, int otherId) {
        inMemoryFilmStorage.getById(id).addLike(otherId);
        log.debug("Лайк поставлен");
    }

    public void deleteLike(int id, int otherId) {
        inMemoryFilmStorage.getById(id).deleteLike(otherId);
        log.debug("Лайк удален");
    }

    public List<Film> getMostLikedFilms(int count) {
        log.debug("Вывод самых понравившихся фильмов");
        List<Film> films = new ArrayList<>(inMemoryFilmStorage.getAllFilms());
        return films.stream()
                .sorted((f1, f2) -> (f2.getLike().size() - f1.getLike().size()))
                .limit(count)
                .collect(Collectors.toList());
    }


    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void deleteFilm(int id) {
        inMemoryFilmStorage.deleteFilm(id);
    }

    public Film getById(int id) {
        return inMemoryFilmStorage.getById(id);
    }

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

}

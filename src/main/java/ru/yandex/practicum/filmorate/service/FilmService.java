package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.validate.Validate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    @Autowired
    public FilmService(@Qualifier("film2") FilmStorage filmStorage, @Qualifier("like") LikeStorage likeStorage,
                       GenreStorage genreStorage, MPAStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public void addLike(int id, int otherId) {
        likeStorage.addLike(id, otherId);
        log.info("Лайк фильма");
    }

    public void deleteLike(int id, int otherId) {
        Film film = getById(id);
        if (film.getLike().contains(otherId)) {
            likeStorage.deleteLike(id, otherId);
            log.info("Удаление лайка");
        } else {
            throw new NotFoundException(String.format("Пользователь с id %d не найден ", otherId));
        }
    }

    public List<Film> getMostLikedFilms(int count) {
        List<Film> films = likeStorage.getMostLikedFilms(count);
        log.info("Получение списка самых пролайканых фильмов");
        return RecordingGenreAndMPA(films);
    }

    public Film createFilm(Film film) {
        Validate.validateFilm(film);
        Film createFilm = filmStorage.createFilm(film);
        genreStorage.saveGenresByFilm(film);
        log.info("Создание фильма");
        return createFilm;
    }

    public Film updateFilm(Film film) {
        Validate.validateFilm(film);
        filmStorage.updateFilm(film);
        genreStorage.saveGenresByFilm(film);
        log.info("Обновление фильма");
        return getById(film.getId());
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
        log.info("Удаление фильма");
    }

    public Film getById(int id) {
        Film film = filmStorage.getById(id);
        film.setLike(likeStorage.getLikes(id));
        film.setGenres(genreStorage.getFilmGenre(id));
        log.info("Вывод определенного фильма");
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        for (Film film : films) {
            film.setGenres(genreStorage.getFilmGenre(film.getId()));
            film.setLike(likeStorage.getLikes(film.getId()));
        }
        log.info("Вывод всех фильмов");
        return RecordingGenreAndMPA(films);
    }

    private List<Film> RecordingGenreAndMPA(List<Film> films) {
        List<MPA> mpa = mpaStorage.getAllMPA();
        List<Film> fullFilms = new ArrayList<>();
        for (Film film : films) {
            int mpaId = film.getMpa().getId();
            MPA mpaRating = mpa.stream().filter(m -> m.getId().equals(mpaId)).findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format("Элемент c id %s не найден", mpaId)));
            film.setMpa(mpaRating);
            film.setGenres(genreStorage.getFilmGenre(film.getId()));
            fullFilms.add(film);
        }
        return fullFilms;
    }
}

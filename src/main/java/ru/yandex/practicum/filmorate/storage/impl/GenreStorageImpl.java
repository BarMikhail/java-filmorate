package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = ?",
                    GenreStorageImpl::mapRowToGenre, id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Жанр с id %d не найден", id));
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY genre_id", GenreStorageImpl::mapRowToGenre);
    }

    @Override
    public Set<Genre> getFilmGenre(int id) {
        log.info("Получение списка жанров для определенного фильма");
        return new HashSet<>(jdbcTemplate.query("SELECT g.genre_id, g.name " +
                "FROM genres AS g " +
                "LEFT JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ? ORDER BY fg.genre_id", GenreStorageImpl::mapRowToGenre, id));
    }

    @Override
    public void addGenreToFilm(int id, Set<Genre> genres) {
        if (!genres.isEmpty()) {
            genres.forEach(genre -> jdbcTemplate.update("MERGE INTO film_genre (genre_id, film_id) VALUES (?, ?)",
                    genre.getId(), id));
        }
    }

    @Override
    public void saveGenresByFilm(Film film) {
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            deleteAllGenreByFilm(film.getId());
            genres.forEach(genre -> addGenreToFilm(film.getId(), film.getGenres()));
        }
    }

    private void deleteAllGenreByFilm(long id) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", id);
    }


    private static Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return Genre.builder().id(id).name(name).build();
    }
}

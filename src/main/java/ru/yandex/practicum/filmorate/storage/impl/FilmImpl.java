package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component("film2")
@RequiredArgsConstructor
public class FilmImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(
                "select * from films f, mpa m where f.mpa_id = m.mpa_id", FilmImpl::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        if (getAllFilms().contains(film)) {
            throw new ValidationException("Фильм уже существует");
        } else {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("films")
                    .usingGeneratedKeyColumns("film_id");
            int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
            film.setId(id);
            return film;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (getAllFilms().stream().anyMatch(x -> x.getId() == film.getId())) {
            jdbcTemplate.update("UPDATE films SET " +
                            "name = ?, description = ?, release_date = ?, " +
                            "duration = ?, mpa_id = ? WHERE film_id = ?",
                    film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        } else {
            throw new NotFoundException(String.format("Фильм с таким id %d нет", film.getId()));
        }
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        if (getById(id) == null) {
            throw new NotFoundException(String.format("Id %s нет", id));
        }
        jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", id);
    }

    @Override
    public Film getById(Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT f.*, m.name " +
                    "FROM films AS f " +
                    "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "WHERE f.film_id = ?", FilmImpl::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("В таблице нет одной записи с id = %s", id));
        }
    }

    public static Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        MPA mpa = MPA.builder().id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa.name"))
                .build();
        Film film = Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();
        film.setId(id);
        film.setMpa(mpa);
        return film;
    }
}

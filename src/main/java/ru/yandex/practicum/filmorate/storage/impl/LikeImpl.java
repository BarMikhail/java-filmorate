package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("like")
@RequiredArgsConstructor
public class LikeImpl implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int id, int userId) {
        jdbcTemplate.update("INSERT INTO likes VALUES(?, ?)", id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", id, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(int count) {
        String sql = "SELECT f.film_id AS ID, f.name AS name, " +
                "f.description AS description, f.release_date AS release_date, " +
                "f.duration AS duration, f.mpa_id AS mpa_id, " +
                "COUNT(l.user_id) AS popular " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
                "GROUP BY ID ORDER BY popular DESC LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Film.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                        .duration(rs.getInt("duration"))
                        .mpa(MPA.builder().id(rs.getInt("mpa_id")).build())
                        .build(),
                count);
    }

    @Override
    public Set<Integer> getLikes(int filmId) {
        return new HashSet<>(jdbcTemplate.query(
                "SELECT user_id FROM likes  WHERE film_id =?", LikeImpl::mapRowToLike, filmId));
    }

    private static int mapRowToLike(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }

}

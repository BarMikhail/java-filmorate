package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MPAStorageImpl implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPA getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE mpa_id = ?",
                    MPAStorageImpl::mapRow, id);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Нет рейтинга с id %d", id));
        }
    }

    @Override
    public List<MPA> getAllMPA() {
        return jdbcTemplate.query("SELECT * FROM mpa ORDER BY mpa_id", MPAStorageImpl::mapRow);
    }

    private static MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MPA.builder().id(rs.getInt("mpa_id"))
                .name(rs.getString("name")).build();
    }
}

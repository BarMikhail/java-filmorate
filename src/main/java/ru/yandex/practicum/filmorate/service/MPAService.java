package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPAStorage mpaStorage;

    public MPA getById(int id) {
        log.info("Вывод оперделенного рейтинга");
        return mpaStorage.getById(id);
    }

    public List<MPA> getAll() {
        log.info("Вывод всех существующих рейтингов фильмов");
        return mpaStorage.getAllMPA();
    }
}

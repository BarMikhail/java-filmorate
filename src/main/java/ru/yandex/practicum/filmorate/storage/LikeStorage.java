package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface LikeStorage {

    void addLike(int id, int otherId);

    void deleteLike(int id, int otherId);

    List<Film> getMostLikedFilms(int count);

    Set<Integer> getLikes(int id);
}

package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> like = new HashSet<>();

//    public Film() {
//    }
//
//    public Film(String name, String description, LocalDate releaseDate, int duration) {
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//    }
//
//    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//    }

    public void addLike(Integer id) {
        like.add(id);
    }

    public void deleteLike(Integer id) {
        if (!like.contains(id)) {
            throw new NotFoundException(String.format("У фильма %s нечего убирать", getName()));
        }
        like.remove(id);
    }
}

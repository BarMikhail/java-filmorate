package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MPA mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Integer> like = new HashSet<>();

    public void addLike(Integer id) {
        like.add(id);
    }

    public void deleteLike(Integer id) {
        if (!like.contains(id)) {
            throw new NotFoundException(String.format("У фильма %s нечего убирать", getName()));
        }
        like.remove(id);
    }

    public Map<String, Object> toMap(){
        Map<String,Object> values = new HashMap<>();
        values.put("name",name);
        values.put("description",description);
        values.put("release_date",releaseDate);
        values.put("duration",duration);
        values.put("mpa_id",mpa.getId());
        return values;
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Date;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private Date releaseDate;
    private int duration;
}
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;


import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void setUp(){
        filmController = new FilmController();
    }

    @Test
    void createFilm() {
        Film create = filmController.createFilm(new Film());
        assertNotNull(create.getId());
    }

    @Test
    void getAllFilms() {
    }

    @Test
    void updateFilm() {
    }
}
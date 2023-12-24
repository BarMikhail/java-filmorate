package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validate {

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Email у пользователя с id {} пуст", user.getId());
            throw new ValidationException("Проверь Email");
        }
        if (!(user.getLogin() == null || user.getLogin().isBlank())) {
            String[] login = user.getLogin().split(" ");
            if (login.length > 1) {
                log.warn("У пользователя с id {} что-то не так с login", user.getId());
                throw new ValidationException("Проверь login");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения {} не может быть в будущем", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    public static void validateFilm(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Name пустой");
            throw new ValidationException("Проверь name");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Слишком большое описание, {}", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата {} выходит за пределы", film.getReleaseDate());
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность меньше 0");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}

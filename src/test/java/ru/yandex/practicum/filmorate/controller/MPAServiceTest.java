package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MPAServiceTest {
    private final MPAService mpaService;

    @Test
    public void testGetMpaById() {
        assertEquals("G", mpaService.getById(1).getName());
    }

    @Test
    public void testGetAllMpa() {
        List<MPA> mpa = new ArrayList<>();
        mpa.add(MPA.builder().id(1).name("G").build());
        mpa.add(MPA.builder().id(2).name("PG").build());
        mpa.add(MPA.builder().id(3).name("PG-13").build());
        mpa.add(MPA.builder().id(4).name("R").build());
        mpa.add(MPA.builder().id(5).name("NC-17").build());
        assertEquals(mpa, mpaService.getAll());
    }
}

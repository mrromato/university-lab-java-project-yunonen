package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Genre;
import com.example.bookcatalog.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreController genreController;

    @Test
    void getAllGenres_shouldReturnList() {
        when(genreRepository.findAll()).thenReturn(List.of(new Genre()));

        ResponseEntity<List<Genre>> response = genreController.getAllGenres();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllGenres_whenEmpty_shouldReturnEmptyList() {
        when(genreRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<Genre>> response = genreController.getAllGenres();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getGenreById_whenExists_shouldReturnGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Фантастика");
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        ResponseEntity<Genre> response = genreController.getGenreById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Фантастика", response.getBody().getName());
    }

    @Test
    void getGenreById_whenNotExists_shouldReturn404() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Genre> response = genreController.getGenreById(99L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void createGenre_shouldReturnSavedGenre() {
        Genre genre = new Genre();
        genre.setName("Sci-Fi");
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        ResponseEntity<Genre> response = genreController.createGenre(genre);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Sci-Fi", response.getBody().getName());
    }

    @Test
    void updateGenre_whenExists_shouldReturnUpdated() {
        Genre existing = new Genre();
        existing.setId(1L);
        existing.setName("Старый жанр");

        Genre details = new Genre();
        details.setName("Новый жанр");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(genreRepository.save(existing)).thenReturn(existing);

        ResponseEntity<Genre> response = genreController.updateGenre(1L, details);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Новый жанр", response.getBody().getName());
    }

    @Test
    void updateGenre_whenNotExists_shouldThrow() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        Genre details = new Genre();
        details.setName("Что-то");

        assertThrows(RuntimeException.class,
                () -> genreController.updateGenre(99L, details));
    }

    @Test
    void deleteGenre_shouldReturn204() {
        doNothing().when(genreRepository).deleteById(1L);

        ResponseEntity<Void> response = genreController.deleteGenre(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(genreRepository, times(1)).deleteById(1L);
    }
}

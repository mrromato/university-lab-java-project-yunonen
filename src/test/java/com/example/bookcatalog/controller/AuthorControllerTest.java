package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Author;
import com.example.bookcatalog.repository.AuthorRepository;
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
class AuthorControllerTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorController authorController;

    @Test
    void getAllAuthors_shouldReturnList() {
        when(authorRepository.findAll()).thenReturn(List.of(new Author()));

        ResponseEntity<List<Author>> response = authorController.getAllAuthors();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllAuthors_whenEmpty_shouldReturnEmptyList() {
        when(authorRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<Author>> response = authorController.getAllAuthors();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAuthorById_whenExists_shouldReturnAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Лев Толстой");
        author.setBiography("Великий русский писатель");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        ResponseEntity<Author> response = authorController.getAuthorById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Лев Толстой", response.getBody().getName());
    }

    @Test
    void getAuthorById_whenNotExists_shouldReturn404() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Author> response = authorController.getAuthorById(99L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void createAuthor_shouldReturnSavedAuthor() {
        Author author = new Author();
        author.setName("Stephen King");
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        ResponseEntity<Author> response = authorController.createAuthor(author);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Stephen King", response.getBody().getName());
    }

    @Test
    void updateAuthor_whenExists_shouldReturnUpdated() {
        Author existing = new Author();
        existing.setId(1L);
        existing.setName("Старое имя");
        existing.setBiography("Старая биография");

        Author details = new Author();
        details.setName("Новое имя");
        details.setBiography("Новая биография");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(authorRepository.save(existing)).thenReturn(existing);

        ResponseEntity<Author> response = authorController.updateAuthor(1L, details);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Новое имя", response.getBody().getName());
        assertEquals("Новая биография", response.getBody().getBiography());
    }

    @Test
    void updateAuthor_whenNotExists_shouldThrowException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        Author details = new Author();
        details.setName("Кто-то");

        assertThrows(RuntimeException.class,
                () -> authorController.updateAuthor(99L, details));
    }

    @Test
    void deleteAuthor_shouldReturn204() {
        doNothing().when(authorRepository).deleteById(1L);

        ResponseEntity<Void> response = authorController.deleteAuthor(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(authorRepository, times(1)).deleteById(1L);
    }
}

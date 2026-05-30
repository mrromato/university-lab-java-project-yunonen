package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Book;
import com.example.bookcatalog.service.BookService;
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
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    void getAllBooks_shouldReturnOkWithBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Евгений Онегин");
        when(bookService.getAllBooks()).thenReturn(List.of(book));

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Евгений Онегин", response.getBody().get(0).getTitle());
    }

    @Test
    void getBookById_whenExists_shouldReturnBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Отцы и дети");
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Отцы и дети", response.getBody().getTitle());
    }

    @Test
    void getBookById_whenNotExists_shouldReturn404() {
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookById(99L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void createBook_shouldReturnCreatedBook() {
        Book book = new Book();
        book.setTitle("Герой нашего времени");
        when(bookService.createBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.createBook(book);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Герой нашего времени", response.getBody().getTitle());
        verify(bookService, times(1)).createBook(any(Book.class));
    }

    @Test
    void updateBook_shouldReturnUpdatedBook() {
        Book updated = new Book();
        updated.setTitle("Новое название");
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updated);

        ResponseEntity<Book> response = bookController.updateBook(1L, updated);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Новое название", response.getBody().getTitle());
    }

    @Test
    void deleteBook_shouldReturn204() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(bookService, times(1)).deleteBook(1L);
    }
}
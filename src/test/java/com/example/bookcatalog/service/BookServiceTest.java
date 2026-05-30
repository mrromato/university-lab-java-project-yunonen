package com.example.bookcatalog.service;

import com.example.bookcatalog.domain.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Война и мир");
        Book book2 = new Book();
        book2.setTitle("Преступление и наказание");
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_whenExists_shouldReturnBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Мастер и Маргарита");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Мастер и Маргарита", result.get().getTitle());
    }

    @Test
    void getBookById_whenNotExists_shouldReturnEmpty() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createBook_shouldSaveAndReturnBook() {
        Book book = new Book();
        book.setTitle("Идиот");
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.createBook(book);

        assertNotNull(result);
        assertEquals("Идиот", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void updateBook_whenExists_shouldUpdateFields() {
        Book existing = new Book();
        existing.setId(1L);
        existing.setTitle("Старый заголовок");

        Book details = new Book();
        details.setTitle("Новый заголовок");
        details.setIsbn("978-0000000000");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(existing)).thenReturn(existing);

        Book result = bookService.updateBook(1L, details);

        assertEquals("Новый заголовок", result.getTitle());
        assertEquals("978-0000000000", result.getIsbn());
    }

    @Test
    void updateBook_whenNotExists_shouldThrowException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Book details = new Book();
        details.setTitle("Что-то");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookService.updateBook(99L, details));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void deleteBook_shouldCallRepository() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
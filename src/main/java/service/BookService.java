package com.example.bookcatalog.service;

import com.example.bookcatalog.domain.Book;
import com.example.bookcatalog.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        log.debug("Запрос списка всех книг");
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        log.debug("Запрос книги по id: {}", id);
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        log.info("Создание новой книги: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        log.info("Обновление книги с id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Книга не найдена с id: {}", id);
                    return new RuntimeException("Книга не найдена с id: " + id);
                });

        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setGenre(bookDetails.getGenre());
        book.setPublisher(bookDetails.getPublisher());
        book.setAuthors(bookDetails.getAuthors());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        log.info("Удаление книги с id: {}", id);
        bookRepository.deleteById(id);
    }
}
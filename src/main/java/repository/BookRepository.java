package com.example.bookcatalog.repository;

import com.example.bookcatalog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    // Поиск книг по названию (частичное совпадение без учета регистра)
    List<Book> findByTitleContainingIgnoreCase(String title);
}
package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Bookmark;
import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.repository.BookmarkRepository;
import com.example.bookcatalog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkRepository bookmarkRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<Bookmark>> getMyBookmarks(Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return ResponseEntity.ok(bookmarkRepository.findByUserId(currentUser.getId()));
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<Bookmark>> getMyBookmarksByStatus(
            @PathVariable String status,
            Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return ResponseEntity.ok(bookmarkRepository.findByUserIdAndStatus(currentUser.getId(), status));
    }


    @PostMapping("/book/{bookId}")
    public ResponseEntity<Bookmark> addBookmark(
            @PathVariable Long bookId,
            @RequestBody Bookmark bookmarkDetails,
            Authentication authentication) {

        Bookmark bookmark = new Bookmark();
        bookmark.setStatus(bookmarkDetails.getStatus());
        bookmark.setBook(bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + bookId)));

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        bookmark.setUser(currentUser);

        return ResponseEntity.ok(bookmarkRepository.save(bookmark));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> updateBookmark(
            @PathVariable Long id,
            @RequestBody Bookmark bookmarkDetails) {
        Bookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Закладка не найдена с id: " + id));
        bookmark.setStatus(bookmarkDetails.getStatus());
        return ResponseEntity.ok(bookmarkRepository.save(bookmark));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        bookmarkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
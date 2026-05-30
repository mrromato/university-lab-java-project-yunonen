package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Book;
import com.example.bookcatalog.domain.Bookmark;
import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.repository.BookmarkRepository;
import com.example.bookcatalog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookmarkControllerTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookmarkController bookmarkController;

    @Test
    void getMyBookmarks_shouldReturnList() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        User user = new User();
        user.setId(1L);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookmarkRepository.findByUserId(1L)).thenReturn(List.of(new Bookmark()));

        ResponseEntity<List<Bookmark>> response = bookmarkController.getMyBookmarks(auth);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void addBookmark_shouldReturnSaved() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        Bookmark bookmarkDetails = new Bookmark();
        bookmarkDetails.setStatus("READING");

        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmarkDetails);

        ResponseEntity<Bookmark> response = bookmarkController.addBookmark(1L, bookmarkDetails, auth);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("READING", response.getBody().getStatus());
    }

    @Test
    void addBookmark_whenBookNotFound_shouldThrow() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Bookmark bookmarkDetails = new Bookmark();
        bookmarkDetails.setStatus("READING");

        assertThrows(RuntimeException.class,
                () -> bookmarkController.addBookmark(99L, bookmarkDetails, auth));
    }

    @Test
    void updateBookmark_shouldReturnUpdated() {
        Bookmark existing = new Bookmark();
        existing.setStatus("READING");
        when(bookmarkRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(existing);

        Bookmark details = new Bookmark();
        details.setStatus("FINISHED");

        ResponseEntity<Bookmark> response = bookmarkController.updateBookmark(1L, details);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("FINISHED", response.getBody().getStatus());
    }

    @Test
    void updateBookmark_whenNotExists_shouldThrow() {
        when(bookmarkRepository.findById(99L)).thenReturn(Optional.empty());
        Bookmark details = new Bookmark();
        details.setStatus("FAVORITE");

        assertThrows(RuntimeException.class,
                () -> bookmarkController.updateBookmark(99L, details));
    }

    @Test
    void deleteBookmark_shouldReturn204() {
        doNothing().when(bookmarkRepository).deleteById(1L);

        ResponseEntity<Void> response = bookmarkController.deleteBookmark(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(bookmarkRepository, times(1)).deleteById(1L);
    }
}
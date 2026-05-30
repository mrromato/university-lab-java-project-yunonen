package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Book;
import com.example.bookcatalog.domain.Review;
import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.repository.ReviewRepository;
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
class ReviewControllerTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    void getReviewsByBook_shouldReturnList() {
        when(reviewRepository.findByBookId(1L)).thenReturn(List.of(new Review()));

        ResponseEntity<List<Review>> response = reviewController.getReviewsByBook(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void createReview_shouldReturnSavedReview() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));

        Review review = new Review();
        review.setText("Отличная книга!");
        review.setRating(5);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ResponseEntity<Review> response = reviewController.createReview(1L, review, auth);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Отличная книга!", response.getBody().getText());
    }

    @Test
    void createReview_whenBookNotFound_shouldThrow() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Review review = new Review();
        assertThrows(RuntimeException.class,
                () -> reviewController.createReview(99L, review, auth));
    }

    @Test
    void deleteReview_shouldReturn204() {
        doNothing().when(reviewRepository).deleteById(1L);

        ResponseEntity<Void> response = reviewController.deleteReview(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}

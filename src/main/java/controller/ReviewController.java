package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.Review;
import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.repository.ReviewRepository;
import com.example.bookcatalog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewRepository.findByBookId(bookId));
    }


    @PostMapping("/book/{bookId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Long bookId,
            @RequestBody Review reviewDetails,
            Authentication authentication) {

        Review review = new Review();
        review.setText(reviewDetails.getText());
        review.setRating(reviewDetails.getRating());
        review.setBook(bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + bookId)));

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        review.setUser(currentUser);

        return ResponseEntity.ok(reviewRepository.save(review));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
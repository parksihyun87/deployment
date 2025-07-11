package com.example.hibnb_project.data.dao;

import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.BookEntity;
import com.example.hibnb_project.data.entity.ReviewEntity;
import com.example.hibnb_project.data.entity.UserEntity;
import com.example.hibnb_project.data.repository.AccomRepository;
import com.example.hibnb_project.data.repository.BookRepository;
import com.example.hibnb_project.data.repository.ReviewRepository;
import com.example.hibnb_project.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewDAO {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final AccomRepository accomRepository;

    public void saveReview (Integer accomid, Integer bookid, String username, Double rating, String commnet, LocalDateTime createdAt) {

        AccomEntity accom = accomRepository.findById(accomid).orElse(null);
        if (accom == null) {
            throw new EntityNotFoundException("Accom not found");
        }
        BookEntity book = bookRepository.findById(bookid).orElse(null);
        if (book == null) {
            throw new EntityNotFoundException("Book not found");
        }
        UserEntity user = userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        ReviewEntity review = ReviewEntity.builder()
                .accomid(accom)
                .bookid(book)
                .username(user)
                .rating(rating)
                .comment(commnet)
                .createdAt(createdAt)
                .build();
        this.reviewRepository.save(review);
    }

    public void deleteReview(Integer reviewid) {
        if (reviewRepository.existsById(reviewid)) {
            reviewRepository.deleteById(reviewid);
            return;
        }
        throw new EntityNotFoundException("Review not found");
    }

}

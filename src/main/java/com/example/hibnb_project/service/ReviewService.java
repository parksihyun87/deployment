package com.example.hibnb_project.service;

import com.example.hibnb_project.data.dao.ReviewDAO;
import com.example.hibnb_project.data.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDAO reviewDAO;

    public void saveReview(ReviewDTO reviewDTO) {
        this.reviewDAO.saveReview(reviewDTO.getAccomid(), reviewDTO.getBookid(), reviewDTO.getUsername(),
                reviewDTO.getRating(), reviewDTO.getComment(), reviewDTO.getCreatedAt());
    }

    public void deleteReview(Integer reviewId) {
        this.reviewDAO.deleteReview(reviewId);
    }
}

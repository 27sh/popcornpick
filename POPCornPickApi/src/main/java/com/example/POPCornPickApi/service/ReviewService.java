package com.example.POPCornPickApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.POPCornPickApi.entity.Review;
import com.example.POPCornPickApi.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
    private ReviewRepository reviewRepository; // ReviewRepository 추가
	
	public double getAverageReviewScore(String title) {
        List<Integer> scores = reviewRepository.getReviewScoresByMovieTitle(title);
        if (scores.isEmpty()) {
            return 0.0;
        }
        double sum = scores.stream().mapToInt(Integer::intValue).sum();
        return sum / scores.size();
    }
	
	public List<Review> getReviewsByUsername(String username) {
        return reviewRepository.findByTicketing_Member_Username(username);
    }
}

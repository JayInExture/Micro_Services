package com.microService.Ratingservice.repositories;

import com.microService.Ratingservice.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByUserId(int userId);
    List<Rating> findByHotelId(int hotelId);
}

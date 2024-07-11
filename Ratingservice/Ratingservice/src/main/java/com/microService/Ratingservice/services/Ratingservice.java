package com.microService.Ratingservice.services;



import com.microService.Ratingservice.entities.Rating;

import java.util.List;

public interface Ratingservice {

    //create
    Rating create(Rating rating);


    //get all ratings
    List<Rating> getRatings();

    //get all by UserId
    List<Rating> getRatingByUserId(int userId);

    //get all by hotel
    List<Rating> getRatingByHotelId(int hotelId);
}

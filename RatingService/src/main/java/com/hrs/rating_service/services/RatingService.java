package com.hrs.rating_service.services;

import com.hrs.rating_service.entity.Rating;

import java.util.List;

public interface RatingService {

    /*create*/
    Rating createRating(Rating rating);
    /*get all ratings*/
    List<Rating> getAllRatings();
    /*get all by userId*/
    List<Rating> getRatingByUserId(String userId);
    /*get all by hotel*/
    List<Rating> getRatingByHotelId(String hotelId);
}

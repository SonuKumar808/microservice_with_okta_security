package com.hrs.hotel_service.service;

import com.hrs.hotel_service.entity.Hotel;

import java.util.List;

public interface HotelService {

    Hotel createHotel(Hotel hotel);
    Hotel getHotel(String id);
    List<Hotel> getAllHotels();
}

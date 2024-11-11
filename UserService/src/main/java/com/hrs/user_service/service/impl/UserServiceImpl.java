package com.hrs.user_service.service.impl;


import com.hrs.user_service.entity.Hotel;
import com.hrs.user_service.entity.Rating;
import com.hrs.user_service.entity.User;
import com.hrs.user_service.exceptions.ResourceNotFoundException;
import com.hrs.user_service.external_services.HotelService;
import com.hrs.user_service.repository.UserRepository;
import com.hrs.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RestTemplate restTemplate;
    private HotelService hotelService;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RestTemplate restTemplate, HotelService hotelService) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.hotelService = hotelService;
    }

    @Override
    public User saveUser(User user) {
        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        return userRepository.save(user);

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        /*get the user from database with the help of repository*/
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id not found!!" + userId));
        /*fetch ratings for the specific user coming RATING-SERVICE*/
        //url: http://localhost:8083/ratings/users/3626afc1-e23b-4a28-9eb2-1258cf99ccab
        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        logger.info("{} ", ratingsOfUser);

        List<Rating> list = Arrays.stream(ratingsOfUser).toList();
        List<Rating> ratingList = list.stream().map(rating -> {
//            api call to RATING SERVICE to get the hotel http://localhost:8082/hotels/4d9c9610-cd40-4073-b222-8c892e2d7cb0
//            set the hotel to the rating
//            return the rating
//            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
//            logger.info("response status code: {}", forEntity.getStatusCode());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;

    }

/*    @Override
    public User deleteUser(String userId) {
        return null;
    }

    @Override
    public User updateUser(String userId) {
        return null;
    }*/
}

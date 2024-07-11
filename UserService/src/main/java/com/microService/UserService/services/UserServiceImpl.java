package com.microService.UserService.services;

import com.microService.UserService.entities.Hotel;
import com.microService.UserService.entities.Rating;
import com.microService.UserService.entities.User;
import com.microService.UserService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements Userservice {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String RATING_SERVICE_URL = "http://RATINGSERVICE/ratings";
    private static final String HOTEL_SERVICE_URL = "http://HOTELSERVICE/hotels";
    @Override
    public User saveUser(User user) {
        // Save the user locally
        User savedUser = userRepository.save(user);

        // Save ratings
        user.getRatings().forEach(rating -> {
            // Save the hotel first and retrieve the hotelId
            Hotel hotel = rating.getHotel();
            Hotel savedHotel = restTemplate.postForObject(HOTEL_SERVICE_URL, hotel, Hotel.class);

            // Set the hotelId in the rating
            rating.setHotelId(savedHotel.getId());
            rating.setUserId(savedUser.getUserId());

            // Save the rating
            restTemplate.postForObject(RATING_SERVICE_URL, rating, Rating.class);
        });

        System.out.println("saved User in service:-" + savedUser);
        return savedUser;

//        User saveuser = userRepository.save(user);
//        return user;
    }

    @Override
    public List<User> getAllUser() {
        List<User> Alluser = userRepository.findAll();
        return Alluser;
    }

    @Override
    public User getUser(int userId) {
        //get user from database with the help  of user repository
//        Optional<User> user = userRepository.findById(userId);

        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElse(new User()); // Provide a default User if not found






        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
        System.out.println("ratings:--" + ratings);

        List<Rating> ratingList = ratings.stream().map(rating -> {

            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);

                Hotel hotel = forEntity.getBody();
                rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratings);



        return user;


//        Optional<User> optionalUser = userRepository.findById(userId);
//
//        if (optionalUser.isPresent()) {
//            return optionalUser.get();
//        } else {
//            return null;
//        }
    }

}

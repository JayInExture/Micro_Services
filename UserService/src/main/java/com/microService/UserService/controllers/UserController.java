package com.microService.UserService.controllers;

import com.microService.UserService.entities.User;
import com.microService.UserService.services.Userservice;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Userservice userservice;


//    Save User
    @PostMapping("/CreateUser")
    @CrossOrigin(origins = "http://localhost:7777")
    public ResponseEntity<User> createUser (@RequestBody User user){
        System.out.println("user rating and hotels"+user);
        User Saveduser = userservice.saveUser(user);
        System.out.println("saved User:-"+Saveduser);
        return ResponseEntity.status(HttpStatus.CREATED).body(Saveduser);
    }

    @GetMapping("/getUser/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable int userId){
       User user = userservice.getUser(userId);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> ratingHotelFallback(int userId, Exception ex) {
        ex.printStackTrace();

        User user = new User();
        user.setEmail("dummy@gmail.com");
        user.setName("Dummy");
        user.setAbout("This user is created dummy because some service is down");
        user.setUserId(141234);

        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }


    //get all user
    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUser = userservice.getAllUser();
        return ResponseEntity.ok(allUser);
    }

}

package com.microService.Ratingservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microService.Ratingservice.entities.Rating;
import com.microService.Ratingservice.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private RatingRepository ratingRepository; // Assuming you have a RatingRepository

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "rating_topic", groupId = "group_id")
    public void consume(String message) {
        try {
            // Deserialize the message to a Rating object
            Rating rating = objectMapper.readValue(message, Rating.class);

            // Save the rating to the database
            ratingRepository.save(rating);

            System.out.println("Consumed rating message and saved: " + rating);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Failed to deserialize rating message: " + message);
        }
    }
}



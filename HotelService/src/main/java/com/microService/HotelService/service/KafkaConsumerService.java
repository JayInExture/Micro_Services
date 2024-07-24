package com.microService.HotelService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microService.HotelService.entities.Hotel;
import com.microService.HotelService.entities.Rating;
import com.microService.HotelService.respositories.HotelRepository;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LogManager.getLogger(KafkaConsumerService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String RATING_TOPIC = "rating_topic";
    private static final String HOTEL_TOPIC = "hotel_topic";
    private static final String TOP_RATING_TOPIC = "top_rating_topic"; // New topic


    @PostConstruct
    public void startKafkaStream() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream(HOTEL_TOPIC);


        stream.foreach((key, value) -> {
            try {
                logger.info("Processing message from {} with key: {} and value: {}", HOTEL_TOPIC, key, value);

                Rating rating = objectMapper.readValue(value, Rating.class);

                    Hotel hotel = rating.getHotel();
                    Hotel savedHotel = hotelRepository.save(hotel);
                    rating.setHotelId(savedHotel.getId());

                    String ratingJson = serializeToJson(rating);
                    kafkaTemplate.send(RATING_TOPIC, ratingJson);
                if (rating.getRating() > 3) { // Check if rating is above 3
                    logger.info("Processed and forwarded message to {}: {}", RATING_TOPIC, ratingJson);
                    kafkaTemplate.send(TOP_RATING_TOPIC, ratingJson);
                    logger.info("Top Ratings---", TOP_RATING_TOPIC, ratingJson);
                } else {
                    logger.info("Rating below 4, not forwarding to {}: {}", RATING_TOPIC, value);
                }
//                Hotel hotel = rating.getHotel();
//                Hotel savedHotel = hotelRepository.save(hotel);
//                rating.setHotelId(savedHotel.getId());
//
//                String ratingJson = serializeToJson(rating);
//                kafkaTemplate.send(RATING_TOPIC, ratingJson);
//
//                logger.info("Processed and forwarded message to {}: {}", RATING_TOPIC, ratingJson);

            } catch (JsonProcessingException e) {
                logger.error("Failed to deserialize message: {}", value, e);
            }
        });


        KafkaStreams streams = new KafkaStreams(builder.build(), getStreamProperties());
        streams.start();
    }

    private Properties getStreamProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "hotel-service-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return props;
    }

    private String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing object to JSON: " + e.getMessage());
        }
    }
}






//package com.microService.HotelService.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.microService.HotelService.entities.Hotel;
//import com.microService.HotelService.respositories.HotelRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumerService {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private HotelRepository hotelRepository; // Assuming you have a HotelRepository
//
//    @KafkaListener(topics = "hotel_topic", groupId = "group_id")
//    public void consumeHotel(String message) {
//        try {
//            // Deserialize the message to a Hotel object
//            Hotel hotel = objectMapper.readValue(message, Hotel.class);
//
//            // Save the hotel to the database
//            hotelRepository.save(hotel);
//
//            System.out.println("Consumed and saved hotel message: " + hotel);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            System.err.println("Failed to deserialize hotel message: " + message);
//        }
//    }
//}
//

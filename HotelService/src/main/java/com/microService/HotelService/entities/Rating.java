package com.microService.HotelService.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {
    private int ratingId;
    private int userId;
    private int hotelId;
    private  int rating;
    private  String feedback;
    private Hotel hotel;

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", userId=" + userId +
                ", hotelId=" + hotelId +
                ", rating=" + rating +
                ", feedback='" + feedback + '\'' +
                ", hotel=" + hotel +
                '}';
    }
}
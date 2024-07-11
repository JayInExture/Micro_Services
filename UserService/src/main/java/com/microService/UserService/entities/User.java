package com.microService.UserService.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_service")
public class User {

    @Id
    @Column(name = "User_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int userId;

    @Column(name = "User_name")
    private  String name;

    @Column(name = "Email")
    private  String email;

    @Column(name = "About")
    private  String about;

    @Transient
    private List<Rating> ratings=new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", about='" + about + '\'' +
                ", ratings=" + ratings +
                '}';
    }


}
package org.example.servicelearningreporting.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "activityForms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityForm {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private LocalDateTime timestamp;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int totalHours;
    private String organizationName;
    private String contactName;
    private String city;
    private String email;
    private String phone;
    private String activityDescription;
    private double donations;
    private String imagePath;
    //Connected to a base user id

}

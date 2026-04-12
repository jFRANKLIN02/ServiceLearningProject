package org.example.servicelearningreporting.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "activity_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityForm {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private LocalDateTime timestamp;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String totalHours;
    private String organizationName;
    private String contactName;
    private String city;
    private String email;
    private String phone;
    private String activityDescription;
    private double donations;
    @ElementCollection
    private java.util.List<String> imagePaths;
    //new field
    private boolean submitted;

    //Instructor form specific fields
    private String program;
    private String participant;
    private String campus;
    private int quantity;

    //Student or Instructor indicator
    private String formType;

    //UserId
    private int userID;
}

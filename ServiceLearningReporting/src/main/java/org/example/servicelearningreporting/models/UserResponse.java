package org.example.servicelearningreporting.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    //Just userid and role? should i grab everything, then display name as a greeting
    private int userID;
    private String fName;
    private String lName;
    private String email;
    private boolean isGraduating;
    private String roleName;
    private String projectIP;
}

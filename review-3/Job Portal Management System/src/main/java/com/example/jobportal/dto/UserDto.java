package com.example.jobportal.dto;

import com.example.jobportal.entity.Role;
import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
    private Role role;
    private String phone;
    private String address;
    
    // Student specific
    private String skills;
    private String education;
    
    // Employer specific
    private String companyName;
    private String companyDescription;
    private String companyLocation;
    private String companyWebsite;
}

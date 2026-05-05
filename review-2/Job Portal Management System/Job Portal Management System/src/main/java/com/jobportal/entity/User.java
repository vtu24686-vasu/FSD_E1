package com.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String skills;
    private Integer experience;
    private String companyName;
    private String resumePath;

    // Extended student profile fields
    private String rollNumber;
    private String gender;
    private String fatherName;
    private String motherName;
    private LocalDate dateOfBirth;
    private String degree;
    private String branch;
    private String collegeName;
    private String mobileNumber;
    private String address;
    private Integer passingYear;
    private Double cgpa;

    @OneToMany(mappedBy = "employer")
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<JobApplication> applications = new ArrayList<>();
}

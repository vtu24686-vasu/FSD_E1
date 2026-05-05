package com.example.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String phone;
    private String address;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    // Additional fields for student
    private String resumePath;
    private String resumeFileName;
    @Column(length = 2000)
    private String parsedResume;
    private String skills;
    private String education;

    // Additional fields for employer
    private String companyName;
    @Column(length = 2000)
    private String companyDescription;
    private String companyLocation;
    private String companyWebsite;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private List<Job> postedJobs;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SavedJob> savedJobs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserFile> files;
}

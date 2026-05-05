package com.jobportal.config;

import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobApplication;
import com.jobportal.entity.Role;
import com.jobportal.entity.User;
import com.jobportal.repository.JobApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private static final int TARGET_COUNT = 6;

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDemoData() {
        return args -> {
            String[] skillSet = {"Java, Spring Boot, MySQL", "Python, Django, SQL", "React, JavaScript, CSS", "Node.js, Express, MongoDB", "DevOps, AWS, Docker"};
            String[] locations = {"Hyderabad", "Bengaluru", "Chennai", "Pune", "Mumbai", "Delhi"};

            // Ensure 6 employers.
            List<User> employers = new ArrayList<>(userRepository.findByRole(Role.EMPLOYER));
            for (int i = employers.size() + 1; i <= TARGET_COUNT; i++) {
                User employer = new User();
                employer.setName("Employer " + i);
                employer.setCompanyName("Tech Company " + i);
                employer.setEmail("employer" + i + "@jobpilot.com");
                employer.setPassword(passwordEncoder.encode("Employer@123"));
                employer.setRole(Role.EMPLOYER);
                employers.add(userRepository.save(employer));
            }

            // Keep demo employer for easy login.
            userRepository.findByEmail("employer.demo@jobpilot.com").orElseGet(() -> {
                User demo = new User();
                demo.setName("TalentBridge Pvt Ltd");
                demo.setCompanyName("TalentBridge Pvt Ltd");
                demo.setEmail("employer.demo@jobpilot.com");
                demo.setPassword(passwordEncoder.encode("Employer@123"));
                demo.setRole(Role.EMPLOYER);
                return userRepository.save(demo);
            });

            // Ensure 6 students.
            List<User> students = new ArrayList<>();
            for (int i = 1; i <= TARGET_COUNT; i++) {
                final int index = i;
                String email = "student" + i + "@jobpilot.com";
                User student = userRepository.findByEmail(email).orElseGet(() -> {
                    User s = new User();
                    s.setName("Student " + index);
                    s.setEmail(email);
                    s.setPassword(passwordEncoder.encode("Student@123"));
                    s.setRole(Role.STUDENT);
                    s.setSkills(skillSet[(index - 1) % skillSet.length]);
                    s.setExperience((index - 1) % 4);
                    return userRepository.save(s);
                });
                students.add(student);
            }

            // Ensure 6 jobs.
            List<Job> jobs = new ArrayList<>();
            if (jobRepository.count() < TARGET_COUNT) {
                for (int i = 1; i <= TARGET_COUNT; i++) {
                    Job job = new Job();
                    job.setTitle("Software Engineer " + i);
                    job.setDescription("Build scalable modules, write clean APIs, and collaborate with product teams.");
                    job.setSkillsRequired(skillSet[(i - 1) % skillSet.length]);
                    job.setSalary("INR " + (5 + i) + " LPA");
                    job.setLocation(locations[(i - 1) % locations.length]);
                    job.setMinExperience((i - 1) % 3);
                    job.setEmployer(employers.get((i - 1) % employers.size()));
                    jobs.add(job);
                }
                jobRepository.saveAll(jobs);
            }
            jobs = jobRepository.findAll().stream().limit(TARGET_COUNT).toList();

            // Ensure 6 applications.
            Random random = new Random();
            for (int i = 0; i < TARGET_COUNT; i++) {
                Job job = jobs.get(i % jobs.size());
                User student = students.get(i % students.size());
                if (applicationRepository.findByJobAndStudent(job, student).isPresent()) {
                    continue;
                }
                JobApplication app = new JobApplication();
                app.setJob(job);
                app.setStudent(student);
                app.setPhone("98765000" + i);
                app.setCity(locations[i % locations.length]);
                app.setHighestQualification("B.Tech");
                app.setSpecialization("Computer Science");
                app.setPassingYear(2024 + (i % 2));
                app.setCgpa(7.2 + (i * 0.2));
                app.setCoverLetter("I am interested in this role and have relevant project experience.");
                app.setStatus(switch (random.nextInt(3)) {
                    case 0 -> ApplicationStatus.APPLIED;
                    case 1 -> ApplicationStatus.SHORTLISTED;
                    default -> ApplicationStatus.REJECTED;
                });
                applicationRepository.save(app);
            }
        };
    }
}

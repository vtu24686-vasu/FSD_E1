package com.example.jobportal;

import com.example.jobportal.dto.UserDto;
import com.example.jobportal.entity.*;
import com.example.jobportal.repository.JobApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.NotificationRepository;
import com.example.jobportal.repository.SavedJobRepository;
import com.example.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final SavedJobRepository savedJobRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void run(String... args) {
        // Create default users
        createUser("Admin User", "admin@jobportal.com", "admin123", Role.ADMIN, null, null, null);
        User employer = createUser("TechCorp HR", "vasudasari827@gmail.com", "employer123", Role.EMPLOYER, null, null, "TechCorp");
        User employer2 = createUser("StartupXYZ", "dasarivasu827@gmail.com", "employer123", Role.EMPLOYER, null, null, "StartupXYZ");
        User employer3 = createUser("CloudNest Talent", "dasarivasu686@gmail.com", "employer123", Role.EMPLOYER, null, null, "CloudNest");
        User employer4 = createUser("FinEdge Recruiter", "careers@finedge.com", "employer123", Role.EMPLOYER, null, null, "FinEdge");
        enrichEmployer(employer, "Enterprise software company building reliable hiring, payroll, and workforce platforms.", "Bangalore, India", "https://techcorp.example.com");
        enrichEmployer(employer2, "Fast-growing startup focused on modern web products, analytics, and growth automation.", "Mumbai, India", "https://startupxyz.example.com");
        enrichEmployer(employer3, "Cloud consulting firm helping teams migrate, secure, and operate production infrastructure.", "Noida, India", "https://cloudnest.example.com");
        enrichEmployer(employer4, "Fintech product company delivering data-driven tools for finance and operations teams.", "Mumbai, India", "https://finedge.example.com");
        User student = createUser("Alice Johnson", "alice@student.com", "student123", Role.STUDENT, "Java, Spring Boot, SQL", "B.Tech Computer Science", null);
        User student2 = createUser("Bob Smith", "bob@student.com", "student123", Role.STUDENT, "React, Node.js, AWS", "B.Sc Software Engineering", null);
        User student3 = createUser("Neha Sharma", "neha@student.com", "student123", Role.STUDENT, "Python, Machine Learning, Spark", "M.Tech Data Science", null);
        User student4 = createUser("Rahul Verma", "rahul@student.com", "student123", Role.STUDENT, "Figma, UX Research, Product Design", "B.Des Interaction Design", null);
        enrichStudentResume(student, "Alice-Johnson-Resume.html", "/sample-resumes/alice-johnson.html");
        enrichStudentResume(student2, "Bob-Smith-Resume.html", "/sample-resumes/bob-smith.html");
        enrichStudentResume(student3, "Neha-Sharma-Resume.html", "/sample-resumes/neha-sharma.html");
        enrichStudentResume(student4, "Rahul-Verma-Resume.html", "/sample-resumes/rahul-verma.html");

        // Create sample jobs
        List<Job> jobs = new ArrayList<>();
        if (jobRepository.count() == 0) {
            addJobs(jobs, employer,
                job("Senior Java Developer", "Design and maintain high-performance Spring Boot services, improve API reliability, and mentor junior engineers.", "Bangalore, India", "Engineering", "3-5 Years", "Full-time", 1800000, 2800000),
                job("DevOps Engineer", "Manage CI/CD pipelines, AWS infrastructure, Kubernetes clusters, Terraform modules, and production observability.", "Hyderabad, India", "DevOps", "2-4 Years", "Full-time", 1400000, 2400000),
                job("Product Manager", "Own product discovery, prioritize roadmap work, coordinate engineering delivery, and turn customer insights into measurable releases.", "Remote", "Management", "4+ Years", "Remote", 2200000, 3500000),
                job("QA Automation Engineer", "Build Selenium and API automation suites, improve regression coverage, and partner with developers on release quality.", "Chennai, India", "Engineering", "1-3 Years", "Full-time", 800000, 1400000),
                job("Backend Intern", "Assist with Java APIs, SQL queries, unit tests, and internal tooling while learning production engineering practices.", "Bangalore, India", "Engineering", "0-1 Years", "Internship", 300000, 500000)
            );
            addJobs(jobs, employer2,
                job("Frontend React Developer", "Build responsive interfaces with React, TypeScript, reusable components, and REST API integrations.", "Mumbai, India", "Engineering", "1-3 Years", "Full-time", 900000, 1600000),
                job("Data Scientist", "Analyze large datasets, build predictive models with Python, and deploy ML experiments for business teams.", "Pune, India", "Data & AI", "2-5 Years", "Full-time", 1500000, 2600000),
                job("UX/UI Designer", "Create Figma prototypes, run usability tests, and collaborate with product managers on user-centered workflows.", "Remote", "Design", "1-2 Years", "Remote", 700000, 1300000),
                job("Growth Marketing Associate", "Plan acquisition campaigns, track funnel metrics, write landing page copy, and optimize paid channels.", "Delhi, India", "Marketing", "1-3 Years", "Full-time", 600000, 1100000),
                job("Inside Sales Executive", "Qualify inbound leads, run product demos, maintain CRM hygiene, and help convert mid-market prospects.", "Gurugram, India", "Sales", "0-2 Years", "Full-time", 500000, 900000)
            );
            addJobs(jobs, employer3,
                job("Cloud Solutions Architect", "Design cloud migrations, advise customers on secure architectures, and create scalable AWS and Azure reference patterns.", "Noida, India", "DevOps", "5+ Years", "Full-time", 2600000, 4200000),
                job("Site Reliability Engineer", "Improve service reliability, tune alerting, automate incident response, and reduce toil across distributed systems.", "Remote", "DevOps", "3-5 Years", "Remote", 1800000, 3000000),
                job("Security Analyst", "Monitor security events, investigate incidents, manage vulnerability reports, and support compliance readiness.", "Bangalore, India", "Engineering", "2-4 Years", "Full-time", 1100000, 1900000),
                job("Technical Support Engineer", "Troubleshoot customer issues, document solutions, and collaborate with engineering on product improvements.", "Kochi, India", "Engineering", "0-2 Years", "Full-time", 450000, 800000),
                job("HR Operations Specialist", "Coordinate onboarding, maintain employee records, support policy rollouts, and improve hiring operations.", "Pune, India", "HR", "2-4 Years", "Full-time", 550000, 950000)
            );
            addJobs(jobs, employer4,
                job("Finance Analyst", "Build forecasts, analyze monthly performance, prepare dashboards, and support business planning reviews.", "Mumbai, India", "Finance", "1-3 Years", "Full-time", 800000, 1500000),
                job("Business Analyst", "Map business requirements, write user stories, analyze data flows, and support Agile delivery teams.", "Bangalore, India", "Management", "2-4 Years", "Full-time", 1000000, 1800000),
                job("Mobile App Developer", "Develop Android and iOS features with React Native, integrate APIs, and improve app performance.", "Ahmedabad, India", "Engineering", "2-5 Years", "Full-time", 1200000, 2200000),
                job("Content Marketing Manager", "Own editorial calendars, publish SEO content, measure campaign performance, and refine brand messaging.", "Remote", "Marketing", "3-5 Years", "Remote", 1200000, 2000000),
                job("Recruitment Coordinator", "Schedule interviews, maintain candidate communication, update ATS records, and support hiring managers.", "Jaipur, India", "HR", "0-2 Years", "Contract", 350000, 650000)
            );

            createDashboardActivity(student, student2, student3, student4, jobs);
        }
        log.info("=== Sample data loaded. Login with: alice@student.com / student123  |  vasudasari827@gmail.com / employer123 ===");
    }

    private User createUser(String name, String email, String password, Role role, String skills, String education, String company) {
        try {
            UserDto dto = new UserDto();
            dto.setName(name); dto.setEmail(email); dto.setPassword(password); dto.setRole(role);
            dto.setSkills(skills); dto.setEducation(education); dto.setCompanyName(company);
            return userService.register(dto);
        } catch (Exception e) {
            return null;
        }
    }

    private void enrichEmployer(User employer, String description, String location, String website) {
        if (employer == null || employer.getCompanyDescription() != null) {
            return;
        }
        employer.setCompanyDescription(description);
        employer.setCompanyLocation(location);
        employer.setCompanyWebsite(website);
        userService.updateProfile(employer);
    }

    private void enrichStudentResume(User student, String fileName, String path) {
        if (student == null || student.getResumePath() != null) {
            return;
        }
        student.setResumeFileName(fileName);
        student.setResumePath(path);
        student.setParsedResume(student.getSkills());
        userService.updateProfile(student);
    }

    private JobSeed job(String title, String desc, String location, String category, String exp, String jobType, Integer minSalary, Integer maxSalary) {
        return new JobSeed(title, desc, location, category, exp, jobType, minSalary, maxSalary);
    }

    private void addJobs(List<Job> jobs, User employer, JobSeed... seeds) {
        if (employer == null) {
            return;
        }
        for (JobSeed seed : seeds) {
            Job job = createJob(seed, employer);
            if (job != null) {
                jobs.add(job);
            }
        }
    }

    private Job createJob(JobSeed seed, User employer) {
        try {
            Job job = Job.builder()
                .title(seed.title())
                .description(seed.description())
                .location(seed.location())
                .category(seed.category())
                .experienceLevel(seed.experienceLevel())
                .jobType(seed.jobType())
                .minSalary(seed.minSalary())
                .maxSalary(seed.maxSalary())
                .salary(formatSalary(seed.minSalary(), seed.maxSalary()))
                .deadline(LocalDate.now().plusDays(30 + jobsOffset(seed.title())))
                .employer(employer)
                .build();
            return jobRepository.save(job);
        } catch (Exception ignored) {}
        return null;
    }

    private String formatSalary(Integer minSalary, Integer maxSalary) {
        if (minSalary == null || maxSalary == null) {
            return "";
        }
        return "₹" + (minSalary / 100000) + "L - ₹" + (maxSalary / 100000) + "L";
    }

    private int jobsOffset(String title) {
        return Math.abs(title.hashCode() % 45);
    }

    private void createDashboardActivity(User student, User student2, User student3, User student4, List<Job> jobs) {
        if (jobs.size() < 8) {
            return;
        }
        createApplication(jobs.get(0), student, ApplicationStatus.SHORTLISTED, "I have built Spring Boot APIs and SQL-backed dashboards that match this role closely.", 2200000);
        createApplication(jobs.get(1), student, ApplicationStatus.UNDER_REVIEW, "I am comfortable with CI/CD, cloud deployments, and production troubleshooting.", 1700000);
        createApplication(jobs.get(5), student, ApplicationStatus.APPLIED, "My frontend experience includes React components, API integration, and responsive UI work.", 1200000);
        createApplication(jobs.get(7), student, ApplicationStatus.REJECTED, "I enjoy user research and can collaborate well with product and engineering teams.", 900000);
        createApplication(jobs.get(6), student3, ApplicationStatus.SELECTED, "My machine learning and Spark projects fit the data scientist responsibilities.", 2100000);
        createApplication(jobs.get(2), student2, ApplicationStatus.UNDER_REVIEW, "I can connect product planning with engineering delivery and customer feedback.", 2600000);
        createApplication(jobs.get(8), student4, ApplicationStatus.APPLIED, "I have campaign planning and content analytics experience for growth-focused roles.", 850000);

        saveJob(student, jobs.get(3));
        saveJob(student, jobs.get(10));
        saveJob(student, jobs.get(16));

        notify(student, "Application shortlisted", "Your Senior Java Developer application has been shortlisted.", "/dashboard", false);
        notify(student, "Interview update", "TechCorp is reviewing your DevOps Engineer profile.", "/dashboard", false);
        notify(student, "Saved job reminder", "QA Automation Engineer closes soon. Review it when you are ready.", "/jobs/" + jobs.get(3).getId(), true);
    }

    private void createApplication(Job job, User applicant, ApplicationStatus status, String coverLetter, Integer expectedSalary) {
        if (job == null || applicant == null) {
            return;
        }
        applicationRepository.findByJobAndApplicant(job, applicant).orElseGet(() ->
            applicationRepository.save(JobApplication.builder()
                .job(job)
                .applicant(applicant)
                .status(status)
                .resumePath(applicant.getResumePath())
                .coverLetter(coverLetter)
                .expectedSalary(expectedSalary)
                .availableFrom(LocalDate.now().plusDays(15))
                .interviewAt(status == ApplicationStatus.SHORTLISTED ? LocalDateTime.now().plusDays(5) : null)
                .interviewMode(status == ApplicationStatus.SHORTLISTED ? "Google Meet" : null)
                .interviewNotes(status == ApplicationStatus.SHORTLISTED ? "Technical screening round" : null)
                .build())
        );
    }

    private void saveJob(User user, Job job) {
        if (user == null || job == null || savedJobRepository.existsByUserAndJob(user, job)) {
            return;
        }
        savedJobRepository.save(SavedJob.builder().user(user).job(job).build());
    }

    private void notify(User user, String title, String message, String link, boolean read) {
        if (user == null) {
            return;
        }
        notificationRepository.save(Notification.builder()
            .user(user)
            .title(title)
            .message(message)
            .link(link)
            .read(read)
            .build());
    }

    private record JobSeed(String title, String description, String location, String category,
                           String experienceLevel, String jobType, Integer minSalary, Integer maxSalary) {}
}

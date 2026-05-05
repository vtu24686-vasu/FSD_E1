package com.jobportal.controller;

import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.security.core.Authentication auth, Model model) {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("totalJobs", jobService.allJobs().size());
        model.addAttribute("jobsApplied", applicationService.countByStudent(student.getId()));
        model.addAttribute("shortlisted", applicationService.countByStudentAndStatus(student.getId(), ApplicationStatus.SHORTLISTED));
        model.addAttribute("recentJobs", jobService.allJobs().stream().limit(6).toList());
        return "student/dashboard";
    }

    @GetMapping("/jobs")
    public String jobs(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("jobs", jobService.search(q));
        model.addAttribute("q", q == null ? "" : q);
        return "student/jobs";
    }

    @GetMapping("/apply/{jobId}")
    public String applyForm(@PathVariable Long jobId,
                            org.springframework.security.core.Authentication auth,
                            Model model) {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("job", jobService.getById(jobId));
        return "student/apply-form";
    }

    @PostMapping("/apply/{jobId}")
    public String apply(@PathVariable Long jobId,
                        org.springframework.security.core.Authentication auth,
                        @RequestParam String phone,
                        @RequestParam String city,
                        @RequestParam String highestQualification,
                        @RequestParam String specialization,
                        @RequestParam Integer passingYear,
                        @RequestParam Double cgpa,
                        @RequestParam String coverLetter,
                        @RequestParam("resumeFile") MultipartFile resumeFile) throws IOException {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        Job job = jobService.getById(jobId);

        String resumePath = null;
        if (!resumeFile.isEmpty()) {
            File dir = new File("src/main/resources/uploads/applications");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            resumePath = dir.getAbsolutePath() + File.separator + student.getId() + "_" + job.getId() + "_" + resumeFile.getOriginalFilename();
            resumeFile.transferTo(new File(resumePath));
        }

        applicationService.apply(job, student, phone, city, highestQualification, specialization, passingYear, cgpa, coverLetter, resumePath);
        return "redirect:/student/applied";
    }

    @GetMapping("/applied")
    public String applied(org.springframework.security.core.Authentication auth, Model model) {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("applications", applicationService.studentNotifications(student));
        return "student/applied";
    }

    @GetMapping("/notifications")
    public String notifications(org.springframework.security.core.Authentication auth, Model model) {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("applications", applicationService.studentNotifications(student));
        return "student/notifications";
    }

    @GetMapping("/profile")
    public String profile(org.springframework.security.core.Authentication auth, Model model) {
        model.addAttribute("student", userService.findByEmail(auth.getName()).orElseThrow());
        return "student/profile";
    }

    @GetMapping("/details")
    public String details(org.springframework.security.core.Authentication auth, Model model) {
        model.addAttribute("student", userService.findByEmail(auth.getName()).orElseThrow());
        return "student/details";
    }

    @PostMapping("/profile")
    public String updateProfile(org.springframework.security.core.Authentication auth,
                                @RequestParam String name,
                                @RequestParam String skills,
                                @RequestParam(required = false) Integer experience,
                                @RequestParam(required = false) String rollNumber,
                                @RequestParam(required = false) String gender,
                                @RequestParam(required = false) String fatherName,
                                @RequestParam(required = false) String motherName,
                                @RequestParam(required = false) LocalDate dateOfBirth,
                                @RequestParam(required = false) String degree,
                                @RequestParam(required = false) String branch,
                                @RequestParam(required = false) String collegeName,
                                @RequestParam(required = false) String mobileNumber,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) Integer passingYear,
                                @RequestParam(required = false) Double cgpa) {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        student.setName(name);
        student.setSkills(skills);
        student.setExperience(experience);
        student.setRollNumber(rollNumber);
        student.setGender(gender);
        student.setFatherName(fatherName);
        student.setMotherName(motherName);
        student.setDateOfBirth(dateOfBirth);
        student.setDegree(degree);
        student.setBranch(branch);
        student.setCollegeName(collegeName);
        student.setMobileNumber(mobileNumber);
        student.setAddress(address);
        student.setPassingYear(passingYear);
        student.setCgpa(cgpa);
        userService.save(student);
        return "redirect:/student/details";
    }

    @PostMapping("/resume")
    public String uploadResume(org.springframework.security.core.Authentication auth,
                               @RequestParam("file") MultipartFile file) throws IOException {
        User student = userService.findByEmail(auth.getName()).orElseThrow();
        if (!file.isEmpty()) {
            File dir = new File("src/main/resources/uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = dir.getAbsolutePath() + File.separator + student.getId() + "_" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            student.setResumePath(filePath);
            userService.save(student);
        }
        return "redirect:/student/profile";
    }
}

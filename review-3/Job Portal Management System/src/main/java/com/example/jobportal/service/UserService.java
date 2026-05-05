package com.example.jobportal.service;

import com.example.jobportal.dto.UserDto;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
        }
        User user = User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(dto.getRole())
            .phone(dto.getPhone())
            .address(dto.getAddress())
            .skills(dto.getSkills())
            .education(dto.getEducation())
            .companyName(dto.getCompanyName())
            .companyDescription(dto.getCompanyDescription())
            .companyLocation(dto.getCompanyLocation())
            .companyWebsite(dto.getCompanyWebsite())
            .build();
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public User updateProfile(User user) {
        return userRepository.save(user);
    }

    public java.util.List<User> findAll() {
        return userRepository.findAll();
    }

    public long countAll() {
        return userRepository.count();
    }

    public long countByRole(com.example.jobportal.entity.Role role) {
        return userRepository.findByRole(role).size();
    }

    public void toggleActive(Long id) {
        User user = findById(id);
        user.setActive(!user.isActive());
        userRepository.save(user);
    }
}

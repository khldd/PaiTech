package com.example.paitech.service;

import com.example.paitech.dto.request.CreateUserRequest;
import com.example.paitech.dto.response.UserResponse;
import com.example.paitech.exception.ResourceNotFoundException;
import com.example.paitech.model.Company;
import com.example.paitech.model.User;
import com.example.paitech.repository.CompanyRepository;
import com.example.paitech.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(CreateUserRequest req) {
        User u = new User();
        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole());
        if (req.getCompanyId() != null) {
            Company c = companyRepository.findById(req.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
            u.setCompany(c);
        }
        userRepository.save(u);
        return toDto(u);
    }

    public UserResponse get(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDto(u);
    }

    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    public UserResponse update(Long id, CreateUserRequest req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        u.setRole(req.getRole());
        if (req.getCompanyId() != null) {
            Company c = companyRepository.findById(req.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
            u.setCompany(c);
        } else {
            u.setCompany(null);
        }
        userRepository.save(u);
        return toDto(u);
    }

    public void delete(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(u);
    }

    private UserResponse toDto(User u) {
        Long companyId = (u.getCompany() != null) ? u.getCompany().getId() : null;
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), companyId);
    }
}

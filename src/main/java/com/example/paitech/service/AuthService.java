package com.example.paitech.service;

import com.example.paitech.dto.request.CreateUserRequest;
import com.example.paitech.dto.request.LoginRequest;
import com.example.paitech.dto.response.UserResponse;
import com.example.paitech.exception.ResourceNotFoundException;
import com.example.paitech.model.Company;
import com.example.paitech.model.User;
import com.example.paitech.repository.CompanyRepository;
import com.example.paitech.repository.UserRepository;
import com.example.paitech.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       UserRepository userRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    public UserResponse register(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadCredentialsException("Email already in use");
        }
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
        return toUserResponse(u);
    }

    public String login(LoginRequest req) {
        var auth = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        authenticationManager.authenticate(auth);
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        var principal = org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities("ROLE_" + u.getRole().name())
                .build();

        return jwtTokenProvider.generateToken(principal);
    }

    public User me(Principal principal) {
        if (principal == null) throw new BadCredentialsException("Not authenticated");
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    public UserResponse toUserResponse(User u) {
        Long companyId = (u.getCompany() != null) ? u.getCompany().getId() : null;
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), companyId);
    }
}


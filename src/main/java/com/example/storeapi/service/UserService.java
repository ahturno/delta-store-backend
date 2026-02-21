package com.example.storeapi.service;

import com.example.storeapi.entity.User;
import com.example.storeapi.repository.UserRepository;
import com.example.storeapi.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ Register User
    public User add(String email, String fullName,String rawPassword) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPasswordHash(PasswordEncoder.encode(rawPassword));


        return userRepository.save(user);
    }

    // ✅ Get User by Email
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Get User by ID
    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }

    public boolean checkByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}

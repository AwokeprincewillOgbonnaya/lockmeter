package com.university.passwordchecker.service;

import com.university.passwordchecker.dto.request.CreateUserRequest;
import com.university.passwordchecker.dto.request.LoginRequest;
import com.university.passwordchecker.dto.request.UpdateUserRequest;
import com.university.passwordchecker.dto.response.GenericResponse;
import com.university.passwordchecker.dto.response.LoginResponse;
import com.university.passwordchecker.dto.response.UserResponse;
import com.university.passwordchecker.exception.PasswordCheckException;
import com.university.passwordchecker.persistance.entity.User;
import com.university.passwordchecker.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public GenericResponse<UserResponse> createUser(CreateUserRequest request) {
        validateUserEmail(request.email());
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordCheckException("Password do not match", 400);
        }
        User user = new User();
        user.setUserName(request.userName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return new GenericResponse<>("User created successfully", UserResponse.fromEntity(user), 200);
    }

    @Transactional
    public GenericResponse<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        User user = findUserById(id);
        user.setUserName(request.userName());
        userRepository.save(user);
        return new GenericResponse<>("User updated successfully", UserResponse.fromEntity(user), 200);
    }

    @Transactional(readOnly = true)
    public GenericResponse<UserResponse> getUserById(Long id) {
        User user = findUserById(id);
        return new GenericResponse<>("User retrieved successfully", UserResponse.fromEntity(user), 200);
    }

    @Transactional(readOnly = true)
    public GenericResponse<UserResponse> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new PasswordCheckException("User with email: " + email + " not found", 404));
        return new GenericResponse<>("User retrieved Successfully", UserResponse.fromEntity(user), 200);
    }

    @Transactional
    public GenericResponse<UserResponse> deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
        return new GenericResponse<>("User deleted successfully", 200);
    }

    @Transactional(readOnly = true)
    public GenericResponse<LoginResponse> userLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new PasswordCheckException("Invalid email or password", 401));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordCheckException("Invalid email or password", 401);
        }
        return new GenericResponse<>("Login Successful", LoginResponse.fromEntity(user), 200);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new PasswordCheckException("User with id: " + id + " Not found", 404));
    }

    private void validateUserEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new PasswordCheckException("Email already exist: " + email, 400);
        }
    }
}

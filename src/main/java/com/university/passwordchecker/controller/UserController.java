package com.university.passwordchecker.controller;

import com.university.passwordchecker.dto.request.CreateUserRequest;
import com.university.passwordchecker.dto.request.LoginRequest;
import com.university.passwordchecker.dto.request.UpdateUserRequest;
import com.university.passwordchecker.dto.response.GenericResponse;
import com.university.passwordchecker.dto.response.LoginResponse;
import com.university.passwordchecker.dto.response.UserResponse;
import com.university.passwordchecker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserResponse>> register(@RequestBody @Valid CreateUserRequest request){
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(userService.userLogin(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<UserResponse>> getUserById(@PathVariable("id") Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    @GetMapping("")
    public ResponseEntity<GenericResponse<UserResponse>> getUserByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<UserResponse>> updateUser(@PathVariable("id") Long userId, @RequestBody @Valid UpdateUserRequest request){
        return ResponseEntity.ok(userService.updateUser(userId,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<UserResponse>> deleteUser (@PathVariable("id") Long userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}

package com.university.passwordchecker.controller;

import com.university.passwordchecker.dto.request.PasswordCheckRequest;
import com.university.passwordchecker.dto.response.GenericResponse;
import com.university.passwordchecker.dto.response.PasswordHistoryResponse;
import com.university.passwordchecker.dto.response.PasswordStrengthResponse;
import com.university.passwordchecker.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/check")
    public ResponseEntity<GenericResponse<PasswordStrengthResponse>> check (@RequestBody @Valid PasswordCheckRequest request){
        return ResponseEntity.ok(passwordService.passwordCheck(request));
    }
    @GetMapping("/generate")
    public ResponseEntity<GenericResponse<String >> generate(){
        return ResponseEntity.ok(passwordService.generatePassword());
    }
    @PostMapping("/save/{userId}")
    public ResponseEntity<GenericResponse<PasswordHistoryResponse>> savePassword(@PathVariable ("userId") Long userId, @RequestParam String generatedPassword){
        return ResponseEntity.ok(passwordService.savePassword(userId, generatedPassword));
    }
    @GetMapping("/history/{userId}")
    public ResponseEntity<GenericResponse<List<PasswordHistoryResponse>>> passwordHistory(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(passwordService.getPasswordHistory(userId));
    }
    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<GenericResponse<Void>> deletePassword(@PathVariable ("historyId") Long historyId){
        return ResponseEntity.ok(passwordService.deletePassword(historyId));
    }
    @DeleteMapping("/history/clear/{userId}")
    public ResponseEntity<GenericResponse<Void>> clearHistory(@PathVariable ("userId") Long userId ){
        return ResponseEntity.ok(passwordService.clearPasswordHistory(userId));
    }
}

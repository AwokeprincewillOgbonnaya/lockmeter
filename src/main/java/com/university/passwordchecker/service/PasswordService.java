package com.university.passwordchecker.service;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.university.passwordchecker.dto.request.PasswordCheckRequest;
import com.university.passwordchecker.dto.response.GenericResponse;
import com.university.passwordchecker.dto.response.PasswordHistoryResponse;
import com.university.passwordchecker.dto.response.PasswordStrengthResponse;
import com.university.passwordchecker.exception.PasswordCheckException;
import com.university.passwordchecker.persistance.entity.PasswordHistory;
import com.university.passwordchecker.persistance.entity.User;
import com.university.passwordchecker.persistance.repository.PasswordHistoryRepository;
import com.university.passwordchecker.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final Zxcvbn zxcvbn = new Zxcvbn();

    public GenericResponse<PasswordStrengthResponse> passwordCheck(PasswordCheckRequest request) {
        String password = request.password();
        boolean hasUpperCase = password.chars()
                .anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars()
                .anyMatch(Character::isLowerCase);
        boolean hasNumber = password.chars()
                .anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars()
                .anyMatch(c -> "!@#$%^&*()_+-=[]{}|;':\",./<>?".indexOf(c) >= 0);
        boolean hasMinLength = password.length() >= 8;

        Strength zxcvbnResult = zxcvbn.measure(password);
        int zxcvbnScore = zxcvbnResult.getScore();

        int compositionScore = 0;
        if (hasUpperCase) compositionScore++;
        if (hasLowerCase) compositionScore++;
        if (hasNumber) compositionScore++;
        if (hasSpecialChar) compositionScore++;
        if (hasMinLength) compositionScore++;

        int normalizedCompositionScore = (int)Math.round((compositionScore/5.0)*4);
        int finalScore = (int)Math.round((zxcvbnScore * 0.8) + (normalizedCompositionScore * 0.2));

        String strength;
        if (finalScore == 0) strength = "Very weak";
        else if (finalScore == 1) strength = "weak";
        else if (finalScore == 2) strength = "Fair";
        else if (finalScore == 3) strength = "Strong";
        else strength = "very strong";

        String zxcvbnWarning = zxcvbnResult.getFeedback().getWarning();

        String compositionFeedback = buildFeedBack(
                hasMinLength,
                hasUpperCase,
                hasLowerCase,
                hasNumber,
                hasSpecialChar
        );

        String finalFeedback;
        if (zxcvbnWarning != null && !zxcvbnWarning.isEmpty()) {
            finalFeedback = zxcvbnWarning + ". " + compositionFeedback;
        } else if (finalScore <= 2) {
            finalFeedback = "This password can be easily compromised despite meeting basic requirements. Try using random unrelated words or a generated password. " + compositionFeedback;
        } else {
            finalFeedback = compositionFeedback;
        }

        PasswordStrengthResponse response = PasswordStrengthResponse.builder()
                .strength(strength)
                .score(finalScore)
                .length(password.length())
                .upperCase(hasUpperCase)
                .lowerCase(hasLowerCase)
                .hasSpecialCharacter(hasSpecialChar)
                .hasNumber(hasNumber)
                .feedback(finalFeedback)
                .build();

        return new GenericResponse<>("Password analyzed successfully", response, 200);
    }


    public GenericResponse<String> generatePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "1234567890";
        String special = "!@#$%^&*()_+-=[]{}";
        String allChar = upperCase + numbers + lowerCase + special;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < 12; i++) {
            password.append(allChar.charAt(random.nextInt(allChar.length())));

        }
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        return new GenericResponse<>("Password Generated successfully", new String(passwordArray), 200);
    }

    @Transactional
    public GenericResponse<PasswordHistoryResponse> savePassword(Long id, String generatedPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new PasswordCheckException("User not found", 404));

        PasswordHistory history = new PasswordHistory();
        history.setGeneratedPassword(generatedPassword);
        history.setCreatedAt(LocalDateTime.now());
        history.setUser(user);

        PasswordHistory saved = passwordHistoryRepository.save(history);

        return new GenericResponse<>("Password saved successfully", PasswordHistoryResponse.fromEntity(saved), 201);

    }

    @Transactional(readOnly = true)
    public GenericResponse<List<PasswordHistoryResponse>> getPasswordHistory(
            Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new PasswordCheckException(
                        "User not found", 404));

        List<PasswordHistoryResponse> history = passwordHistoryRepository
                .findByUserId(userId)
                .stream()
                .map(PasswordHistoryResponse::fromEntity)
                .collect(Collectors.toList());

        return new GenericResponse<>(
                "Password history retrieved successfully",
                history,
                200
        );
    }

    @Transactional
    public GenericResponse<Void> deletePassword(Long historyId) {
        if (!passwordHistoryRepository.existsById(historyId)) {
            throw new PasswordCheckException("passwordHistory not found", 404);
        }
        passwordHistoryRepository.deleteById(historyId);
        return new GenericResponse<>("password deleted successfully", 200);
    }

    @Transactional
    public GenericResponse<Void> clearPasswordHistory(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new PasswordCheckException("User not found", 404));

        List<PasswordHistory> history = passwordHistoryRepository.findByUserId(userId);
        passwordHistoryRepository.deleteAll(history);

        return new GenericResponse<>("History cleared successfully", 200);
    }


    private String buildFeedBack(
            boolean hasMinLength,
            boolean hasUpperCase,
            boolean hasLowerCase,
            boolean hasNumber,
            boolean hasSpecialChar) {

        StringBuilder feedback = new StringBuilder();

        if (!hasMinLength)
            feedback.append("Password must be at least 8 characters. ");
        if (!hasUpperCase)
            feedback.append("Add uppercase letters (A-Z). ");
        if (!hasLowerCase)
            feedback.append("Add lowercase letters (a-z). ");
        if (!hasNumber)
            feedback.append("Add numbers (0-9). ");
        if (!hasSpecialChar)
            feedback.append("Add special characters (!@#$%). ");

        return feedback.length() > 0
                ? feedback.toString().trim()
                : "Great password! All requirements met.";
    }

}

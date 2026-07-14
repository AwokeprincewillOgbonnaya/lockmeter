package com.university.passwordchecker.persistance.repository;

import com.university.passwordchecker.persistance.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory,Long> {
List<PasswordHistory> findByUserId (Long id);
void deleteByUserId(Long Id);
}

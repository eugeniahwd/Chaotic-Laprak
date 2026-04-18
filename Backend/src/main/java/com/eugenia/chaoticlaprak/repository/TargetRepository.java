package com.eugenia.chaoticlaprak.repository;

import com.eugenia.chaoticlaprak.model.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TargetRepository extends JpaRepository<Target, Long> {
    List<Target> findByRole(String role);
}
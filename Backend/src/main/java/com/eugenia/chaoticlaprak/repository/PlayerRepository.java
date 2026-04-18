package com.eugenia.chaoticlaprak.repository;

import com.eugenia.chaoticlaprak.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);
}
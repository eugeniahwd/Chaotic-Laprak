package com.eugenia.chaoticlaprak.service;

import com.eugenia.chaoticlaprak.model.Player;
import com.eugenia.chaoticlaprak.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player register(String username, String password) {
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(password);
        return playerRepository.save(player);
    }

    public Optional<Player> login(String username, String password) {
        return playerRepository.findByUsername(username)
                .filter(p -> p.getPassword().equals(password));
    }
}
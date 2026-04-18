package com.eugenia.chaoticlaprak.controller;

import com.eugenia.chaoticlaprak.dto.SessionEndRequest;
import com.eugenia.chaoticlaprak.model.GameSession;
import com.eugenia.chaoticlaprak.model.Player;
import com.eugenia.chaoticlaprak.repository.PlayerRepository;
import com.eugenia.chaoticlaprak.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameSessionService gameSessionService;
    private final PlayerRepository playerRepository;

    @PostMapping("/start/{playerId}")
    public ResponseEntity<?> startSession(@PathVariable Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        GameSession session = gameSessionService.startSession(player);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/end")
    public ResponseEntity<?> endSession(@RequestBody SessionEndRequest request) {
        GameSession session = gameSessionService.endSession(
                request.getSessionId(),
                request.getDurationSeconds()
        );
        return ResponseEntity.ok(session);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<GameSession>> getLeaderboard() {
        return ResponseEntity.ok(gameSessionService.getLeaderboard());
    }
}
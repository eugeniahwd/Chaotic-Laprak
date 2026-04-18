package com.eugenia.chaoticlaprak.service;

import com.eugenia.chaoticlaprak.model.GameSession;
import com.eugenia.chaoticlaprak.model.Player;
import com.eugenia.chaoticlaprak.model.Target;
import com.eugenia.chaoticlaprak.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final TargetService targetService;

    public GameSession startSession(Player player) {
        List<Target> targets = targetService.getRandomTargets();
        String combination = targets.stream()
                .map(t -> String.valueOf(t.getId()))
                .collect(Collectors.joining(","));

        GameSession session = new GameSession();
        session.setPlayer(player);
        session.setStartTime(LocalDateTime.now());
        session.setTargetCombination(combination);
        return gameSessionRepository.save(session);
    }

    public GameSession endSession(Long sessionId, Long durationSeconds) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setEndTime(LocalDateTime.now());
        session.setDurationSeconds(durationSeconds);
        return gameSessionRepository.save(session);
    }

    public List<GameSession> getLeaderboard() {
        return gameSessionRepository.findTop10ByOrderByDurationSecondsAsc();
    }
}
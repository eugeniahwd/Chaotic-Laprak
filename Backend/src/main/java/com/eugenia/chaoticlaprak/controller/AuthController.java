package com.eugenia.chaoticlaprak.controller;

import com.eugenia.chaoticlaprak.dto.AuthRequest;
import com.eugenia.chaoticlaprak.model.Player;
import com.eugenia.chaoticlaprak.model.Target;
import com.eugenia.chaoticlaprak.repository.TargetRepository;
import com.eugenia.chaoticlaprak.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PlayerService playerService;
    private final TargetRepository targetRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        try {
            Player player = playerService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(player);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Username sudah dipakai!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return playerService.login(request.getUsername(), request.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/seed")
    public ResponseEntity<?> seedTargets() {
        if (targetRepository.count() == 0) {
            Target t1 = new Target(); t1.setName("Mr. Astha"); t1.setRole("dosen"); t1.setSpriteKey("astha");
            Target t2 = new Target(); t2.setName("Mrs. Riri"); t2.setRole("dosen"); t2.setSpriteKey("riri");
            Target t3 = new Target(); t3.setName("BN"); t3.setRole("aslab"); t3.setSpriteKey("bn");
            Target t4 = new Target(); t4.setName("NL"); t4.setRole("aslab"); t4.setSpriteKey("nl");
            Target t5 = new Target(); t5.setName("AF"); t5.setRole("aslab"); t5.setSpriteKey("af");
            targetRepository.saveAll(java.util.List.of(t1, t2, t3, t4, t5));
            return ResponseEntity.ok("Targets seeded!");
        }
        return ResponseEntity.ok("Already seeded!");
    }
}
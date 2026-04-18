package com.eugenia.chaoticlaprak;

public class PacingMovement implements MovementStrategy {
    private float range;
    private float speed;

    public PacingMovement(float range, float speed) {
        this.range = range;
        this.speed = speed;
    }

    @Override
    public void move(NPC npc, float delta) {
        npc.x += npc.direction * speed * delta;
        if (npc.x > npc.startX + range) npc.direction = -1;
        if (npc.x < npc.startX - range) npc.direction = 1;
    }
}

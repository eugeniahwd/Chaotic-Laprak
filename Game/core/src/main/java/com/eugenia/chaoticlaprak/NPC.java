package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class NPC {
    public float x, y;
    private float speed = 40f;
    float startX;
    private float range = 50f;
    int direction = 1;
    private ShapeRenderer shapeRenderer;
    public String name;
    public boolean isDosen;
    public boolean signed = false;
    private MovementStrategy movementStrategy;

    // Untuk dosen/aslab
    public MoodState moodState;

    public static final float SIZE = 32f;

    public NPC(float x, float y, String name, boolean isDosen) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.movementStrategy = new PacingMovement(50f, 40f);
        this.name = name;
        this.isDosen = isDosen;
        this.shapeRenderer = new ShapeRenderer();

        this.moodState = Math.random() < 0.5 ? new BadMoodState() : new GoodMoodState();
    }

    public boolean canSign() { return moodState.canSign(); }
    public String getBubbleText() {
        if (signed) return "Sudah dapat TTD!";
        return moodState.getBubbleText();
    }
    public void giveCemilan() { moodState = moodState.improve(); }

    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    public void update(float delta) {
        movementStrategy.move(this, delta);
    }

    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Dosen = biru, Aslab = hijau, NPC biasa = kuning
        if (isDosen) shapeRenderer.setColor(0, 0, 1, 1);
        else shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(x, y, SIZE, SIZE);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

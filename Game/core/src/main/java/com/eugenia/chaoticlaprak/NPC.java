package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NPC {
    public float x, y;
    public float startX;
    public int direction = 1;
    public String name;
    public boolean isDosen;
    public boolean signed = false;
    public MoodState moodState;
    private MovementStrategy movementStrategy;

    // Sprite
    private Texture spriteSheet;
    private TextureRegion currentFrame;
    private static final int FRAME_W = 20;
    private static final int FRAME_H = 32;

    // Fallback shape renderer kalau tidak ada sprite
    private ShapeRenderer shapeRenderer;
    private boolean hasSprite = false;

    public static final float SIZE = 32f;

    public NPC(float x, float y, String name, boolean isDosen) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.name = name;
        this.isDosen = isDosen;
        this.moodState = Math.random() < 0.5 ? new BadMoodState() : new GoodMoodState();
        this.movementStrategy = new PacingMovement(35f, 30f);
        this.shapeRenderer = new ShapeRenderer();

        // Load sprite berdasarkan nama
        String spritePath = getSpritePathForName(name);
        if (spritePath != null) {
            try {
                spriteSheet = new Texture(Gdx.files.internal(spritePath));
                // Default: frame pertama (baris 0 = menghadap bawah, kolom 1 = idle)
                currentFrame = new TextureRegion(spriteSheet, FRAME_W, 0, FRAME_W, FRAME_H);
                hasSprite = true;
            } catch (Exception e) {
                hasSprite = false;
            }
            System.out.println("Sprite loaded for " + name + ": " + hasSprite);
        }
    }

    private String getSpritePathForName(String name) {
        switch (name) {
            case "Mr. Astha": return "astha.png";
            case "Mrs. Riri": return "riri.png";
            case "BN": return "bn.png";
            case "NL": return "nl.png";
            case "AF": return "af.png";
            case "NPC1": return "npc.png";
            case "NPC2": return "npc2.png";
            default: return null;
        }
    }

    public void update(float delta) {
        movementStrategy.move(this, delta);

        // Update frame berdasarkan arah gerak
        if (hasSprite) {
            int col = 1; // kolom tengah = idle
            // baris: 0=bawah, 1=kiri, 2=kanan, 3=atas
            int row = direction == 1 ? 2 : 1; // kanan atau kiri
            currentFrame = new TextureRegion(spriteSheet,
                col * FRAME_W, row * FRAME_H, FRAME_W, FRAME_H);
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (hasSprite) {
            batch.draw(currentFrame, x, y, 12, 18);
        } else {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(isDosen ?
                new com.badlogic.gdx.graphics.Color(0, 0, 1, 1) :
                new com.badlogic.gdx.graphics.Color(0, 1, 0, 1));
            shapeRenderer.rect(x, y, SIZE, SIZE);
            shapeRenderer.end();
        }
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

    public void dispose() {
        if (spriteSheet != null) spriteSheet.dispose();
        shapeRenderer.dispose();
    }
}

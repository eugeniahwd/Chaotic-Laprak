package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Player {
    public float x, y;
    private float speed = 50f;
    private ShapeRenderer shapeRenderer;
    private Texture spriteSheet;
    private TextureRegion currentFrame;
    private static final int FRAME_W = 20;
    private static final int FRAME_H = 32;
    private boolean hasSprite = false;
    private int dirRow = 0;

    // Ukuran player
    public static final float SIZE = 10f;

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        shapeRenderer = new ShapeRenderer();
        try {
            spriteSheet = new Texture(Gdx.files.internal("player.png"));
            currentFrame = new TextureRegion(spriteSheet, FRAME_W, 0, FRAME_W, FRAME_H);
            hasSprite = true;
        } catch (Exception e) {
            hasSprite = false;
        }
    }

    public void update(float delta, TiledMapTileLayer blockedLayer) {
        float speed = GameManager.getInstance().getPlayerSpeed();
        float newX = x;
        float newY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) { newY += speed * delta; dirRow = 3; }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { newY -= speed * delta; dirRow = 0; }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { newX -= speed * delta; dirRow = 1; }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { newX += speed * delta; dirRow = 2; }

        if (!isBlocked(newX, y, blockedLayer)) x = newX;
        if (!isBlocked(x, newY, blockedLayer)) y = newY;

        // Update frame sprite
        if (hasSprite) {
            currentFrame = new TextureRegion(spriteSheet, FRAME_W, dirRow * FRAME_H, FRAME_W, FRAME_H);
        }
    }

    private boolean isBlocked(float x, float y, TiledMapTileLayer blockedLayer) {
        int tileX = (int)(x / 16); // sesuai tile size 16px
        int tileY = (int)(y / 16);
        TiledMapTileLayer.Cell cell = blockedLayer.getCell(tileX, tileY);
        return cell != null;
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (hasSprite) {
            batch.draw(currentFrame, x, y, 12, 18);
        } else {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.rect(x, y, SIZE, SIZE);
            shapeRenderer.end();
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

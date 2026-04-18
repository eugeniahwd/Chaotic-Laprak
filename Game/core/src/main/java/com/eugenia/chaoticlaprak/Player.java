package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Player {
    public float x, y;
    private float speed = 50f;
    private ShapeRenderer shapeRenderer;

    // Ukuran player (kotak placeholder dulu)
    public static final float SIZE = 10f;

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float delta, TiledMapTileLayer blockedLayer) {
        float speed = GameManager.getInstance().getPlayerSpeed();
        float newX = x;
        float newY = y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) newY += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) newY -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) newX -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) newX += speed * delta;

        if (!isBlocked(newX, y, blockedLayer)) x = newX;
        if (!isBlocked(x, newY, blockedLayer)) y = newY;
    }

    private boolean isBlocked(float x, float y, TiledMapTileLayer blockedLayer) {
        int tileX = (int)(x / 16); // sesuai tile size 16px
        int tileY = (int)(y / 16);
        TiledMapTileLayer.Cell cell = blockedLayer.getCell(tileX, tileY);
        return cell != null;
    }

    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1); // merah
        shapeRenderer.rect(x, y, SIZE, SIZE);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

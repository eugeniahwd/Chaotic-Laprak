package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Kantin {
    public float x, y;
    public static final float SIZE = 64f;
    private ShapeRenderer shapeRenderer;

    public Kantin(float x, float y) {
        this.x = x;
        this.y = y;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0.5f, 0, 1); // orange
        shapeRenderer.rect(x, y, SIZE, SIZE);
        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

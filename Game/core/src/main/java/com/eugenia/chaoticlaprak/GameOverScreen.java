package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    private final ChaoticLaprakGame game;
    private final String reason; // "waktu" atau "energi"
    private BitmapFont titleFont;
    private BitmapFont mainFont;
    private BitmapFont hintFont;
    private ShapeRenderer shape;
    private GlyphLayout layout;
    private float btnX, btnY, btnW = 220f, btnH = 42f;

    private static final Color BG         = new Color(0.22f, 0.17f, 0.11f, 1f);
    private static final Color WOOD_DARK  = new Color(0.35f, 0.22f, 0.10f, 1f);
    private static final Color WOOD_MID   = new Color(0.50f, 0.33f, 0.15f, 1f);
    private static final Color WOOD_LIGHT = new Color(0.65f, 0.45f, 0.22f, 1f);
    private static final Color WOOD_GRAIN = new Color(0.42f, 0.28f, 0.12f, 1f);
    private static final Color ACCENT     = new Color(0.50f, 0.72f, 0.22f, 1f);
    private static final Color YELLOW     = new Color(0.88f, 0.72f, 0.22f, 1f);
    private static final Color RED        = new Color(0.80f, 0.20f, 0.15f, 1f);
    private static final Color TEXT_MAIN  = new Color(0.95f, 0.88f, 0.70f, 1f);
    private static final Color TEXT_HINT  = new Color(0.65f, 0.58f, 0.40f, 1f);

    public GameOverScreen(ChaoticLaprakGame game, String reason) {
        this.game = game;
        this.reason = reason;
    }

    private void drawCentered(BitmapFont font, String text, float centerX, float y) {
        layout.setText(font, text);
        font.draw(game.batch, text, centerX - layout.width / 2f, y);
    }

    @Override
    public void show() {
        shape = new ShapeRenderer();
        layout = new GlyphLayout();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 20;
        param.color = RED;
        titleFont = generator.generateFont(param);

        param.size = 11;
        param.color = TEXT_MAIN;
        mainFont = generator.generateFont(param);

        param.size = 9;
        param.color = TEXT_HINT;
        hintFont = generator.generateFont(param);

        generator.dispose();
    }

    private void drawWoodPlank(float x, float y, float w, float h) {
        shape.setColor(WOOD_MID);
        shape.rect(x, y, w, h);
        shape.setColor(WOOD_LIGHT);
        shape.rect(x, y + h - 4, w, 4);
        shape.setColor(WOOD_GRAIN);
        shape.rect(x, y + h * 0.6f, w, 2);
        shape.rect(x, y + h * 0.3f, w, 2);
        shape.setColor(WOOD_DARK);
        shape.rect(x, y, w, 4);
    }

    private void drawWoodBorder(float x, float y, float w, float h, float thickness) {
        drawWoodPlank(x, y + h - thickness, w, thickness);
        drawWoodPlank(x, y, w, thickness);
        drawWoodPlank(x, y, thickness, h);
        drawWoodPlank(x + w - thickness, y, thickness, h);
    }

    private void drawPixelNail(float x, float y) {
        shape.setColor(WOOD_DARK);
        shape.rect(x - 4, y - 4, 8, 8);
        shape.setColor(new Color(0.75f, 0.65f, 0.45f, 1f));
        shape.rect(x - 2, y - 2, 4, 4);
    }

    @Override
    public void render(float delta) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float cx = w / 2f;

        ScreenUtils.clear(BG.r, BG.g, BG.b, 1);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Grid background
        shape.setColor(0.19f, 0.15f, 0.09f, 1f);
        for (int i = 0; i < w; i += 32) shape.rect(i, 0, 1, h);
        for (int j = 0; j < h; j += 32) shape.rect(0, j, w, 1);

        // Header
        drawWoodPlank(0, h - 75, w, 75);
        shape.setColor(RED);
        shape.rect(0, h - 78, w, 3);
        drawPixelNail(40, h - 37);
        drawPixelNail(w - 40, h - 37);
        drawPixelNail(cx, h - 37);

        // Footer
        drawWoodPlank(0, 0, w, 55);
        shape.setColor(ACCENT);
        shape.rect(0, 55, w, 3);
        drawPixelNail(40, 27);
        drawPixelNail(w - 40, 27);

        // Panel
        float panelW = 440f;
        float panelH = 300f;
        float panelX = cx - panelW / 2f;
        float panelY = h / 2f - panelH / 2f;

        shape.setColor(0.10f, 0.07f, 0.04f, 1f);
        shape.rect(panelX + 4, panelY - 4, panelW, panelH);
        shape.setColor(0.28f, 0.19f, 0.10f, 1f);
        shape.rect(panelX, panelY, panelW, panelH);
        drawWoodBorder(panelX, panelY, panelW, panelH, 12);
        drawPixelNail(panelX, panelY + panelH);
        drawPixelNail(panelX + panelW, panelY + panelH);
        drawPixelNail(panelX, panelY);
        drawPixelNail(panelX + panelW, panelY);

        // Divider
        shape.setColor(WOOD_GRAIN);
        shape.rect(panelX + 20, panelY + panelH * 0.45f, panelW - 40, 3);
        shape.setColor(WOOD_LIGHT);
        shape.rect(panelX + 20, panelY + panelH * 0.45f + 3, panelW - 40, 1);

        // Tombol COBA LAGI
        btnX = cx - btnW / 2f;
        btnY = panelY + 18;
        shape.setColor(RED);
        shape.rect(btnX, btnY, btnW, btnH);
        drawWoodBorder(btnX, btnY, btnW, btnH, 6);
        drawPixelNail(btnX, btnY);
        drawPixelNail(btnX + btnW, btnY);
        drawPixelNail(btnX, btnY + btnH);
        drawPixelNail(btnX + btnW, btnY + btnH);

        // Dekorasi daun
        shape.setColor(ACCENT);
        shape.rect(22, h - 68, 8, 8);
        shape.rect(30, h - 60, 8, 8);
        shape.rect(14, h - 60, 8, 8);
        shape.rect(22, h - 52, 8, 16);
        shape.setColor(new Color(0.35f, 0.55f, 0.15f, 1f));
        shape.rect(w - 38, h - 68, 8, 8);
        shape.rect(w - 46, h - 60, 8, 8);
        shape.rect(w - 30, h - 60, 8, 8);
        shape.rect(w - 38, h - 52, 8, 16);

        shape.end();

        // Handle input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            GameManager.getInstance().reset();
            game.setScreen(new MenuScreen(game));
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = h - Gdx.input.getY();
            if (mx >= btnX && mx <= btnX + btnW &&
                my >= btnY && my <= btnY + btnH) {
                GameManager.getInstance().reset();
                game.setScreen(new MenuScreen(game));
            }
        }

        // Teks
        game.batch.begin();

        drawCentered(titleFont, "CHAOTIC LAPRAK", cx, h - 25);

        titleFont.setColor(RED);
        drawCentered(titleFont, "GAME OVER!", cx, panelY + panelH - 20);
        titleFont.setColor(RED);

        float topSection = panelY + panelH * 0.45f + 15;
        mainFont.setColor(TEXT_MAIN);

        if (reason.equals("waktu")) {
            drawCentered(mainFont, "waktu habis!", cx, topSection + 70);
            drawCentered(mainFont, "laprak belum ditandatangani...", cx, topSection + 45);
        } else {
            drawCentered(mainFont, "energi habis!", cx, topSection + 70);
            drawCentered(mainFont, "kamu pingsan kelelahan...", cx, topSection + 45);
        }

        hintFont.setColor(TEXT_HINT);
        drawCentered(hintFont, "TTD terkumpul: " + GameManager.getInstance().progress + "/3",
            cx, topSection + 15);

        mainFont.setColor(WOOD_DARK);
        drawCentered(mainFont, "COBA LAGI", cx, btnY + btnH - 14);
        mainFont.setColor(TEXT_MAIN);

        hintFont.setColor(TEXT_HINT);
        drawCentered(hintFont, "[ENTER] atau [R] untuk coba lagi", cx, btnY - 10);
        drawCentered(hintFont, "Chaotic Laprak  -  Netlab OOP 2026", cx, 38);

        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        titleFont.dispose();
        mainFont.dispose();
        hintFont.dispose();
        shape.dispose();
    }
}

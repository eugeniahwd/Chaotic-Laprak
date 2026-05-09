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

public class MenuScreen implements Screen {
    private final ChaoticLaprakGame game;
    private BitmapFont titleFont;
    private BitmapFont inputFont;
    private BitmapFont hintFont;
    private ShapeRenderer shape;

    private StringBuilder username = new StringBuilder();
    private StringBuilder password = new StringBuilder();
    private boolean typingUsername = true;
    private String message = "";
    private float time = 0f;
    private GlyphLayout layout = new GlyphLayout();

    private float cursorTimer = 0f;
    private boolean showCursor = true;

    // Palet warna earthy pixel
    private static final Color BG          = new Color(0.22f, 0.17f, 0.11f, 1f); // coklat gelap
    private static final Color WOOD_DARK   = new Color(0.35f, 0.22f, 0.10f, 1f); // kayu gelap
    private static final Color WOOD_MID    = new Color(0.50f, 0.33f, 0.15f, 1f); // kayu medium
    private static final Color WOOD_LIGHT  = new Color(0.65f, 0.45f, 0.22f, 1f); // kayu terang
    private static final Color WOOD_GRAIN  = new Color(0.42f, 0.28f, 0.12f, 1f); // serat kayu
    private static final Color ACCENT      = new Color(0.50f, 0.72f, 0.22f, 1f); // hijau earthy
    private static final Color YELLOW      = new Color(0.88f, 0.72f, 0.22f, 1f); // kuning hangat
    private static final Color TEXT_MAIN   = new Color(0.95f, 0.88f, 0.70f, 1f); // krem
    private static final Color TEXT_HINT   = new Color(0.65f, 0.58f, 0.40f, 1f); // krem redup

    public MenuScreen(ChaoticLaprakGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        shape = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 20;
        param.color = YELLOW;
        titleFont = generator.generateFont(param);

        param.size = 11;
        param.color = TEXT_MAIN;
        inputFont = generator.generateFont(param);

        param.size = 9;
        param.color = TEXT_HINT;
        hintFont = generator.generateFont(param);

        generator.dispose();
    }

    private void drawWoodPlank(float x, float y, float w, float h) {
        // Base kayu
        shape.setColor(WOOD_MID);
        shape.rect(x, y, w, h);
        // Serat kayu atas
        shape.setColor(WOOD_LIGHT);
        shape.rect(x, y + h - 4, w, 4);
        // Serat kayu tengah
        shape.setColor(WOOD_GRAIN);
        shape.rect(x, y + h * 0.6f, w, 2);
        shape.rect(x, y + h * 0.3f, w, 2);
        // Shadow bawah
        shape.setColor(WOOD_DARK);
        shape.rect(x, y, w, 4);
    }

    private void drawWoodBorder(float x, float y, float w, float h, float thickness) {
        // Atas
        drawWoodPlank(x, y + h - thickness, w, thickness);
        // Bawah
        drawWoodPlank(x, y, w, thickness);
        // Kiri
        drawWoodPlank(x, y, thickness, h);
        // Kanan
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
        time += delta;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        ScreenUtils.clear(BG.r, BG.g, BG.b, 1);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // ── Background texture pixel (grid gelap) ──
        shape.setColor(0.19f, 0.15f, 0.09f, 1f);
        for (int i = 0; i < w; i += 32) {
            shape.rect(i, 0, 1, h);
        }
        for (int j = 0; j < h; j += 32) {
            shape.rect(0, j, w, 1);
        }

        // ── Header papan kayu ──
        drawWoodPlank(0, h - 75, w, 75);
        shape.setColor(ACCENT);
        shape.rect(0, h - 78, w, 3);
        // Paku di header
        drawPixelNail(40, h - 37);
        drawPixelNail(w - 40, h - 37);
        drawPixelNail(w / 2f, h - 37);

        // ── Footer papan kayu ──
        drawWoodPlank(0, 0, w, 55);
        shape.setColor(ACCENT);
        shape.rect(0, 55, w, 3);
        drawPixelNail(40, 27);
        drawPixelNail(w - 40, 27);

        // ── Panel utama (papan kayu dengan border) ──
        // Shadow panel
        shape.setColor(0.10f, 0.07f, 0.04f, 1f);
        shape.rect(w / 2f - 205, h / 2f - 155, 414, 264);
        // Background panel
        shape.setColor(0.28f, 0.19f, 0.10f, 1f);
        shape.rect(w / 2f - 208, h / 2f - 158, 416, 266);
        // Border kayu panel
        drawWoodBorder(w / 2f - 208, h / 2f - 158, 416, 266, 12);
        // Paku di sudut panel
        drawPixelNail(w / 2f - 208, h / 2f + 108);
        drawPixelNail(w / 2f + 208, h / 2f + 108);
        drawPixelNail(w / 2f - 208, h / 2f - 158);
        drawPixelNail(w / 2f + 208, h / 2f - 158);

        // ── Field username ──
        // Background field
        shape.setColor(0.18f, 0.13f, 0.07f, 1f);
        shape.rect(w / 2f - 180, h / 2f + 20, 360, 36);
        // Border field aktif/nonaktif
        if (typingUsername) {
            shape.setColor(ACCENT);
        } else {
            shape.setColor(WOOD_LIGHT);
        }
        shape.rect(w / 2f - 180, h / 2f + 20, 360, 2);
        shape.rect(w / 2f - 180, h / 2f + 54, 360, 2);
        shape.rect(w / 2f - 180, h / 2f + 20, 2, 36);
        shape.rect(w / 2f + 178, h / 2f + 20, 2, 36);

        // ── Field password ──
        shape.setColor(0.18f, 0.13f, 0.07f, 1f);
        shape.rect(w / 2f - 180, h / 2f - 50, 360, 36);
        if (!typingUsername) {
            shape.setColor(ACCENT);
        } else {
            shape.setColor(WOOD_LIGHT);
        }
        shape.rect(w / 2f - 180, h / 2f - 50, 360, 2);
        shape.rect(w / 2f - 180, h / 2f - 16, 360, 2);
        shape.rect(w / 2f - 180, h / 2f - 50, 2, 36);
        shape.rect(w / 2f + 178, h / 2f - 50, 2, 36);

        // ── Divider kayu di tengah panel ──
        shape.setColor(WOOD_GRAIN);
        shape.rect(w / 2f - 180, h / 2f - 5, 360, 3);
        shape.setColor(WOOD_LIGHT);
        shape.rect(w / 2f - 180, h / 2f - 2, 360, 1);

        // ── Dekorasi daun pixel di pojok ──
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

        // Tombol START
        boolean hoverStart = !typingUsername && password.length() > 0;
        shape.setColor(hoverStart ? ACCENT : WOOD_MID);
        shape.rect(w / 2f - 80, h / 2f - 130, 160, 40);
        drawWoodBorder(w / 2f - 80, h / 2f - 130, 160, 40, 6);
        drawPixelNail(w / 2f - 80, h / 2f - 130);
        drawPixelNail(w / 2f + 80, h / 2f - 130);
        drawPixelNail(w / 2f - 80, h / 2f - 90);
        drawPixelNail(w / 2f + 80, h / 2f - 90);

        shape.end();

        // ── Handle input ──
        // Cursor kedip
        cursorTimer += delta;
        if (cursorTimer >= 0.5f) {
            showCursor = !showCursor;
            cursorTimer = 0f;
        }

        // Pindah field
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            typingUsername = !typingUsername;
            showCursor = true;
            cursorTimer = 0f;
        }

        // Backspace
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (typingUsername && username.length() > 0)
                username.deleteCharAt(username.length() - 1);
            else if (!typingUsername && password.length() > 0)
                password.deleteCharAt(password.length() - 1);
        }

        // Enter: kalau di username → pindah ke password, kalau di password → mulai game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (typingUsername) {
                // Kalau masih di username → pindah ke password
                typingUsername = false;
                showCursor = true;
                cursorTimer = 0f;
            } else {
                // Kalau di password → login
                if (username.length() > 0 && password.length() > 0) {
                    message = "Connecting...";
                    ApiClient.login(username.toString(), password.toString(),
                        () -> {
                            ApiClient.startSession(() -> {
                                Gdx.app.postRunnable(() -> {
                                    GameManager.getInstance().reset();
                                    game.setScreen(new GameScreen(game));
                                });
                            });
                        },
                        () -> {
                            ApiClient.register(username.toString(), password.toString(),
                                () -> {
                                    ApiClient.login(username.toString(), password.toString(),
                                        () -> {
                                            ApiClient.startSession(() -> {
                                                Gdx.app.postRunnable(() -> {
                                                    GameManager.getInstance().reset();
                                                    game.setScreen(new GameScreen(game));
                                                });
                                            });
                                        },
                                        () -> Gdx.app.postRunnable(() -> message = "Gagal login!")
                                    );
                                },
                                () -> Gdx.app.postRunnable(() -> message = "Gagal register!")
                            );
                        }
                    );
                } else {
                    message = "Isi semua field dulu!";
                }
            }
        }

        // Klik tombol START dengan mouse
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mx = Gdx.input.getX();
            float my = h - Gdx.input.getY();
            if (mx >= w / 2f - 80 && mx <= w / 2f + 80 &&
                my >= h / 2f - 130 && my <= h / 2f - 90) {
                if (username.length() > 0 && password.length() > 0) {
                    message = "Connecting...";
                    ApiClient.login(username.toString(), password.toString(),
                        () -> {
                            ApiClient.startSession(() -> {
                                GameManager.getInstance().reset();
                                game.setScreen(new GameScreen(game));
                            });
                        },
                        () -> {
                            ApiClient.register(username.toString(), password.toString(),
                                () -> {
                                    ApiClient.login(username.toString(), password.toString(),
                                        () -> {
                                            ApiClient.startSession(() -> {
                                                GameManager.getInstance().reset();
                                                game.setScreen(new GameScreen(game));
                                            });
                                        },
                                        () -> message = "Gagal login!"
                                    );
                                },
                                () -> message = "Gagal register!"
                            );
                        }
                    );
                } else {
                    message = "Isi semua field dulu!";
                }
            }
        }

        // Input karakter
        for (int i = 29; i <= 54; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                String ch = Input.Keys.toString(i).toLowerCase();
                if (typingUsername) username.append(ch);
                else password.append(ch);
            }
        }
        for (int i = 7; i <= 16; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                String ch = Input.Keys.toString(i);
                if (typingUsername) username.append(ch);
                else password.append(ch);
            }
        }

        // ── Teks ──
        game.batch.begin();

        // Judul di header
        titleFont.draw(game.batch, "CHAOTIC LAPRAK",
            w / 2f - 155, h - 25);

        // Subtitle
        hintFont.draw(game.batch, "kejar dosennya. Kumpulkan TTD-nya. jangan telat!",
            w / 2f - 214, h / 2f + 150);

        // Label username
        hintFont.draw(game.batch, "USERNAME",
            w / 2f - 180, h / 2f + 74);
        String userDisplay = username.toString() + (typingUsername && showCursor ? "|" : "");
        inputFont.draw(game.batch, userDisplay,
            w / 2f - 175, h / 2f + 48);

        // Label password
        hintFont.draw(game.batch, "PASSWORD",
            w / 2f - 180, h / 2f + 4);
        String passDisplay = "*".repeat(password.length()) + (!typingUsername && showCursor ? "|" : "");
        inputFont.draw(game.batch, passDisplay,
            w / 2f - 175, h / 2f - 22);

        // Teks tombol START
        inputFont.setColor(!typingUsername && password.length() > 0 ? YELLOW : TEXT_HINT);
        inputFont.draw(game.batch, "[ START ]", w / 2f - 52, h / 2f - 103);
        inputFont.setColor(TEXT_MAIN);

        // Pesan error
        if (!message.isEmpty()) {
            inputFont.setColor(0.9f, 0.35f, 0.15f, 1f);
            layout.setText(inputFont, message);
            inputFont.draw(game.batch, message, w / 2f - layout.width / 2f, h / 2f - 65);
            inputFont.setColor(TEXT_MAIN);
        }

        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        titleFont.dispose();
        inputFont.dispose();
        hintFont.dispose();
        shape.dispose();
    }
}

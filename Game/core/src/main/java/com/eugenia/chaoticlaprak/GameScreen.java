package com.eugenia.chaoticlaprak;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GameScreen implements Screen {
    private final ChaoticLaprakGame game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Player player;
    private TiledMapTileLayer blockedLayer;
    private List<NPC> npcs;
    private BitmapFont font;
    private ShapeRenderer uiRenderer;
    private NPC nearbyTarget = null; //buat interaksi

    // kantin:
    private Kantin kantin;
    private String kantinMessage = "";
    private float kantinMessageTimer = 0f;

    public GameScreen(ChaoticLaprakGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player = new Player(200, 200);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        blockedLayer = (TiledMapTileLayer) map.getLayers().get("Blocked");
        font = new BitmapFont();
        uiRenderer = new ShapeRenderer();
        kantin = new Kantin(592, 400);

        // 6 titik spawn dosen/aslab (adjust sesuai mapmu)
        float[][] targetSpawns = {
            {200, 200}, {400, 200}, {600, 200},
            {200, 400}, {400, 400}, {600, 400}
        };

        // 4 titik spawn NPC penunjuk arah
        float[][] npcSpawns = {
            {300, 300}, {500, 300}, {300, 500}, {500, 500}
        };

        // Data dosen dan aslab
        List<String[]> dosens = new ArrayList<>(Arrays.asList(
            new String[]{"Mr. Astha", "dosen"},
            new String[]{"Mrs. Riri", "dosen"}
        ));
        List<String[]> aslabs = new ArrayList<>(Arrays.asList(
            new String[]{"BN", "aslab"},
            new String[]{"NL", "aslab"},
            new String[]{"AF", "aslab"}
        ));

        Collections.shuffle(dosens);
        Collections.shuffle(aslabs);

        // Minimal 1 dosen, lalu 2 aslab ATAU 2 dosen 1 aslab
        List<String[]> selectedTargets = new ArrayList<>();
        selectedTargets.add(dosens.get(0)); // 1 dosen pasti ada
        selectedTargets.add(aslabs.get(0));
        selectedTargets.add(aslabs.get(1));
        Collections.shuffle(selectedTargets);

        // 6 titik spawn dosen/aslab
        List<float[]> targetSpawnList = new ArrayList<>(Arrays.asList(
            new float[]{200, 250}, new float[]{450, 250}, new float[]{500, 200},
            new float[]{200, 400}, new float[]{400, 400}, new float[]{600, 400}
        ));
        Collections.shuffle(targetSpawnList);

        // 4 titik spawn NPC, pilih 2
        List<float[]> npcSpawnList = new ArrayList<>(Arrays.asList(
            new float[]{300, 300}, new float[]{300, 300},
            new float[]{300, 450}, new float[]{500, 300}
        ));
        Collections.shuffle(npcSpawnList);

        npcs = new ArrayList<>();

        // Spawn 3 target
        for (int i = 0; i < 3; i++) {
            boolean isDosen = selectedTargets.get(i)[1].equals("dosen");
            npcs.add(new NPC(targetSpawnList.get(i)[0], targetSpawnList.get(i)[1],
                selectedTargets.get(i)[0], isDosen));
        }

// Spawn 2 NPC dari 4 titik random
        npcs.add(new NPC(npcSpawnList.get(0)[0], npcSpawnList.get(0)[1], "NPC1", false));
        npcs.add(new NPC(npcSpawnList.get(1)[0], npcSpawnList.get(1)[1], "NPC2", false));

        GameManager.getInstance().addObserver(newEnergy -> {
            if (newEnergy < GameManager.ENERGY_SLOWDOWN_THRESHOLD) {
                System.out.println("Player melambat!"); // nanti diganti visual effect
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.position.set(
            player.x + Player.SIZE / 2,
            player.y + Player.SIZE / 2,
            0
        );
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        GameManager.getInstance().update(delta);

        player.update(delta, blockedLayer);
        player.render(camera);

        for (NPC npc : npcs) {
            npc.update(delta);
            npc.render(camera);
        }

        // Cek proximity ke target
        nearbyTarget = null;
        for (NPC npc : npcs) {
            if (!npc.isDosen && npcs.indexOf(npc) >= 3) continue; // skip NPC biasa
            float dist = Vector2.dst(player.x, player.y, npc.x, npc.y);
            if (dist < 80f) {
                nearbyTarget = npc;
                break;
            }
        }

        // Tampilkan bubble
        if (nearbyTarget != null) {
            game.batch.begin();
            font.draw(game.batch, nearbyTarget.name + ": " + nearbyTarget.getBubbleText(),
                Gdx.graphics.getWidth() / 2f - 100,
                Gdx.graphics.getHeight() / 2f + 50);
            game.batch.end();
        }

        // Handle input M dan C
        if (nearbyTarget != null) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                if (nearbyTarget.canSign() && !nearbyTarget.signed) {
                    new InteractCommand(nearbyTarget).execute();
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                if (GameManager.getInstance().hasCemilan) {
                    nearbyTarget.giveCemilan();
                    GameManager.getInstance().hasCemilan = false;
                } else {
                    // tampilkan pesan
                    game.batch.begin();
                    font.draw(game.batch, "Ke kantin dulu!",
                        Gdx.graphics.getWidth() / 2f - 50,
                        Gdx.graphics.getHeight() / 2f);
                    game.batch.end();
                }
            }
        }

        // UI tidak ikut camera, pakai koordinat layar langsung
        uiRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background bar energi (abu-abu)
        uiRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
        uiRenderer.rect(20, Gdx.graphics.getHeight() - 30, 200, 20);

        // Bar energi (hijau/merah tergantung threshold)
        float energy = GameManager.getInstance().energy;
        if (energy < GameManager.ENERGY_SLOWDOWN_THRESHOLD) {
            uiRenderer.setColor(1, 0, 0, 1); // merah kalau hampir habis
        } else {
            uiRenderer.setColor(0, 1, 0, 1); // hijau
        }
        uiRenderer.rect(20, Gdx.graphics.getHeight() - 30, energy * 20, 20);

        uiRenderer.end();

        // Text
        game.batch.begin();
        font.draw(game.batch, "Energi: " + (int)energy + "/10", 20, Gdx.graphics.getHeight() - 40);
        font.draw(game.batch, "TTD: " + GameManager.getInstance().progress + "/3", 20, Gdx.graphics.getHeight() - 60);
        font.draw(game.batch, "Waktu: " + (int)GameManager.getInstance().elapsedTime + "s", 20, Gdx.graphics.getHeight() - 80);
        font.draw(game.batch, GameManager.getInstance().energy < GameManager.ENERGY_SLOWDOWN_THRESHOLD ? "LAMBAT! Minum Kopi!" : "", 20, Gdx.graphics.getHeight() - 100);
        game.batch.end();

        kantin.render(camera);

        // Cek proximity ke kantin
        float distKantin = Vector2.dst(player.x, player.y, kantin.x, kantin.y);
        if (distKantin < 80f) {
            game.batch.begin();
            font.draw(game.batch, "Kantin: Beli Kopi (K) | Beli Cemilan (X)",
                Gdx.graphics.getWidth() / 2f - 150,
                Gdx.graphics.getHeight() / 2f + 50);
            game.batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                if (!GameManager.getInstance().hasBoughtKopi) {
                    new BuyCommand("kopi").execute();
                    GameManager.getInstance().hasBoughtKopi = true;
                    kantinMessage = "Energi penuh!";
                } else {
                    kantinMessage = "Sudah beli kopi!";
                }
                kantinMessageTimer = 2f;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                new BuyCommand("cemilan").execute();
                kantinMessage = "Cemilan dibeli!";
                kantinMessageTimer = 2f;
            }
        }

        // Tampilkan pesan kantin sementara
        if (kantinMessageTimer > 0) {
            kantinMessageTimer -= delta;
            game.batch.begin();
            font.draw(game.batch, kantinMessage,
                Gdx.graphics.getWidth() / 2f - 50,
                Gdx.graphics.getHeight() / 2f);
            game.batch.end();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        player.dispose();
        for (NPC npc : npcs) npc.dispose();
        font.dispose();
        uiRenderer.dispose();
        kantin.dispose();
    }
}

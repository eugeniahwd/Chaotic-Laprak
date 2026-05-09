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
import com.badlogic.gdx.math.Vector3;
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
    private NPC nearbyTarget = null;

    private String npcHint = "";
    private Kantin kantin;
    private String kantinMessage = "";
    private float kantinMessageTimer = 0f;
    private float relocateTimer = 10f;
    private String nearbyBuildingName = "";

    private static final float[][] BUILDING_POSITIONS = {
        {112, 336}, {352, 304}, {576, 96},
        {736, 32},  {736, 368}, {700, 368}
    };
    private static final String[] BUILDING_NAMES = {
        "Gedung S", "Gedung K", "Gedung DTE",
        "Gedung I-Cell", "Gedung MRPQ", "Kantin"
    };
    private static final float[][] TARGET_SPAWNS = {
        {112, 336}, {352, 304}, {576, 96},
        {736, 32},  {736, 368}, {112, 96}
    };

    public GameScreen(ChaoticLaprakGame game) {
        this.game = game;
    }

    private String getHintForNPC() {
        // pilih random satu target yang belum di-TTD
        List<NPC> unsignedTargets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (!npcs.get(i).signed) unsignedTargets.add(npcs.get(i));
        }
        if (unsignedTargets.isEmpty()) return "Semua sudah di-TTD!";

        // pilih random salah satu
        NPC target = unsignedTargets.get((int)(Math.random() * unsignedTargets.size()));

        // Cari gedung terdekat dari posisi target
        String nearestBuilding = "suatu tempat";
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < BUILDING_POSITIONS.length - 1; i++) { // skip kantin
            float dist = Vector2.dst(target.x, target.y,
                BUILDING_POSITIONS[i][0], BUILDING_POSITIONS[i][1]);
            if (dist < minDist) {
                minDist = dist;
                nearestBuilding = BUILDING_NAMES[i];
            }
        }
        return target.name + " ada di " + nearestBuilding + "!";
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        player = new Player(32, 32);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 3f);
        blockedLayer = (TiledMapTileLayer) map.getLayers().get("Blocked");
        font = new BitmapFont();
        uiRenderer = new ShapeRenderer();
        kantin = new Kantin(600, 375);

        List<float[]> npcSpawnList = new ArrayList<>(Arrays.asList(
            new float[]{200, 200}, new float[]{400, 200},
            new float[]{200, 300}, new float[]{400, 300}
        ));
        Collections.shuffle(npcSpawnList);

        npcs = new ArrayList<>();
        npcs.addAll(TargetFactory.createTargets(TARGET_SPAWNS));
        npcs.add(new NPC(npcSpawnList.get(0)[0], npcSpawnList.get(0)[1], "NPC1", false));
        npcs.add(new NPC(npcSpawnList.get(1)[0], npcSpawnList.get(1)[1], "NPC2", false));
        npcHint = getHintForNPC();

        GameManager.getInstance().addObserver(newEnergy -> {
            if (newEnergy < GameManager.ENERGY_SLOWDOWN_THRESHOLD) {
                System.out.println("Player melambat!");
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Camera
        float mapWidth = map.getProperties().get("width", Integer.class) * 16f;
        float mapHeight = map.getProperties().get("height", Integer.class) * 16f;
        float camHalfW = camera.viewportWidth / 2f;
        float camHalfH = camera.viewportHeight / 2f;
        float camX = Math.max(camHalfW, Math.min(player.x + Player.SIZE / 2, mapWidth - camHalfW));
        float camY = Math.max(camHalfH, Math.min(player.y + Player.SIZE / 2, mapHeight - camHalfH));
        camera.position.set(camX, camY, 0);
        camera.update();

        // Render map
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Update game state
        GameManager.getInstance().update(delta);

        // Cek game over - waktu habis
        if (GameManager.getInstance().countdownTime <= 0) {
            game.setScreen(new GameOverScreen(game, "waktu"));
            return;
        }

        // Cek game over - energi habis
        if (GameManager.getInstance().energy <= 0) {
            game.setScreen(new GameOverScreen(game, "energi"));
            return;
        }

        // Cek win
        if (GameManager.getInstance().isGameWon()) {
            game.setScreen(new WinScreen(game, GameManager.getInstance().elapsedTime));
            return;
        }

        // Relocate
        relocateTimer -= delta;
        if (relocateTimer <= 0) {
            relocateTimer = 10f;
            List<float[]> newSpawns = new ArrayList<>(Arrays.asList(TARGET_SPAWNS));
            Collections.shuffle(newSpawns);
            for (int i = 0; i < 3; i++) {
                if (!npcs.get(i).signed) {
                    npcs.get(i).x = newSpawns.get(i)[0];
                    npcs.get(i).y = newSpawns.get(i)[1];
                    npcs.get(i).startX = newSpawns.get(i)[0];
                }
            }
            npcHint = getHintForNPC();
        }

        // Update player & NPC
        player.update(delta, blockedLayer);
        for (NPC npc : npcs) npc.update(delta);

        // Cek proximity target
        nearbyTarget = null;
        for (int i = 0; i < 3; i++) {
            NPC npc = npcs.get(i);
            if (Vector2.dst(player.x, player.y, npc.x, npc.y) < 80f) {
                nearbyTarget = npc;
                break;
            }
        }

        // Cek proximity gedung
        nearbyBuildingName = "";
        for (int i = 0; i < BUILDING_POSITIONS.length; i++) {
            if (Vector2.dst(player.x, player.y,
                BUILDING_POSITIONS[i][0], BUILDING_POSITIONS[i][1]) < 80f) {
                nearbyBuildingName = BUILDING_NAMES[i];
                break;
            }
        }

        // Handle input
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
                }
            }
        }

        if (distKantin() < 80f) {
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
        if (kantinMessageTimer > 0) kantinMessageTimer -= delta;

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch, camera);
        for (NPC npc : npcs) npc.render(game.batch, camera);
        game.batch.end();

        // RENDER UI (koordinat layar)
        float energy = GameManager.getInstance().energy;

        // Bar energi
        uiRenderer.begin(ShapeRenderer.ShapeType.Filled);
        uiRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
        uiRenderer.rect(20, Gdx.graphics.getHeight() - 30, 200, 20);
        uiRenderer.setColor(
            energy < GameManager.ENERGY_SLOWDOWN_THRESHOLD ? 1 : 0,
            energy < GameManager.ENERGY_SLOWDOWN_THRESHOLD ? 0 : 1, 0, 1);
        uiRenderer.rect(20, Gdx.graphics.getHeight() - 30, energy * 20, 20);
        uiRenderer.end();

        game.batch.setProjectionMatrix(game.batch.getProjectionMatrix().setToOrtho2D(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        game.batch.begin();

        // HUD
        font.draw(game.batch, "Energi: " + (int)energy + "/10", 20, Gdx.graphics.getHeight() - 40);
        font.draw(game.batch, "TTD: " + GameManager.getInstance().progress + "/3", 20, Gdx.graphics.getHeight() - 60);
        font.draw(game.batch, "Waktu: " + (int)GameManager.getInstance().countdownTime + "s", 20, Gdx.graphics.getHeight() - 80);
        if (energy < GameManager.ENERGY_SLOWDOWN_THRESHOLD) {
            font.draw(game.batch, "LAMBAT! Minum Kopi!", 20, Gdx.graphics.getHeight() - 100);
        }

        // Bubble NPC/dosen - di atas kepala NPC
        if (nearbyTarget != null) {
            Vector3 npcPos = new Vector3(nearbyTarget.x, nearbyTarget.y + 20, 0);
            camera.project(npcPos);
            font.draw(game.batch, nearbyTarget.name + ": " + nearbyTarget.getBubbleText(),
                npcPos.x - 50, npcPos.y + 10);

            // ke kantin dulu! di atas kepala NPC jg
            if (!GameManager.getInstance().hasCemilan && !nearbyTarget.canSign() && !nearbyTarget.signed) {
                font.draw(game.batch, "Ke kantin dulu!",
                    npcPos.x - 40, npcPos.y - 10);
            }
        }

        // Bubble kantin - di atas kepala player
        if (distKantin() < 80f) {
            Vector3 playerPos = new Vector3(player.x, player.y + 20, 0);
            camera.project(playerPos);
            font.draw(game.batch, "Beli Kopi (K) | Beli Cemilan (X)",
                playerPos.x - 70, playerPos.y + 10);
        }

        // Pesan kantin sementara - di atas kepala player
        if (kantinMessageTimer > 0) {
            Vector3 playerPos = new Vector3(player.x, player.y + 20, 0);
            camera.project(playerPos);
            font.draw(game.batch, kantinMessage,
                playerPos.x - 40, playerPos.y - 10);
        }

        // Bubble NPC penunjuk arah (index 3 dan 4)
        for (int i = 3; i < 5; i++) {
            NPC npc = npcs.get(i);
            if (Vector2.dst(player.x, player.y, npc.x, npc.y) < 80f) {
                Vector3 npcPos = new Vector3(npc.x, npc.y + 20, 0);
                camera.project(npcPos);
                font.draw(game.batch, npcHint, npcPos.x - 60, npcPos.y + 10);
                break;
            }
        }

        game.batch.end();
    }

    private float distKantin() {
        return Vector2.dst(player.x, player.y, kantin.x, kantin.y);
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

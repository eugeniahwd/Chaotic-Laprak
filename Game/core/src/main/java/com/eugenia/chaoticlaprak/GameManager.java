package com.eugenia.chaoticlaprak;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private List<EnergyObserver> observers = new ArrayList<>();

    // State game
    public float energy = 10f;
    public int progress = 0; // TTD terkumpul
    public boolean hasCemilan = false;
    public float elapsedTime = 0f;
    public boolean gameOver = false;
    public boolean hasBoughtKopi = false;

    // Konstanta
    public static final float ENERGY_SLOWDOWN_THRESHOLD = 4f;
    public static final float NORMAL_SPEED = 70f;
    public static final float SLOW_SPEED = 40f;

    // Singleton
    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void reset() {
        instance = new GameManager();
    }

    public float getPlayerSpeed() {
        return energy < ENERGY_SLOWDOWN_THRESHOLD ? SLOW_SPEED : NORMAL_SPEED;
    }

    public void addObserver(EnergyObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (EnergyObserver observer : observers) {
            observer.onEnergyChanged(energy);
        }
    }

    public void update(float delta) {
        if (!gameOver) {
            elapsedTime += delta;
            // Energi berkurang perlahan seiring waktu
            energy -= 0.3f * delta;
            if (energy < 0) energy = 0;
            notifyObservers();
        }
    }

    public boolean isGameWon() {
        return progress >= 3;
    }
}

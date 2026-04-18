package com.eugenia.chaoticlaprak;

public class BadMoodState implements MoodState {
    @Override
    public boolean canSign() { return false; }

    @Override
    public String getBubbleText() { return "Gak Mau! Beri Cemilan (C)"; }

    @Override
    public MoodState improve() { return new GoodMoodState(); }
}

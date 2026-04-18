package com.eugenia.chaoticlaprak;

public class GoodMoodState implements MoodState {
    @Override
    public boolean canSign() { return true; }

    @Override
    public String getBubbleText() { return "Minta TTD (M)"; }

    @Override
    public MoodState improve() { return this; }
}

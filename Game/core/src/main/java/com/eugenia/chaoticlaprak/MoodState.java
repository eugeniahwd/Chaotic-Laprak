package com.eugenia.chaoticlaprak;

public interface MoodState {
    boolean canSign();
    String getBubbleText();
    MoodState improve();
}

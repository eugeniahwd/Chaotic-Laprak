package com.eugenia.chaoticlaprak;

public class BuyCommand implements Command {
    private String itemType; // "kopi" atau "cemilan"

    public BuyCommand(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public void execute() {
        if (itemType.equals("kopi")) {
            GameManager.getInstance().energy = 10f;
        } else if (itemType.equals("cemilan")) {
            GameManager.getInstance().hasCemilan = true;
        }
    }
}

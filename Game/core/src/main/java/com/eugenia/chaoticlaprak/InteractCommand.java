package com.eugenia.chaoticlaprak;

public class InteractCommand implements Command {
    private NPC target;

    public InteractCommand(NPC target) {
        this.target = target;
    }

    @Override
    public void execute() {
        if (target.canSign() && !target.signed) {
            GameManager.getInstance().progress++;
            target.signed = true;
        }
    }
}

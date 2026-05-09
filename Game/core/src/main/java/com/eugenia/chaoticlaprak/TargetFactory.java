package com.eugenia.chaoticlaprak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TargetFactory {

    private static final List<String[]> DOSENS = Arrays.asList(
        new String[]{"Mr. Astha", "dosen"},
        new String[]{"Mrs. Riri", "dosen"}
    );

    private static final List<String[]> ASLABS = Arrays.asList(
        new String[]{"BN", "aslab"},
        new String[]{"NL", "aslab"},
        new String[]{"AF", "aslab"}
    );

    public static List<NPC> createTargets(float[][] spawnPoints) {
        List<String[]> dosens = new ArrayList<>(DOSENS);
        List<String[]> aslabs = new ArrayList<>(ASLABS);
        Collections.shuffle(dosens);
        Collections.shuffle(aslabs);

        List<String[]> selected = new ArrayList<>();
        selected.add(dosens.get(0)); // minimal 1 dosen
        selected.add(aslabs.get(0));
        selected.add(aslabs.get(1));
        Collections.shuffle(selected);

        List<float[]> spawnList = new ArrayList<>(Arrays.asList(spawnPoints));
        Collections.shuffle(spawnList);

        List<NPC> targets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            boolean isDosen = selected.get(i)[1].equals("dosen");
            targets.add(new NPC(spawnList.get(i)[0], spawnList.get(i)[1],
                selected.get(i)[0], isDosen));
        }
        return targets;
    }
}

package com.eugenia.chaoticlaprak.service;

import com.eugenia.chaoticlaprak.model.Target;
import com.eugenia.chaoticlaprak.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetService {

    private final TargetRepository targetRepository;

    public List<Target> getRandomTargets() {
        List<Target> dosens = targetRepository.findByRole("dosen");
        List<Target> aslabs = targetRepository.findByRole("aslab");

        // Minimal 1 dosen
        Collections.shuffle(dosens);
        Collections.shuffle(aslabs);

        List<Target> result = new ArrayList<>();
        result.add(dosens.get(0)); // pasti ada 1 dosen
        result.add(aslabs.get(0));
        result.add(aslabs.get(1));

        Collections.shuffle(result);
        return result;
    }
}
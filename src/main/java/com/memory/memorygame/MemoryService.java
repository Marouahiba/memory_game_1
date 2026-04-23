package com.memory.memorygame;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MemoryService {

    private final String[] IMAGES = {
            "/images/img1.jpg", "/images/img2.jpg",
            "/images/img3.jpg", "/images/img4.jpg",
            "/images/img5.jpg", "/images/img6.jpg",
            "/images/img7.jpg", "/images/img8.jpg",
            "/images/img9.jpg", "/images/img10.jpg",
            "/images/img11.jpg", "/images/img12.jpg"
    };

    public List<Map<String, Object>> getCartes(int niveau) {
        int nbPaires;
        switch (niveau) {
            case 1: nbPaires = 6; break;   // 12 cartes (6 paires)
            case 2: nbPaires = 8; break;   // 16 cartes (8 paires)
            case 3: nbPaires = 12; break;  // 24 cartes (12 paires)
            default: nbPaires = 6;
        }

        List<Map<String, Object>> cartes = new ArrayList<>();
        for (int i = 0; i < nbPaires; i++) {
            String image = IMAGES[i % IMAGES.length];
            Map<String, Object> carte1 = new HashMap<>();
            carte1.put("valeur", image);
            carte1.put("retournee", false);
            cartes.add(carte1);

            Map<String, Object> carte2 = new HashMap<>();
            carte2.put("valeur", image);
            carte2.put("retournee", false);
            cartes.add(carte2);
        }

        Collections.shuffle(cartes);
        return cartes;
    }
}
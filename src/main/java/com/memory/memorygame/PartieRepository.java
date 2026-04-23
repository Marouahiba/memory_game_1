package com.memory.memorygame;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartieRepository extends JpaRepository<Partie, Long> {
    List<Partie> findByJoueur(Joueur joueur);
}
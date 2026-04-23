package com.memory.memorygame;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    Optional<Joueur> findByPseudo(String pseudo);
}
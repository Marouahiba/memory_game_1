package com.memory.memorygame;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PartieRepository extends JpaRepository<Partie, Long> {
    void deleteById(Long id);
    List<Partie> findByNomJoueur(String nomJoueur);

}

package com.memory.memorygame;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partie")
public class Partie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomJoueur;
    private int niveau;
    private int score;
    private int coups;
    @Column(columnDefinition = "LONGTEXT")
    private String cartes; // On stockera l'état du jeu en JSON
    private LocalDateTime dateSauvegarde;

    // Getters et Setters (à générer avec Alt+Insert)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomJoueur() { return nomJoueur; }
    public void setNomJoueur(String nomJoueur) { this.nomJoueur = nomJoueur; }
    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getCoups() { return coups; }
    public void setCoups(int coups) { this.coups = coups; }
    public String getCartes() { return cartes; }
    public void setCartes(String cartes) { this.cartes = cartes; }
    public LocalDateTime getDateSauvegarde() { return dateSauvegarde; }
    public void setDateSauvegarde(LocalDateTime dateSauvegarde) { this.dateSauvegarde = dateSauvegarde; }
}
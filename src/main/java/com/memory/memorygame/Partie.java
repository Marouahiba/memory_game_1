package com.memory.memorygame;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partie")
public class Partie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_joueur")
    private Joueur joueur;

    private int niveau;
    private int score;
    private int coups;

    @Column(columnDefinition = "LONGTEXT")
    private String cartes;

    private LocalDateTime dateSauvegarde;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Joueur getJoueur() { return joueur; }
    public void setJoueur(Joueur joueur) { this.joueur = joueur; }

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
package com.memory.memorygame;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GameController {

    private final MemoryService memoryService;
    private final PartieRepository partieRepository;
    private final JoueurRepository joueurRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public GameController(MemoryService memoryService, PartieRepository partieRepository, JoueurRepository joueurRepository) {
        this.memoryService = memoryService;
        this.partieRepository = partieRepository;
        this.joueurRepository = joueurRepository;
    }

    @GetMapping("/")
    public String accueil() {
        return "accueil";
    }

    @GetMapping("/nouvelle-partie")
    public String nouvellePartie(@RequestParam int niveau, HttpSession session, Model model) {
        if (!estConnecte(session)) {
            return "redirect:/";
        }

        Long joueurId = (Long) session.getAttribute("joueurId");
        Joueur joueur = joueurRepository.findById(joueurId).orElse(null);
        if (joueur == null) return "redirect:/";

        model.addAttribute("niveau", niveau);
        model.addAttribute("joueur", joueur.getPseudo());
        model.addAttribute("cartes", memoryService.getCartes(niveau));
        return "jeu";
    }

    @GetMapping("/charger-partie")
    public String afficherPartiesSauvegardees(HttpSession session, Model model) {
        if (!estConnecte(session)) {
            return "redirect:/";
        }

        Long joueurId = (Long) session.getAttribute("joueurId");
        Joueur joueur = joueurRepository.findById(joueurId).orElse(null);
        if (joueur == null) return "redirect:/";

        List<Partie> partiesJoueur = partieRepository.findByJoueur(joueur);
        model.addAttribute("parties", partiesJoueur);
        model.addAttribute("joueur", joueur.getPseudo());
        return "charger";
    }

    @GetMapping("/reprendre-partie")
    public String reprendrePartie(@RequestParam Long id, Model model) {
        Partie partie = partieRepository.findById(id).orElse(null);
        if (partie == null) {
            return "redirect:/charger-partie";
        }

        model.addAttribute("id", partie.getId());
        model.addAttribute("niveau", partie.getNiveau());
        model.addAttribute("score", partie.getScore());
        model.addAttribute("coups", partie.getCoups());
        model.addAttribute("cartesSauvegardees", partie.getCartes());

        return "reprendre";
    }

    @PostMapping("/sauvegarder")
    @ResponseBody
    public Map<String, Object> sauvegarderPartie(@RequestBody Map<String, Object> data, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long joueurId = (Long) session.getAttribute("joueurId");
            if (joueurId == null) {
                result.put("erreur", "Non connecté");
                return result;
            }

            Joueur joueur = joueurRepository.findById(joueurId).orElse(null);
            if (joueur == null) {
                result.put("erreur", "Joueur non trouvé");
                return result;
            }

            Partie partie;

            if (data.containsKey("id") && data.get("id") != null) {
                Long id = ((Number) data.get("id")).longValue();
                partie = partieRepository.findById(id).orElse(null);
                if (partie == null) {
                    partie = new Partie();
                }
            } else {
                partie = new Partie();
            }

            partie.setJoueur(joueur);

            Object niveau = data.get("niveau");
            if (niveau instanceof Integer) {
                partie.setNiveau((Integer) niveau);
            } else if (niveau instanceof String) {
                partie.setNiveau(Integer.parseInt((String) niveau));
            }

            Object score = data.get("score");
            if (score instanceof Integer) {
                partie.setScore((Integer) score);
            } else if (score instanceof String) {
                partie.setScore(Integer.parseInt((String) score));
            }

            Object coups = data.get("coups");
            if (coups instanceof Integer) {
                partie.setCoups((Integer) coups);
            } else if (coups instanceof String) {
                partie.setCoups(Integer.parseInt((String) coups));
            }

            ObjectMapper mapper = new ObjectMapper();
            partie.setCartes(mapper.writeValueAsString(data.get("cartes")));
            partie.setDateSauvegarde(LocalDateTime.now());

            Partie sauvegardee = partieRepository.save(partie);
            result.put("id", sauvegardee.getId());
        } catch (Exception e) {
            result.put("erreur", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/supprimer-partie")
    @ResponseBody
    public Map<String, Object> supprimerPartie(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long id = ((Number) data.get("id")).longValue();
            partieRepository.deleteById(id);
            result.put("success", true);
        } catch (Exception e) {
            result.put("erreur", e.getMessage());
        }
        return result;
    }

    private boolean estConnecte(HttpSession session) {
        return session.getAttribute("joueurId") != null;
    }

    @PostMapping("/connexion")
    @ResponseBody
    public Map<String, Object> connexion(@RequestBody Map<String, Object> data, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String pseudo = (String) data.get("pseudo");
        String motDePasse = (String) data.get("motDePasse");

        Optional<Joueur> joueurOpt = joueurRepository.findByPseudo(pseudo);

        if (joueurOpt.isPresent() && passwordEncoder.matches(motDePasse, joueurOpt.get().getMotDePasse())) {
            session.setAttribute("joueurId", joueurOpt.get().getId());
            session.setAttribute("joueurPseudo", joueurOpt.get().getPseudo());
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("message", "Pseudo ou mot de passe incorrect");
        }
        return result;
    }

    @PostMapping("/inscription")
    @ResponseBody
    public Map<String, Object> inscription(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        String pseudo = (String) data.get("pseudo");
        String motDePasse = (String) data.get("motDePasse");

        if (joueurRepository.findByPseudo(pseudo).isPresent()) {
            result.put("success", false);
            result.put("message", "Ce pseudo existe déjà");
            return result;
        }

        String motDePasseCrypte = passwordEncoder.encode(motDePasse);
        Joueur joueur = new Joueur(pseudo, motDePasseCrypte);
        joueurRepository.save(joueur);

        result.put("success", true);
        return result;
    }

    @GetMapping("/deconnexion")
    public String deconnexion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/verifier-session")
    @ResponseBody
    public Map<String, Object> verifierSession(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Long joueurId = (Long) session.getAttribute("joueurId");
        if (joueurId != null) {
            result.put("connecte", true);
            result.put("joueur", session.getAttribute("joueurPseudo"));
        } else {
            result.put("connecte", false);
        }
        return result;
    }

    @GetMapping("/commentjouer")
    public String commentJouer() {
        return "commentjouer";
    }
}
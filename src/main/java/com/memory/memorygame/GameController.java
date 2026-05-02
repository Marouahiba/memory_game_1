package com.memory.memorygame;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        if (joueur == null) {
            return "redirect:/";
        }

        model.addAttribute("niveau", niveau);
        model.addAttribute("joueur", joueur.getPseudo());
        model.addAttribute("cartes", memoryService.getCartes(niveau));
        return "jeu";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "ok";
    }

    @GetMapping("/charger-partie")
    public String afficherPartiesSauvegardees(HttpSession session, Model model) {
        if (!estConnecte(session)) {
            return "redirect:/";
        }

        Long joueurId = (Long) session.getAttribute("joueurId");
        Joueur joueur = joueurRepository.findById(joueurId).orElse(null);
        if (joueur == null) {
            return "redirect:/";
        }

        List<Partie> partiesJoueur = partieRepository.findByJoueur(joueur);
        model.addAttribute("parties", partiesJoueur);
        model.addAttribute("joueur", joueur.getPseudo());
        return "charger";
    }

    @GetMapping("/reprendre-partie")
    public String reprendrePartie(@RequestParam Long id, HttpSession session, Model model) {
        if (!estConnecte(session)) {
            return "redirect:/";
        }

        Partie partie = partieRepository.findById(id).orElse(null);
        Long joueurId = (Long) session.getAttribute("joueurId");
        if (partie == null || !partie.getJoueur().getId().equals(joueurId)) {
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
                result.put("erreur", "Non connecte");
                return result;
            }

            Joueur joueur = joueurRepository.findById(joueurId).orElse(null);
            if (joueur == null) {
                result.put("erreur", "Joueur non trouve");
                return result;
            }

            Partie partie = chargerOuCreerPartie(data.get("id"), joueurId, result);
            if (partie == null) {
                return result;
            }

            partie.setJoueur(joueur);
            partie.setNiveau(lireEntier(data.get("niveau")));
            partie.setScore(lireEntier(data.get("score")));
            partie.setCoups(lireEntier(data.get("coups")));

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
    public Map<String, Object> supprimerPartie(@RequestBody Map<String, Object> data, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long joueurId = (Long) session.getAttribute("joueurId");
            if (joueurId == null) {
                result.put("erreur", "Non connecte");
                return result;
            }

            Long id = ((Number) data.get("id")).longValue();
            Partie partie = partieRepository.findById(id).orElse(null);
            if (partie == null) {
                result.put("erreur", "Partie introuvable");
                return result;
            }
            if (!partie.getJoueur().getId().equals(joueurId)) {
                result.put("erreur", "Suppression non autorisee");
                return result;
            }

            partieRepository.delete(partie);
            result.put("success", true);
        } catch (Exception e) {
            result.put("erreur", e.getMessage());
        }
        return result;
    }

    @PostMapping("/connexion")
    @ResponseBody
    public Map<String, Object> connexion(@RequestBody Map<String, Object> data, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String pseudo = (String) data.get("pseudo");
            String motDePasse = (String) data.get("motDePasse");

            Optional<Joueur> joueurOpt = joueurRepository.findByPseudo(pseudo);

            if (joueurOpt.isPresent() && passwordEncoder.matches(motDePasse, joueurOpt.get().getMotDePasse())) {
                session.setAttribute("joueurId", joueurOpt.get().getId());
                session.setAttribute("joueurPseudo", joueurOpt.get().getPseudo());
                result.put("success", true);
                return result;
            }

            result.put("success", false);
            result.put("message", "Pseudo ou mot de passe incorrect");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur serveur: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/inscription")
    @ResponseBody
    public Map<String, Object> inscription(@RequestBody Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            String pseudo = (String) data.get("pseudo");
            String motDePasse = (String) data.get("motDePasse");

            if (joueurRepository.findByPseudo(pseudo).isPresent()) {
                result.put("success", false);
                result.put("message", "Ce pseudo existe deja");
                return result;
            }

            String motDePasseCrypte = passwordEncoder.encode(motDePasse);
            Joueur joueur = new Joueur(pseudo, motDePasseCrypte);
            joueurRepository.save(joueur);

            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur serveur: " + e.getMessage());
        }
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

    private boolean estConnecte(HttpSession session) {
        return session.getAttribute("joueurId") != null;
    }

    private Partie chargerOuCreerPartie(Object rawId, Long joueurId, Map<String, Object> result) {
        if (rawId == null) {
            return new Partie();
        }

        Long id = ((Number) rawId).longValue();
        Partie partie = partieRepository.findById(id).orElse(null);
        if (partie == null) {
            return new Partie();
        }
        if (!partie.getJoueur().getId().equals(joueurId)) {
            result.put("erreur", "Cette partie n'appartient pas au joueur connecte");
            return null;
        }
        return partie;
    }

    private int lireEntier(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            return Integer.parseInt(text);
        }
        return 0;
    }
}

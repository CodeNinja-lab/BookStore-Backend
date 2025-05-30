package com.ucad.m2SIR.SenBook.service;

import com.ucad.m2SIR.SenBook.dto.PanierDTO;
import com.ucad.m2SIR.SenBook.model.DetailsLivre;
import com.ucad.m2SIR.SenBook.model.Inventaire;
import com.ucad.m2SIR.SenBook.model.Panier;
import com.ucad.m2SIR.SenBook.model.Utilisateur;
import com.ucad.m2SIR.SenBook.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PanierService {

    private final DetailsLivreRepository detailsLivreRepository;
    private final PanierRepository panierRepository;
    private final ClientService clientService;
    private final InventaireRepository inventaireRepository;

    public PanierService(DetailsLivreRepository detailsLivreRepository,
                         PanierRepository panierRepository, ClientService clientService,
                         InventaireRepository inventaireRepository) {
        this.detailsLivreRepository = detailsLivreRepository;
        this.panierRepository = panierRepository;
        this.clientService = clientService;
        this.inventaireRepository = inventaireRepository;
    }

    public List<PanierDTO> getPanier() {
        List<PanierDTO> detailsLivres = new ArrayList<>();
        for (Panier panier : panierRepository.findAllByUtilisateurId(clientService.getCurrentUser().getId())) {
            detailsLivres.add(new PanierDTO(panier));
        }
        return detailsLivres;
    }

    @Transactional
    public String addToPanier(int detailLivreId, int quantite) {
        Utilisateur user = clientService.getCurrentUser();
        DetailsLivre livre = detailsLivreRepository.findById(detailLivreId).orElse(null);
        if (user != null && livre != null) {
            Inventaire inventaire = inventaireRepository.findByDetailLivre(livre).orElse(null);
            if (inventaire != null && inventaire.getQuantite() >= quantite) {
                Panier panier = panierRepository.findByUtilisateurAndDetailLivre(user, livre).orElse(new Panier());
                panier.setDetailLivre(livre);
                panier.setUtilisateur(user);
                panier.setDateAjout(Instant.now());
                panier.setQuantite(quantite);
                if (panierRepository.save(panier).getId() != null) {
                    return "Success : Produit ajouté dans le panier";
                }
            } else {
                return "Failed : La quantité demandé est indisponible";
            }
        }
        return "Failed : Erreur lors de l'ajout du produit dans le panier";
    }

    @Transactional
    public String clearPanier() {
        Utilisateur user = clientService.getCurrentUser();
        if (user != null) {
            panierRepository.deleteAllByUtilisateur(user);
            if (!panierRepository.existsByUtilisateur(user)) {
                return "Success : Panier nettoyé avec succés";
            }
        }
        return "Failed : Une erreur est survenue lors du nettoyage du panier";
    }

    @Transactional
    public String removeFromPanier(int detailLivreId) {
        Utilisateur user = clientService.getCurrentUser();
        DetailsLivre livre = detailsLivreRepository.findById(detailLivreId).orElse(null);
        if (user != null && livre != null) {
            panierRepository.deleteAllByUtilisateurAndDetailLivre(user, livre);
            if (!panierRepository.existsByUtilisateurAndDetailLivre(user, livre)) {
                return "Success : Produit enlevé du panier correctement";
            }
        }
        return "Failed : Une erreur est survenue lors de l'enlevement du produit";
    }
}

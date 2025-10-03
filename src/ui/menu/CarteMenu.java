package ui.menu;

import entity.Carte;
import entity.CarteCredit;
import entity.CarteDebit;
import entity.CartePrepayee;
import entity.enums.CarteEnum;
import entity.enums.StatutEnum;
import exception.ServiceException;
import service.impl.CarteService;
import ui.utils.InputUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CarteMenu {
    private final CarteService carteService;

    public CarteMenu(CarteService carteService) {
        this.carteService = carteService;
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GESTION DES CARTES               ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Creer une carte");
            System.out.println("2. Rechercher une carte par ID");
            System.out.println("3. Rechercher les cartes d'un client");
            System.out.println("4. Modifier le statut d'une carte");
            System.out.println("5. Supprimer une carte");
            System.out.println("0. Retour au menu principal");
            System.out.println("─────────────────────────────────────────");

            int choix = InputUtils.lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    creerCarte();
                    break;
                case 2:
                    rechercherParId();
                    break;
                case 3:
                    rechercherParClient();
                    break;
                case 4:
                    modifierStatut();
                    break;
                case 5:
                    supprimerCarte();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    InputUtils.afficherErreur("Choix invalide!");
            }

            if (continuer && choix != 0) {
                InputUtils.pause();
            }
        }
    }

    private void creerCarte() {
        System.out.println("\n═══ CREATION D'UNE CARTE ═══");
        
        System.out.println("\nType de carte:");
        System.out.println("1. Carte de debit");
        System.out.println("2. Carte de credit");
        System.out.println("3. Carte prepayee");
        
        int typeCarte = InputUtils.lireEntier("Choix: ");
        
        String idClient = InputUtils.lireChaine("ID du client: ");
        int dureeValidite = InputUtils.lireEntier("Duree de validite (annees): ");

        Carte carte = null;
        String id = UUID.randomUUID().toString();
        LocalDateTime dateExpiration = LocalDateTime.now().plusYears(dureeValidite);

        switch (typeCarte) {
            case 1:
                CarteDebit carteDebit = new CarteDebit();
                carteDebit.setId(id);
                carteDebit.setDateExpiration(dateExpiration);
                carteDebit.setStatus(StatutEnum.ACTIVE);
                carteDebit.setIdClient(idClient);
                carteDebit.setTypeCarte(CarteEnum.CARTEDEBIT);
                double plafondJour = InputUtils.lireDouble("Plafond journalier (€): ");
                carteDebit.setPlafond_journalier(plafondJour);
                carte = carteDebit;
                break;

            case 2:
                CarteCredit carteCredit = new CarteCredit();
                carteCredit.setId(id);
                carteCredit.setDateExpiration(dateExpiration);
                carteCredit.setStatus(StatutEnum.ACTIVE);
                carteCredit.setIdClient(idClient);
                carteCredit.setTypeCarte(CarteEnum.CARTECREDIT);
                double plafondMois = InputUtils.lireDouble("Plafond mensuel (EUR): ");
                float tauxInteret = InputUtils.lireFloat("Taux d'interet (%): ");
                carteCredit.setPlafond_mensuel(plafondMois);
                carteCredit.setTaux_interet(tauxInteret);
                carte = carteCredit;
                break;

            case 3:
                CartePrepayee cartePrepayee = new CartePrepayee();
                cartePrepayee.setId(id);
                cartePrepayee.setDateExpiration(dateExpiration);
                cartePrepayee.setStatus(StatutEnum.ACTIVE);
                cartePrepayee.setIdClient(idClient);
                cartePrepayee.setTypeCarte(CarteEnum.CARTEPREPAYEE);
                double solde = InputUtils.lireDouble("Solde initial (EUR): ");
                cartePrepayee.setSolde_disponible(solde);
                carte = cartePrepayee;
                break;

            default:
                InputUtils.afficherErreur("Type de carte invalide!");
                return;
        }

        try {
            carteService.createCarte(carte);
            InputUtils.afficherSucces("Carte creee avec succes!");
            System.out.println("ID: " + id);
            System.out.println("Numero: " + carte.getNumiro() + " (genere automatiquement)");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la création: " + e.getMessage());
        }
    }

    private void rechercherParId() {
        System.out.println("\n═══ RECHERCHE PAR ID ═══");
        
        String id = InputUtils.lireChaine("ID de la carte: ");

        try {
            Optional<Carte> carteOpt = carteService.readCard(id);
            if (carteOpt.isPresent()) {
                afficherDetailsCarte(carteOpt.get());
            } else {
                InputUtils.afficherInfo("Aucune carte trouvee avec cet ID.");
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    private void rechercherParClient() {
        System.out.println("\n═══ RECHERCHE PAR CLIENT ═══");
        
        String nomClient = InputUtils.lireChaine("Nom du client: ");

        try {
            Optional<Carte> carteOpt = carteService.findCardByClientName(nomClient);
            if (carteOpt.isPresent()) {
                afficherDetailsCarte(carteOpt.get());
            } else {
                InputUtils.afficherInfo("Aucune carte trouvee pour ce client.");
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    private void modifierStatut() {
        System.out.println("\n═══ MODIFICATION DU STATUT ═══");
        
        String id = InputUtils.lireChaine("ID de la carte: ");

        try {
            Optional<Carte> carteOpt = carteService.readCard(id);
            if (carteOpt.isEmpty()) {
                InputUtils.afficherErreur("Carte non trouvée.");
                return;
            }

            Carte carte = carteOpt.get();
            
            System.out.println("\nStatut actuel: " + carte.getStatus());
            System.out.println("\nNouveau statut:");
            System.out.println("1. ACTIVE");
            System.out.println("2. SUSPENDUE");
            System.out.println("3. BLOQUEE");
            
            int choix = InputUtils.lireEntier("Choix: ");
            
            StatutEnum nouveauStatut;
            switch (choix) {
                case 1: nouveauStatut = StatutEnum.ACTIVE; break;
                case 2: nouveauStatut = StatutEnum.SUSPENDUE; break;
                case 3: nouveauStatut = StatutEnum.BLOQUEE; break;
                default:
                    InputUtils.afficherErreur("Choix invalide!");
                    return;
            }

            carte.setStatus(nouveauStatut);
            carteService.updateCard(carte);
            InputUtils.afficherSucces("Statut modifie avec succes!");

        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la modification: " + e.getMessage());
        }
    }

    private void supprimerCarte() {
        System.out.println("\n═══ SUPPRESSION D'UNE CARTE ═══");
        
        String id = InputUtils.lireChaine("ID de la carte: ");

        try {
            Optional<Carte> carteOpt = carteService.readCard(id);
            if (carteOpt.isEmpty()) {
                InputUtils.afficherErreur("Carte non trouvee.");
                return;
            }

            if (InputUtils.confirmer("Etes-vous sur de vouloir supprimer cette carte?")) {
                Boolean supprime = carteService.deleteCarte(carteOpt.get());
                if (supprime) {
                    InputUtils.afficherSucces("Carte supprimee avec succes!");
                } else {
                    InputUtils.afficherErreur("Erreur lors de la suppression.");
                }
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void afficherDetailsCarte(Carte carte) {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       INFORMATIONS CARTE            │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ ID       : " + carte.getId());
        System.out.println("│ Numero   : " + carte.getNumiro());
        System.out.println("│ Type     : " + carte.getTypeCarte());
        System.out.println("│ Statut   : " + carte.getStatus());
        System.out.println("│ Expiration: " + carte.getDateExpiration());
        System.out.println("│ Client ID: " + carte.getIdClient());
        
        if (carte instanceof CarteDebit) {
            CarteDebit cd = (CarteDebit) carte;
            System.out.println("│ Plafond J: " + cd.getPlafond_journalier() + " EUR");
        } else if (carte instanceof CarteCredit) {
            CarteCredit cc = (CarteCredit) carte;
            System.out.println("│ Plafond M: " + cc.getPlafond_mensuel() + " EUR");
            System.out.println("│ Taux Int : " + cc.getTaux_interet() + " %");
        } else if (carte instanceof CartePrepayee) {
            CartePrepayee cp = (CartePrepayee) carte;
            System.out.println("│ Solde    : " + cp.getSolde_disponible() + " EUR");
        }
        
        System.out.println("└─────────────────────────────────────┘");
    }
}
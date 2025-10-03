package ui.menu;

import entity.AlerteFraude;
import exception.ServiceException;
import service.impl.FraudeService;
import ui.utils.InputUtils;

import java.util.List;

public class FraudeMenu {
    private final FraudeService fraudeService;

    public FraudeMenu(FraudeService fraudeService) {
        this.fraudeService = fraudeService;
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       DETECTION DE FRAUDE              ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Detecter les anomalies d'une carte");
            System.out.println("2. Verifier le depassement de plafond");
            System.out.println("3. Consulter les alertes d'une carte");
            System.out.println("4. Consulter les alertes d'un client");
            System.out.println("5. Générer une alerte manuelle");
            System.out.println("0. Retour au menu principal");
            System.out.println("─────────────────────────────────────────");

            int choix = InputUtils.lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    detecterAnomalies();
                    break;
                case 2:
                    verifierPlafond();
                    break;
                case 3:
                    consulterAlertesCarte();
                    break;
                case 4:
                    consulterAlertesClient();
                    break;
                case 5:
                    genererAlerteManuelle();
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

    private void detecterAnomalies() {
        System.out.println("\n═══ DETECTION D'ANOMALIES ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");

        try {
            System.out.println("\nAnalyse en cours...\n");
            fraudeService.detecterAnomalies(idCarte);
            InputUtils.afficherSucces("Analyse terminee! Consultez les resultats ci-dessus.");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void verifierPlafond() {
        System.out.println("\n═══ VERIFICATION DU PLAFOND ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");

        try {
            System.out.println("\nVerification en cours...\n");
            fraudeService.verifierDepassementPlafond(idCarte);
            InputUtils.afficherSucces("Verification terminee!");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void consulterAlertesCarte() {
        System.out.println("\n═══ ALERTES D'UNE CARTE ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");

        try {
            List<AlerteFraude> alertes = fraudeService.rechercherAlertesParCarte(idCarte);
            
            if (alertes.isEmpty()) {
                InputUtils.afficherInfo("Aucune alerte pour cette carte.");
            } else {
                afficherListeAlertes(alertes);
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void consulterAlertesClient() {
        System.out.println("\n═══ ALERTES D'UN CLIENT ═══");
        
        String idClient = InputUtils.lireChaine("ID du client: ");

        try {
            List<AlerteFraude> alertes = fraudeService.rechercherAlertesParClient(idClient);
            
            if (alertes.isEmpty()) {
                InputUtils.afficherInfo("Aucune alerte pour ce client.");
            } else {
                afficherListeAlertes(alertes);
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void genererAlerteManuelle() {
        System.out.println("\n═══ GENERATION D'ALERTE MANUELLE ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");
        String description = InputUtils.lireChaine("Description: ");
        
        System.out.println("\nNiveau d'alerte:");
        System.out.println("1. INFO");
        System.out.println("2. AVERTISSEMENT");
        System.out.println("3. CRITIQUE");
        
        int choix = InputUtils.lireEntier("Choix: ");
        
        String niveau;
        switch (choix) {
            case 1: niveau = "INFO"; break;
            case 2: niveau = "AVERTISSEMENT"; break;
            case 3: niveau = "CRITIQUE"; break;
            default:
                InputUtils.afficherErreur("Niveau invalide!");
                return;
        }

        try {
            fraudeService.genererAlerteFraude(idCarte, description, niveau);
            InputUtils.afficherSucces("Alerte generee avec succes!");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void afficherListeAlertes(List<AlerteFraude> alertes) {
        System.out.println("\n┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                        LISTE DES ALERTES                           │");
        System.out.println("├────────────────────────────────────────────────────────────────────┤");
        
        for (AlerteFraude alerte : alertes) {
            System.out.println("│ [" + alerte.niveau() + "]");
            System.out.println("│ ID: " + alerte.id());
            System.out.println("│ Carte: " + alerte.idCarte());
            System.out.println("│ Description: " + alerte.description());
            System.out.println("├────────────────────────────────────────────────────────────────────┤");
        }
        
        System.out.println("│ Total: " + alertes.size() + " alerte(s)");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        // Statistiques
        long critiques = alertes.stream().filter(a -> a.niveau().toString().equals("CRITIQUE")).count();
        long avertissements = alertes.stream().filter(a -> a.niveau().toString().equals("AVERTISSEMENT")).count();
        long infos = alertes.stream().filter(a -> a.niveau().toString().equals("INFO")).count();
        
        System.out.println("\nStatistiques:");
        System.out.println("   Critiques: " + critiques);
        System.out.println("   Avertissements: " + avertissements);
        System.out.println("   Infos: " + infos);
    }
}
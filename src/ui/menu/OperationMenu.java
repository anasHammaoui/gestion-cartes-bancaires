package ui.menu;

import entity.OperationCarte;
import entity.enums.OperationTypeEnum;
import exception.ServiceException;
import service.impl.OperationService;
import ui.utils.InputUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperationMenu {
    private final OperationService operationService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public OperationMenu(OperationService operationService) {
        this.operationService = operationService;
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GESTION DES OPERATIONS           ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Enregistrer un achat");
            System.out.println("2. Enregistrer un retrait");
            System.out.println("3. Enregistrer un paiement en ligne");
            System.out.println("4. Consulter les operations d'une carte");
            System.out.println("5. Consulter les operations d'un client");
            System.out.println("6. Filtrer les operations par type");
            System.out.println("7. Filtrer les operations par date");
            System.out.println("0. Retour au menu principal");
            System.out.println("─────────────────────────────────────────");

            int choix = InputUtils.lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    enregistrerAchat();
                    break;
                case 2:
                    enregistrerRetrait();
                    break;
                case 3:
                    enregistrerPaiement();
                    break;
                case 4:
                    consulterOperationsCarte();
                    break;
                case 5:
                    consulterOperationsClient();
                    break;
                case 6:
                    filtrerParType();
                    break;
                case 7:
                    filtrerParDate();
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

    private void enregistrerAchat() {
        System.out.println("\n═══ ENREGISTRER UN ACHAT ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");
        double montant = InputUtils.lireDouble("Montant (EUR): ");
        String lieu = InputUtils.lireChaine("Lieu (ex: Carrefour Paris): ");

        try {
            operationService.enregistrerAchat(idCarte, montant, lieu);
            InputUtils.afficherSucces("Achat enregistre avec succes!");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void enregistrerRetrait() {
        System.out.println("\n═══ ENREGISTRER UN RETRAIT ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");
        double montant = InputUtils.lireDouble("Montant (EUR): ");
        String lieu = InputUtils.lireChaine("Lieu (ex: ATM BNP Paribas): ");

        try {
            operationService.enregistrerRetrait(idCarte, montant, lieu);
            InputUtils.afficherSucces("Retrait enregistre avec succes!");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void enregistrerPaiement() {
        System.out.println("\n═══ ENREGISTRER UN PAIEMENT EN LIGNE ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");
        double montant = InputUtils.lireDouble("Montant (EUR): ");
        String lieu = InputUtils.lireChaine("Site/Service (ex: Amazon.fr): ");

        try {
            operationService.enregistrerPaiement(idCarte, montant, lieu);
            InputUtils.afficherSucces("Paiement enregistre avec succes!");
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void consulterOperationsCarte() {
        System.out.println("\n═══ OPÉRATIONS D'UNE CARTE ═══");
        
        String idCarte = InputUtils.lireChaine("ID de la carte: ");

        try {
            List<OperationCarte> operations = operationService.rechercherOperationsParCarte(idCarte);
            
            if (operations.isEmpty()) {
                InputUtils.afficherInfo("Aucune operation trouvee pour cette carte.");
            } else {
                afficherListeOperations(operations);
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void consulterOperationsClient() {
        System.out.println("\n═══ OPÉRATIONS D'UN CLIENT ═══");
        
        String idClient = InputUtils.lireChaine("ID du client: ");

        try {
            List<OperationCarte> operations = operationService.rechercherOperationsParClient(idClient);
            
            if (operations.isEmpty()) {
                InputUtils.afficherInfo("Aucune operation trouvee pour ce client.");
            } else {
                afficherListeOperations(operations);
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void filtrerParType() {
        System.out.println("\n═══ FILTRER PAR TYPE ═══");
        System.out.println("1. ACHAT");
        System.out.println("2. RETRAIT");
        System.out.println("3. PAIEMENT EN LIGNE");
        
        int choix = InputUtils.lireEntier("Type: ");
        
        OperationTypeEnum type;
        switch (choix) {
            case 1: type = OperationTypeEnum.ACHAT; break;
            case 2: type = OperationTypeEnum.RETRAIT; break;
            case 3: type = OperationTypeEnum.PAIEMENTENLIGNE; break;
            default:
                InputUtils.afficherErreur("Type invalide!");
                return;
        }

        try {
            List<OperationCarte> operations = operationService.filtrerParType(type);
            
            if (operations.isEmpty()) {
                InputUtils.afficherInfo("Aucune operation trouvee pour ce type.");
            } else {
                afficherListeOperations(operations);
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void filtrerParDate() {
        System.out.println("\n═══ FILTRER PAR DATE ═══");
        
        LocalDateTime dateDebut = InputUtils.lireDate("Date de debut");
        LocalDateTime dateFin = InputUtils.lireDate("Date de fin");

        try {
            List<OperationCarte> operations = operationService.filtrerParDate(dateDebut, dateFin);
            
            if (operations.isEmpty()) {
                InputUtils.afficherInfo("Aucune operation trouvee dans cette periode.");
            } else {
                afficherListeOperations(operations);
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur: " + e.getMessage());
        }
    }

    private void afficherListeOperations(List<OperationCarte> operations) {
        System.out.println("\n┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    LISTE DES OPÉRATIONS                        │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s %-15s %-10s %-20s │%n", "Date", "Type", "Montant", "Lieu");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        
        double total = 0;
        for (OperationCarte op : operations) {
            String date = op.date().format(FORMATTER);
            System.out.printf("│ %-20s %-15s %8.2f EUR %-20s │%n", 
                date, op.type(), op.montant(), 
                op.lieu().length() > 20 ? op.lieu().substring(0, 17) + "..." : op.lieu());
            total += op.montant();
        }
        
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ Total: %d operation(s) - Montant total: %.2f EUR%n", operations.size(), total);
        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }
}
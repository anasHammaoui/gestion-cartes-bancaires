package ui.menu;

import service.impl.CarteService;
import service.impl.ClientService;
import service.impl.FraudeService;
import service.impl.OperationService;
import ui.utils.InputUtils;

public class MenuPrincipal {
    private final ClientMenu clientMenu;
    private final CarteMenu carteMenu;
    private final OperationMenu operationMenu;
    private final FraudeMenu fraudeMenu;

    public MenuPrincipal(ClientService clientService, CarteService carteService, 
                        OperationService operationService, FraudeService fraudeService) {
        this.clientMenu = new ClientMenu(clientService);
        this.carteMenu = new CarteMenu(carteService);
        this.operationMenu = new OperationMenu(operationService);
        this.fraudeMenu = new FraudeMenu(fraudeService);
    }

    public void demarrer() {
        afficherBanniere();
        boolean continuer = true;

        while (continuer) {
            afficherMenuPrincipal();
            int choix = InputUtils.lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    clientMenu.afficher();
                    break;
                case 2:
                    carteMenu.afficher();
                    break;
                case 3:
                    operationMenu.afficher();
                    break;
                case 4:
                    fraudeMenu.afficher();
                    break;
                case 0:
                    if (InputUtils.confirmer("Etes-vous sur de vouloir quitter?")) {
                        continuer = false;
                        afficherAuRevoir();
                    }
                    break;
                default:
                    InputUtils.afficherErreur("Choix invalide!");
                    InputUtils.pause();
            }
        }
    }

    private void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║        SYSTEME DE GESTION DES CARTES BANCAIRES               ║");
        System.out.println("║                                                              ║");
        System.out.println("║                                                              ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      MENU PRINCIPAL                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("║                                                              ║");
        System.out.println("║  1. Gestion des Clients                                      ║");
        System.out.println("║  2. Gestion des Cartes                                       ║");
        System.out.println("║  3. Gestion des Operations                                   ║");
        System.out.println("║  4. Detection de Fraude                                      ║");
        System.out.println("║                                                              ║");
        System.out.println("║  0. Quitter                                                  ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private void afficherAuRevoir() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║              Merci d'avoir utilise notre systeme!            ║");
        System.out.println("║                      A bientot!                              ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
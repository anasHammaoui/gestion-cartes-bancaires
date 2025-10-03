package ui.menu;

import entity.Client;
import exception.ServiceException;
import service.impl.ClientService;
import ui.utils.InputUtils;

import java.util.Optional;
import java.util.UUID;

public class ClientMenu {
    private final ClientService clientService;

    public ClientMenu(ClientService clientService) {
        this.clientService = clientService;
    }

    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GESTION DES CLIENTS              ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Creer un client");
            System.out.println("2. Rechercher un client par nom");
            System.out.println("3. Rechercher un client par email");
            System.out.println("4. Supprimer un client");
            System.out.println("0. Retour au menu principal");
            System.out.println("─────────────────────────────────────────");

            int choix = InputUtils.lireEntier("Votre choix: ");

            switch (choix) {
                case 1:
                    creerClient();
                    break;
                case 2:
                    rechercherParNom();
                    break;
                case 3:
                    rechercherParEmail();
                    break;
                case 4:
                    supprimerClient();
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

    private void creerClient() {
        System.out.println("\n═══ CREATION D'UN CLIENT ═══");
        
        String nom = InputUtils.lireChaine("Nom: ");
        String email = InputUtils.lireChaine("Email: ");
        String phone = InputUtils.lireChaine("Telephone: ");

        String id = UUID.randomUUID().toString();
        Client client = new Client(id, nom, email, phone);

        try {
            clientService.createClient(client);
            InputUtils.afficherSucces("Client cree avec succes!");
            System.out.println("ID: " + id);
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la creation: " + e.getMessage());
        }
    }

    private void rechercherParNom() {
        System.out.println("\n═══ RECHERCHE PAR NOM ═══");
        
        String nom = InputUtils.lireChaine("Nom du client: ");

        try {
            Optional<Client> clientOpt = clientService.findClientByName(nom);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                afficherDetailsClient(client);
            } else {
                InputUtils.afficherInfo("Aucun client trouve avec ce nom.");
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    private void rechercherParEmail() {
        System.out.println("\n═══ RECHERCHE PAR EMAIL ═══");
        
        String email = InputUtils.lireChaine("Email du client: ");

        try {
            Optional<Client> clientOpt = clientService.findClientByEmail(email);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                afficherDetailsClient(client);
            } else {
                InputUtils.afficherInfo("Aucun client trouve avec cet email.");
            }
        } catch (ServiceException e) {
            InputUtils.afficherErreur("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    private void supprimerClient() {
        System.out.println("\n═══ SUPPRESSION D'UN CLIENT ═══");
        
        String id = InputUtils.lireChaine("ID du client: ");

        if (InputUtils.confirmer("Etes-vous sur de vouloir supprimer ce client?")) {
            try {
                Boolean supprime = clientService.deleteClient(id);
                if (supprime) {
                    InputUtils.afficherSucces("Client supprime avec succes!");
                } else {
                    InputUtils.afficherErreur("Client non trouve.");
                }
            } catch (ServiceException e) {
                InputUtils.afficherErreur("Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    private void afficherDetailsClient(Client client) {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       INFORMATIONS CLIENT           │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ ID       : " + client.id());
        System.out.println("│ Nom      : " + client.nom());
        System.out.println("│ Email    : " + client.email());
        System.out.println("│ Telephone: " + client.phone());
        System.out.println("└─────────────────────────────────────┘");
    }
}
import dao.impl.ClientDao;
import dao.impl.CarteDao;
import dao.impl.OperationDao;
import entity.Client;
import entity.CarteDebit;
import entity.OperationCarte;
import entity.enums.CarteEnum;
import entity.enums.StatutEnum;
import entity.enums.OperationTypeEnum;
import exception.ServiceException;
import service.impl.CarteService;
import service.impl.ClientService;
import service.impl.OperationService;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Main {

    public static void main(String[] args){
        ClientDao clientDao = new ClientDao();
        ClientService client = new ClientService(clientDao);

        Client newClient = new Client(UUID.randomUUID().toString(), "anas", "anas@gm.co", "15555558");
        
        // Carte CRUD:
        CarteDao carteDao = new CarteDao();
        CarteService carteService = new CarteService(carteDao);
        
        // Create a new debit card
        CarteDebit newCard = new CarteDebit();
        newCard.setId(UUID.randomUUID().toString());
        newCard.setDateExpiration(LocalDateTime.now().plusYears(3));
        newCard.setStatus(StatutEnum.ACTIVE);
        newCard.setIdClient("154a8958-40e1-4668-b628-d985e4f5f001");
        newCard.setTypeCarte(CarteEnum.CARTEDEBIT);
        newCard.setPlafond_journalier(1000.0);
        
        try {
            carteService.createCarte(newCard);
            System.out.println("Card created successfully!");
            System.out.println("Auto-generated card number: " + newCard.getNumiro());
            
            // // Read the card back
            // Optional<Carte> retrievedCard = carteService.readCard(newCard.getId());
            // if (retrievedCard.isPresent()) {
            //     System.out.println("Card found: " + retrievedCard.get().getNumiro());
            // }
            
            // // Update the card
            // newCard.setStatus(StatutEnum.SUSPENDUE);
            // Optional<Carte> updatedCard = carteService.updateCard(newCard);
            // if (updatedCard.isPresent()) {
            //     System.out.println("Card updated successfully!");
            // }
            
            // // Delete the card
            // Boolean deleted = carteService.deleteCarte(newCard);
            // System.out.println("Card deleted: " + deleted);
            
        } catch (ServiceException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Operation CRUD Example:
        /*
        OperationDao operationDao = new OperationDao();
        OperationService operationService = new OperationService(operationDao);
        
        try {
            // Register different types of operations
            operationService.enregistrerAchat(newCard.getId(), 50.0, "Supermarché Carrefour");
            operationService.enregistrerRetrait(newCard.getId(), 100.0, "ATM BNP Paribas");
            operationService.enregistrerPaiement(newCard.getId(), 25.99, "Amazon.fr");
            
            // Search operations by card
            List<OperationCarte> operationsByCard = operationService.rechercherOperationsParCarte(newCard.getId());
            System.out.println("Operations for card: " + operationsByCard.size());
            
            // Filter by type
            List<OperationCarte> achats = operationService.filtrerParType(OperationTypeEnum.ACHAT);
            System.out.println("Total purchases: " + achats.size());
            
            // Filter by date range
            LocalDateTime startDate = LocalDateTime.now().minusDays(1);
            LocalDateTime endDate = LocalDateTime.now().plusDays(1);
            List<OperationCarte> todayOperations = operationService.filtrerParDate(startDate, endDate);
            System.out.println("Today's operations: " + todayOperations.size());
            
        } catch (ServiceException e) {
            System.out.println("Operation Error: " + e.getMessage());
        }
        */

        // client crud
//        try {
//            client.createClient(newClient);
//        } catch (ServiceException e){
//            System.out.println(e.getMessage());
//        }

//        try {
//            System.out.println(client.deleteClient("49610dd5-f8ed-49d0-a097-fe78165b8c19"));
//        } catch(ServiceException e){
//            System.out.println("faild to delete:" + e.getMessage());
//        }

//        try {
//            Optional<Client> user = client.findClientByName("anas");
//            user.ifPresent(System.out::println);
//        } catch (ServiceException e){
//            System.out.println("faild to find client");
//        }
    }

}

import dao.impl.ClientDao;
import dao.impl.CarteDao;
import entity.Client;
import entity.Carte;
import entity.CarteDebit;
import entity.enums.CarteEnum;
import entity.enums.StatutEnum;
import exception.ServiceException;
import service.impl.CarteService;
import service.impl.ClientService;

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
        newCard.setNumiro("1234567890123456");
        newCard.setDateExpiration(LocalDateTime.now().plusYears(3));
        newCard.setStatus(StatutEnum.ACTIVE);
        newCard.setIdClient("154a8958-40e1-4668-b628-d985e4f5f001");
        newCard.setTypeCarte(CarteEnum.CARTEDEBIT);
        newCard.setPlafond_journalier(1000.0);
        
        try {
            carteService.createCarte(newCard);
            System.out.println("Card created successfully!");
            
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

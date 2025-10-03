package utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import entity.Carte;
import entity.CarteCredit;
import entity.CarteDebit;
import entity.CartePrepayee;
import entity.enums.CarteEnum;
import entity.enums.StatutEnum;

public class CarteUtils {
     public static Carte createCarteFromResultSet(ResultSet result) throws SQLException {
        String typeCarte = result.getString("typeCarte");
        CarteEnum carteType = CarteEnum.valueOf(typeCarte);
        
        Carte carte;
        switch (carteType) {
            case CARTEDEBIT:
                carte = new CarteDebit();
                ((CarteDebit) carte).setPlafond_journalier(result.getDouble("plafond_journalier"));
                break;
            case CARTECREDIT:
                carte = new CarteCredit();
                ((CarteCredit) carte).setPlafond_mensuel(result.getDouble("plafond_mensuel"));
                ((CarteCredit) carte).setTaux_interet(result.getFloat("taux_interet"));
                break;
            case CARTEPREPAYEE:
                carte = new CartePrepayee();
                ((CartePrepayee) carte).setSolde_disponible(result.getDouble("solde_disponible"));
                break;
            default:
                throw new SQLException("Unknown carte type: " + typeCarte);
        }

        // Set common properties
        carte.setId(result.getString("id"));
        carte.setNumiro(result.getString("numero"));
        
        // Handle date conversion
        Date dateExpiration = result.getDate("dateExpiration");
        if (dateExpiration != null) {
            carte.setDateExpiration(dateExpiration.toLocalDate().atStartOfDay());
        }
        
        carte.setStatus(StatutEnum.valueOf(result.getString("status")));
        carte.setIdClient(result.getString("user_id"));
        carte.setTypeCarte(carteType);
        
        return carte;
    }

    public static String generateCardNumber(CarteEnum carteType) {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        
        switch (carteType) {
            case CARTEDEBIT:
                cardNumber.append("4"); 
                break;
            case CARTECREDIT:
                cardNumber.append("5");
                break;
            case CARTEPREPAYEE:
                cardNumber.append("6");
                break;
            default:
                cardNumber.append("9"); 
        }
        
        for (int i = 1; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        return cardNumber.toString();
    }
}
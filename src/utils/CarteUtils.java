package utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
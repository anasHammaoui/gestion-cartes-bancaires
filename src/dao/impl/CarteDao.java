package dao.impl;

import dao.CarteInterface;
import dao.DbConnection;
import entity.Carte;
import entity.CarteCredit;
import entity.CarteDebit;
import entity.CartePrepayee;
import exception.DaoException;
import utils.CarteUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CarteDao implements CarteInterface {
    @Override
public void createCarte(Carte carte) throws DaoException {

    if (carte.getNumiro() == null || carte.getNumiro().isBlank()) {
        String generatedNumber = CarteUtils.generateCardNumber(carte.getTypeCarte());
        carte.setNumiro(generatedNumber);
        System.out.println("Generated card number: " + generatedNumber);
    }

    String query;
    switch (carte.getTypeCarte()) {
        case CARTEDEBIT:
            query = "INSERT INTO carte (id, numero, dateExpiration, typeCarte, status, user_id, plafond_journalier) VALUES (?,?,?,?,?,?,?)";
            break;
        case CARTECREDIT:
            query = "INSERT INTO carte (id, numero, dateExpiration, typeCarte, status, user_id, plafond_mensuel, taux_interet) VALUES (?,?,?,?,?,?,?,?)";
            break;
        case CARTEPREPAYEE:
            query = "INSERT INTO carte (id, numero, dateExpiration, typeCarte, status, user_id, solde_disponible) VALUES (?,?,?,?,?,?,?)";
            break;
        default:
            throw new DaoException("Type de carte non supporté");
    }
    try (Connection conn = DbConnection.getConnection()){
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, carte.getId());
        stmt.setString(2, carte.getNumiro());
        stmt.setDate(3, carte.getDateExpiration() != null ? Date.valueOf(carte.getDateExpiration().toLocalDate()) : null);
        stmt.setString(4, carte.getTypeCarte().toString());
        stmt.setString(5, carte.getStatus().toString());
        stmt.setString(6, carte.getIdClient());

        switch (carte.getTypeCarte()) {
            case CARTEDEBIT:
                CarteDebit carteDebit = (CarteDebit) carte;
                stmt.setDouble(7, carteDebit.getPlafond_journalier());
                break;
            case CARTECREDIT:
                CarteCredit carteCredit = (CarteCredit) carte;
                stmt.setDouble(7, carteCredit.getPlafond_mensuel());
                stmt.setDouble(8, carteCredit.getTaux_interet());
                break;
            case CARTEPREPAYEE:
                CartePrepayee cartePrepayee = (CartePrepayee) carte;
                stmt.setDouble(7, cartePrepayee.getSolde_disponible());
                break;
        }

        stmt.executeUpdate();
        System.out.println("carte created with success");
    } catch (SQLException e){
        throw new DaoException("failed to insert carte: " + e.getMessage());
    }
}

    @Override
    public Optional<Carte> readCard(String id) throws DaoException {
        String query = "SELECT * FROM carte WHERE id = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return Optional.of(CarteUtils.createCarteFromResultSet(result));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to get carte: " + e.getMessage());
        }
    }

    @Override
    public Optional<Carte> findCardByClientName(String name) throws DaoException {
        String query = "SELECT c.* FROM carte c INNER JOIN client cl ON c.user_id = cl.id WHERE cl.nom = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return Optional.of(CarteUtils.createCarteFromResultSet(result));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to get carte by client name: " + e.getMessage());
        }
    }

    @Override
    public Optional<Carte> updateCard(Carte carte) throws DaoException {
        String query;
        switch (carte.getTypeCarte()) {
            case CARTEDEBIT:
                query = "UPDATE carte SET numero=?, dateExpiration=?, typeCarte=?, status=?, user_id=?, plafond_journalier=? WHERE id=?";
                break;
            case CARTECREDIT:
                query = "UPDATE carte SET numero=?, dateExpiration=?, typeCarte=?, status=?, user_id=?, plafond_mensuel=?, taux_interet=? WHERE id=?";
                break;
            case CARTEPREPAYEE:
                query = "UPDATE carte SET numero=?, dateExpiration=?, typeCarte=?, status=?, user_id=?, solde_disponible=? WHERE id=?";
                break;
            default:
                throw new DaoException("Type de carte non supporté");
        }
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, carte.getNumiro());
            stmt.setDate(2, carte.getDateExpiration() != null ? Date.valueOf(carte.getDateExpiration().toLocalDate()) : null);
            stmt.setString(3, carte.getTypeCarte().toString());
            stmt.setString(4, carte.getStatus().toString());
            stmt.setString(5, carte.getIdClient());

            switch (carte.getTypeCarte()) {
                case CARTEDEBIT:
                    CarteDebit carteDebit = (CarteDebit) carte;
                    stmt.setDouble(6, carteDebit.getPlafond_journalier());
                    stmt.setString(7, carte.getId());
                    break;
                case CARTECREDIT:
                    CarteCredit carteCredit = (CarteCredit) carte;
                    stmt.setDouble(6, carteCredit.getPlafond_mensuel());
                    stmt.setDouble(7, carteCredit.getTaux_interet());
                    stmt.setString(8, carte.getId());
                    break;
                case CARTEPREPAYEE:
                    CartePrepayee cartePrepayee = (CartePrepayee) carte;
                    stmt.setDouble(6, cartePrepayee.getSolde_disponible());
                    stmt.setString(7, carte.getId());
                    break;
            }

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Carte updated with success");
                return readCard(carte.getId());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to update carte: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteCarte(Carte carte) throws DaoException {
        String query = "DELETE FROM carte WHERE id = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, carte.getId());
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete carte: " + e.getMessage());
        }
    }
}

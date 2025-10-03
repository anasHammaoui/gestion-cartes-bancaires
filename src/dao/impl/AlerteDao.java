package dao.impl;

import dao.AlerteInterface;
import dao.DbConnection;
import entity.AlerteFraude;
import entity.enums.AlertEnum;
import exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlerteDao implements AlerteInterface {

    @Override
    public void createAlerte(AlerteFraude alerte) throws DaoException {
        String query = "INSERT INTO alerte_fraude (id, description, niveau, idCarte) VALUES (?,?,?,?)";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, alerte.id());
            stmt.setString(2, alerte.description());
            stmt.setString(3, alerte.niveau().toString());
            stmt.setString(4, alerte.idCarte());
            stmt.executeUpdate();
            System.out.println("Alerte created with success");
        } catch (SQLException e) {
            throw new DaoException("Failed to insert alerte: " + e.getMessage());
        }
    }

    @Override
    public Optional<AlerteFraude> readAlerte(String id) throws DaoException {
        String query = "SELECT * FROM alerte_fraude WHERE id = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return Optional.of(createAlerteFromResultSet(result));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to get alerte: " + e.getMessage());
        }
    }

    @Override
    public Optional<AlerteFraude> updateAlerte(AlerteFraude alerte) throws DaoException {
        String query = "UPDATE alerte_fraude SET description=?, niveau=?, idCarte=? WHERE id=?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, alerte.description());
            stmt.setString(2, alerte.niveau().toString());
            stmt.setString(3, alerte.idCarte());
            stmt.setString(4, alerte.id());
            
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Alerte updated with success");
                return readAlerte(alerte.id());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to update alerte: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteAlerte(String id) throws DaoException {
        String query = "DELETE FROM alerte_fraude WHERE id = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete alerte: " + e.getMessage());
        }
    }

    @Override
    public List<AlerteFraude> findAlertesByCard(String cardId) throws DaoException {
        String query = "SELECT * FROM alerte_fraude WHERE idCarte = ? ORDER BY niveau DESC";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardId);
            ResultSet result = stmt.executeQuery();
            return createAlerteListFromResultSet(result);
        } catch (SQLException e) {
            throw new DaoException("Failed to get alertes by card: " + e.getMessage());
        }
    }

    @Override
    public List<AlerteFraude> findAlertesByClient(String clientId) throws DaoException {
        String query = "SELECT a.* FROM alerte_fraude a INNER JOIN carte c ON a.idCarte = c.id WHERE c.user_id = ? ORDER BY a.niveau DESC";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, clientId);
            ResultSet result = stmt.executeQuery();
            return createAlerteListFromResultSet(result);
        } catch (SQLException e) {
            throw new DaoException("Failed to get alertes by client: " + e.getMessage());
        }
    }

    /**
     * Helper method to create AlerteFraude from ResultSet
     */
    private AlerteFraude createAlerteFromResultSet(ResultSet result) throws SQLException {
        return new AlerteFraude(
            result.getString("id"),
            result.getString("description"),
            AlertEnum.valueOf(result.getString("niveau")),
            result.getString("idCarte")
        );
    }

    /**
     * Helper method to create list of AlerteFraude from ResultSet
     */
    private List<AlerteFraude> createAlerteListFromResultSet(ResultSet result) throws SQLException {
        List<AlerteFraude> alertes = new ArrayList<>();
        while (result.next()) {
            alertes.add(createAlerteFromResultSet(result));
        }
        return alertes;
    }
}
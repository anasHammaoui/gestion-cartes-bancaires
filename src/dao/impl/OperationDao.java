package dao.impl;

import dao.DbConnection;
import dao.OperationInterface;
import entity.OperationCarte;
import entity.enums.OperationTypeEnum;
import exception.DaoException;
import utils.OperationsUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OperationDao implements OperationInterface {

    @Override
    public void createOperation(OperationCarte operation) throws DaoException {
        String query = "INSERT INTO operation_carte (id, date, montant, type, lieu, idCarte) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, operation.id());
            stmt.setTimestamp(2, operation.date() != null ? Timestamp.valueOf(operation.date()) : null);
            stmt.setDouble(3, operation.montant());
            stmt.setString(4, operation.type().toString());
            stmt.setString(5, operation.lieu());
            stmt.setString(6, operation.idCarte());
            stmt.executeUpdate();
            System.out.println("Operation created with success");
        } catch (SQLException e) {
            throw new DaoException("Failed to insert operation: " + e.getMessage());
        }
    }

    @Override
    public Optional<OperationCarte> readOperation(String id) throws DaoException {
        String query = "SELECT * FROM operation_carte WHERE id = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return Optional.of(OperationsUtils.createOperationFromResultSet(result));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to get operation: " + e.getMessage());
        }
    }

    @Override
    public Optional<OperationCarte> updateOperation(OperationCarte operation) throws DaoException {
        String query = "UPDATE operation_carte SET date=?, montant=?, type=?, lieu=?, idCarte=? WHERE id=?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setTimestamp(1, operation.date() != null ? Timestamp.valueOf(operation.date()) : null);
            stmt.setDouble(2, operation.montant());
            stmt.setString(3, operation.type().toString());
            stmt.setString(4, operation.lieu());
            stmt.setString(5, operation.idCarte());
            stmt.setString(6, operation.id());
            
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Operation updated with success");
                return readOperation(operation.id());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to update operation: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteOperation(String id) throws DaoException {
        String query = "DELETE FROM operation_carte WHERE id = ?";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            int deleted = stmt.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete operation: " + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> findOperationsByCard(String cardId) throws DaoException {
        String query = "SELECT * FROM operation_carte WHERE idCarte = ? ORDER BY date DESC";
        return OperationsUtils.executeQueryForList(query, cardId);
    }

    @Override
    public List<OperationCarte> findOperationsByClient(String clientId) throws DaoException {
        String query = "SELECT o.* FROM operation_carte o INNER JOIN carte c ON o.idCarte = c.id WHERE c.user_id = ? ORDER BY o.date DESC";
        return OperationsUtils.executeQueryForList(query, clientId);
    }

    @Override
    public List<OperationCarte> findOperationsByType(OperationTypeEnum type) throws DaoException {
        String query = "SELECT * FROM operation_carte WHERE type = ? ORDER BY date DESC";
        return OperationsUtils.executeQueryForList(query, type.toString());
    }

    @Override
    public List<OperationCarte> findOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DaoException {
        String query = "SELECT * FROM operation_carte WHERE date BETWEEN ? AND ? ORDER BY date DESC";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet result = stmt.executeQuery();
            return OperationsUtils.createOperationListFromResultSet(result);
        } catch (SQLException e) {
            throw new DaoException("Failed to get operations by date range: " + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> findOperationsByCardAndType(String cardId, OperationTypeEnum type) throws DaoException {
        String query = "SELECT * FROM operation_carte WHERE idCarte = ? AND type = ? ORDER BY date DESC";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardId);
            stmt.setString(2, type.toString());
            ResultSet result = stmt.executeQuery();
            return OperationsUtils.createOperationListFromResultSet(result);
        } catch (SQLException e) {
            throw new DaoException("Failed to get operations by card and type: " + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> findOperationsByCardAndDateRange(String cardId, LocalDateTime startDate, LocalDateTime endDate) throws DaoException {
        String query = "SELECT * FROM operation_carte WHERE idCarte = ? AND date BETWEEN ? AND ? ORDER BY date DESC";
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardId);
            stmt.setTimestamp(2, Timestamp.valueOf(startDate));
            stmt.setTimestamp(3, Timestamp.valueOf(endDate));
            ResultSet result = stmt.executeQuery();
            return OperationsUtils.createOperationListFromResultSet(result);
        } catch (SQLException e) {
            throw new DaoException("Failed to get operations by card and date range: " + e.getMessage());
        }
    }

   
}
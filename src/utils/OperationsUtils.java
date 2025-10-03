package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.DbConnection;
import entity.OperationCarte;
import entity.enums.OperationTypeEnum;
import exception.DaoException;

public class OperationsUtils {

    public static List<OperationCarte> executeQueryForList(String query, String parameter) throws DaoException {
        try (Connection conn = DbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, parameter);
            ResultSet result = stmt.executeQuery();
            return createOperationListFromResultSet(result);
        } catch (SQLException e) {
            throw new DaoException("Failed to execute query: " + e.getMessage());
        }
    }

    public static OperationCarte createOperationFromResultSet(ResultSet result) throws SQLException {
        Timestamp timestamp = result.getTimestamp("date");
        LocalDateTime date = timestamp != null ? timestamp.toLocalDateTime() : null;
        
        return new OperationCarte(
            result.getString("id"),
            date,
            result.getDouble("montant"),
            OperationTypeEnum.valueOf(result.getString("type")),
            result.getString("lieu"),
            result.getString("idCarte")
        );
    }

    public static List<OperationCarte> createOperationListFromResultSet(ResultSet result) throws SQLException {
        List<OperationCarte> operations = new ArrayList<>();
        while (result.next()) {
            operations.add(createOperationFromResultSet(result));
        }
        return operations;
    }
}

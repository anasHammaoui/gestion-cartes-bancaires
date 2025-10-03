package dao;

import entity.OperationCarte;
import entity.enums.OperationTypeEnum;
import exception.DaoException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OperationInterface {
    void createOperation(OperationCarte operation) throws DaoException;
    Optional<OperationCarte> readOperation(String id) throws DaoException;
    Optional<OperationCarte> updateOperation(OperationCarte operation) throws DaoException;
    Boolean deleteOperation(String id) throws DaoException;
    
    // Search by card
    List<OperationCarte> findOperationsByCard(String cardId) throws DaoException;
    List<OperationCarte> findOperationsByClient(String clientId) throws DaoException;
    
    // Filtering by type and date
    List<OperationCarte> findOperationsByType(OperationTypeEnum type) throws DaoException;
    List<OperationCarte> findOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DaoException;
    List<OperationCarte> findOperationsByCardAndType(String cardId, OperationTypeEnum type) throws DaoException;
    List<OperationCarte> findOperationsByCardAndDateRange(String cardId, LocalDateTime startDate, LocalDateTime endDate) throws DaoException;
}
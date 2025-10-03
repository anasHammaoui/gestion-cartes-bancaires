package service;

import entity.OperationCarte;
import entity.enums.OperationTypeEnum;
import exception.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OperationServiceInterface {
    void createOperation(OperationCarte operation) throws ServiceException;
    Optional<OperationCarte> readOperation(String id) throws ServiceException;
    Optional<OperationCarte> updateOperation(OperationCarte operation) throws ServiceException;
    Boolean deleteOperation(String id) throws ServiceException;
    
    // Business operations
    void enregistrerPaiement(String cardId, double montant, String lieu) throws ServiceException;
    void enregistrerRetrait(String cardId, double montant, String lieu) throws ServiceException;
    void enregistrerAchat(String cardId, double montant, String lieu) throws ServiceException;
    
    // Search operations
    List<OperationCarte> rechercherOperationsParCarte(String cardId) throws ServiceException;
    List<OperationCarte> rechercherOperationsParClient(String clientId) throws ServiceException;
    List<OperationCarte> filtrerParType(OperationTypeEnum type) throws ServiceException;
    List<OperationCarte> filtrerParDate(LocalDateTime startDate, LocalDateTime endDate) throws ServiceException;
    List<OperationCarte> filtrerParCarteEtType(String cardId, OperationTypeEnum type) throws ServiceException;
    List<OperationCarte> filtrerParCarteEtDate(String cardId, LocalDateTime startDate, LocalDateTime endDate) throws ServiceException;
}
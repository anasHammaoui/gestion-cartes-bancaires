package service;

import entity.AlerteFraude;
import entity.OperationCarte;
import exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface FraudeServiceInterface {
    // Basic CRUD operations
    void createAlerte(AlerteFraude alerte) throws ServiceException;
    Optional<AlerteFraude> readAlerte(String id) throws ServiceException;
    Optional<AlerteFraude> updateAlerte(AlerteFraude alerte) throws ServiceException;
    Boolean deleteAlerte(String id) throws ServiceException;
    
    // Search operations
    List<AlerteFraude> rechercherAlertesParCarte(String cardId) throws ServiceException;
    List<AlerteFraude> rechercherAlertesParClient(String clientId) throws ServiceException;
    
    // Fraud detection operations
    void detecterAnomalies(String cardId) throws ServiceException;
    void verifierMontantEleve(OperationCarte operation, double seuilMontant) throws ServiceException;
    void verifierOperationsRapprochees(String cardId, int minutesInterval) throws ServiceException;
    void verifierDepassementPlafond(String cardId) throws ServiceException;
    
    // Alert generation
    void genererAlerteFraude(String cardId, String description, String niveau) throws ServiceException;
}
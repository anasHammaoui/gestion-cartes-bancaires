package service.impl;

import dao.impl.OperationDao;
import entity.OperationCarte;
import entity.enums.OperationTypeEnum;
import exception.DaoException;
import exception.ServiceException;
import service.OperationServiceInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OperationService implements OperationServiceInterface {
    private final OperationDao operationDao;

    public OperationService(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    @Override
    public void createOperation(OperationCarte operation) throws ServiceException {
        if (operation == null) {
            throw new IllegalArgumentException("Operation is required");
        }
        try {
            operationDao.createOperation(operation);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<OperationCarte> readOperation(String id) throws ServiceException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Operation ID is required");
        }
        try {
            return operationDao.readOperation(id);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<OperationCarte> updateOperation(OperationCarte operation) throws ServiceException {
        if (operation == null) {
            throw new IllegalArgumentException("Operation is required");
        }
        try {
            return operationDao.updateOperation(operation);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Boolean deleteOperation(String id) throws ServiceException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Operation ID is required");
        }
        try {
            return operationDao.deleteOperation(id);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void enregistrerPaiement(String cardId, double montant, String lieu) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (lieu == null || lieu.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        OperationCarte operation = new OperationCarte(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            montant,
            OperationTypeEnum.PAIEMENTENLIGNE,
            lieu,
            cardId
        );

        try {
            operationDao.createOperation(operation);
            System.out.println("Paiement enregistré avec succès: " + montant + "€ à " + lieu);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void enregistrerRetrait(String cardId, double montant, String lieu) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (lieu == null || lieu.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        OperationCarte operation = new OperationCarte(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            montant,
            OperationTypeEnum.RETRAIT,
            lieu,
            cardId
        );

        try {
            operationDao.createOperation(operation);
            System.out.println("Retrait enregistré avec succès: " + montant + "€ à " + lieu);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public void enregistrerAchat(String cardId, double montant, String lieu) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        if (montant <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (lieu == null || lieu.isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        OperationCarte operation = new OperationCarte(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            montant,
            OperationTypeEnum.ACHAT,
            lieu,
            cardId
        );

        try {
            operationDao.createOperation(operation);
            System.out.println("Achat enregistré avec succès: " + montant + "€ à " + lieu);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> rechercherOperationsParCarte(String cardId) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        try {
            return operationDao.findOperationsByCard(cardId);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> rechercherOperationsParClient(String clientId) throws ServiceException {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        try {
            return operationDao.findOperationsByClient(clientId);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> filtrerParType(OperationTypeEnum type) throws ServiceException {
        if (type == null) {
            throw new IllegalArgumentException("Operation type is required");
        }
        try {
            return operationDao.findOperationsByType(type);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> filtrerParDate(LocalDateTime startDate, LocalDateTime endDate) throws ServiceException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        try {
            return operationDao.findOperationsByDateRange(startDate, endDate);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> filtrerParCarteEtType(String cardId, OperationTypeEnum type) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Operation type is required");
        }
        try {
            return operationDao.findOperationsByCardAndType(cardId, type);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public List<OperationCarte> filtrerParCarteEtDate(String cardId, LocalDateTime startDate, LocalDateTime endDate) throws ServiceException {
        if (cardId == null || cardId.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        try {
            return operationDao.findOperationsByCardAndDateRange(cardId, startDate, endDate);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }
}
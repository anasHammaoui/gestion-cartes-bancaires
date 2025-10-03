package dao;

import entity.AlerteFraude;
import exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface AlerteInterface {
    void createAlerte(AlerteFraude alerte) throws DaoException;
    Optional<AlerteFraude> readAlerte(String id) throws DaoException;
    Optional<AlerteFraude> updateAlerte(AlerteFraude alerte) throws DaoException;
    Boolean deleteAlerte(String id) throws DaoException;
    
    // Search by card
    List<AlerteFraude> findAlertesByCard(String cardId) throws DaoException;
    List<AlerteFraude> findAlertesByClient(String clientId) throws DaoException;
}
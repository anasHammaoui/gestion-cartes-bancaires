package service;

import entity.Carte;
import exception.ServiceException;

import java.util.Optional;

public interface CarteServiceInterface {
    void createCarte(Carte carte) throws ServiceException;
    Optional<Carte> readCard(String id) throws ServiceException;
    Optional<Carte> findCardByClientName(String name) throws ServiceException;
    Optional<Carte> updateCard(Carte carte) throws ServiceException;
    Boolean deleteCarte(Carte carte) throws ServiceException;
}
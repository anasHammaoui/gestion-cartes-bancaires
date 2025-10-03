package dao;

import entity.Carte;
import exception.DaoException;

import java.util.Optional;

public interface CarteInterface {
    void createCarte(Carte carte) throws DaoException;
    Optional<Carte> readCard(String id) throws DaoException;
    Optional<Carte> findCardByClientName(String name) throws DaoException;
    Optional<Carte> updateCard(Carte carte) throws DaoException;
    Boolean deleteCarte(Carte id) throws DaoException;
}

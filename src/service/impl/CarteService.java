package service.impl;

import dao.impl.CarteDao;
import entity.Carte;
import exception.DaoException;
import exception.ServiceException;
import service.CarteServiceInterface;

import java.util.Optional;

public class CarteService implements CarteServiceInterface {
    private final CarteDao carteDao;

    public CarteService(CarteDao carteDao) {
        this.carteDao = carteDao;
    }

    @Override
    public void createCarte(Carte carte) throws ServiceException {
        if (carte == null) {
            throw new IllegalArgumentException("Carte is required");
        }
        try {
            carteDao.createCarte(carte);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<Carte> readCard(String id) throws ServiceException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Card ID is required");
        }
        try {
            return carteDao.readCard(id);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<Carte> findCardByClientName(String name) throws ServiceException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        try {
            return carteDao.findCardByClientName(name);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<Carte> updateCard(Carte carte) throws ServiceException {
        if (carte == null) {
            throw new IllegalArgumentException("Carte is required");
        }
        try {
            return carteDao.updateCard(carte);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Boolean deleteCarte(Carte carte) throws ServiceException {
        if (carte == null) {
            throw new IllegalArgumentException("Carte is required");
        }
        try {
            return carteDao.deleteCarte(carte);
        } catch (DaoException e) {
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }
}
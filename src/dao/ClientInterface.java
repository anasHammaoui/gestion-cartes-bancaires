package dao;

import entity.Client;
import exception.DaoException;

import java.util.Optional;

public interface ClientInterface {
    void createClient(Client client) throws DaoException;
    Optional<Client> findClientByName(String name) throws DaoException;
    Optional<Client> findClientByEmail(String email) throws DaoException;
    Boolean deleteClient(String id) throws DaoException;
}

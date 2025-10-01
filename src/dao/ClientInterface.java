package dao;

import entity.Client;
import exception.DaoException;

public interface ClientInterface {
    void createClient(Client client) throws DaoException;
    Client findClientByName(String name) throws DaoException;
    Client findClientByEmail(String email) throws DaoException;
    Boolean deleteClient(String id) throws DaoException;
}

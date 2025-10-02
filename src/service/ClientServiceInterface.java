package service;

import entity.Client;
import exception.ServiceException;

import java.util.Optional;

public interface ClientServiceInterface {
    void createClient(Client client) throws ServiceException;
    Optional<Client> findClientByName(String name) throws ServiceException;
    Optional<Client> findClientByEmail(String email) throws ServiceException;
    Boolean deleteClient(String id) throws ServiceException;
}

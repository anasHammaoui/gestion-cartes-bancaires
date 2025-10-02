package service.impl;

import dao.impl.ClientDao;
import entity.Client;
import exception.DaoException;
import exception.ServiceException;
import service.ClientServiceInterface;

import java.util.Optional;

public class ClientService implements ClientServiceInterface {
    private final ClientDao clientDao;

    public ClientService(ClientDao clientDao){
        this.clientDao = clientDao;
    }

    @Override
    public void createClient(Client client) throws ServiceException {
        if (client == null) {
            throw new IllegalArgumentException("Client is required");
        }
        try {
            clientDao.createClient(client);
        } catch (DaoException e){
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<Client> findClientByName(String name) throws ServiceException {
        if ( name == null || name.isBlank()){
            throw new IllegalArgumentException("Name is required");
        }
        try {
          return clientDao.findClientByName(name);
        } catch (DaoException e){
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Optional<Client> findClientByEmail(String email) throws ServiceException {
        if ( email == null || email.isBlank()){
            throw new IllegalArgumentException("Email is required");
        }
        try {
            return clientDao.findClientByEmail(email);
        } catch (DaoException e){
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }

    @Override
    public Boolean deleteClient(String id) throws ServiceException {
        if ( id == null || id.isBlank()){
            throw new IllegalArgumentException("id is required");
        }
        try {
            return clientDao.deleteClient(id);
        } catch (DaoException e){
            throw new ServiceException("Service Error:" + e.getMessage());
        }
    }
}

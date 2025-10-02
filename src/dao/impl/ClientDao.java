package dao.impl;

import dao.ClientInterface;
import dao.DbConnection;
import entity.Client;
import exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClientDao implements ClientInterface {

    @Override
    public void createClient(Client client) throws DaoException {
        if (client == null) {
            throw new IllegalArgumentException("Client is required");
        }
        String query = "INSTERT INTO client (id, nom, email, phone) VALUES (?,?,?,?)";
        try (Connection conn = DbConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,client.id());
            stmt.setString(2,client.nom());
            stmt.setString(3,client.email());
            stmt.setString(4,client.phone());
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DaoException("failed to insert client: " + e.getMessage());
        }
    }

    @Override
    public Optional<Client> findClientByName(String name) throws DaoException {

        String query = "SELECT * FROM client WHERE client.nom = ?";
        try(Connection conn = DbConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet result =  stmt.executeQuery();
            if (result.next()){
                Client client = new Client(
                        result.getString("id"),
                        result.getString("nom"),
                        result.getString("email"),
                        result.getString("phone")
                );
                return  Optional.of(client);
            }
            return  Optional.empty();
        } catch(SQLException e){
            throw new DaoException("Faild to get client: " + e.getMessage());
        }
    }

    @Override
    public Optional<Client> findClientByEmail(String email) throws DaoException {
        String query = "SELECT * FROM client WHERE client.email = ?";
        try(Connection conn = DbConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet result =  stmt.executeQuery();
            if (result.next()){
                Client client = new Client(
                        result.getString("id"),
                        result.getString("nom"),
                        result.getString("email"),
                        result.getString("phone")
                );
                return  Optional.of(client);
            }
            return  Optional.empty();
        } catch(SQLException e){
            throw new DaoException("Faild to get client: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteClient(String id) throws DaoException {
        String query = "DELETE FROM client WHERE client.id = ?";
        try(Connection conn = DbConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            int deleted =  stmt.executeUpdate();
            return  deleted > 0;
        } catch(SQLException e){
            throw new DaoException("Faild to delete client: " + e.getMessage());
        }
    }
}

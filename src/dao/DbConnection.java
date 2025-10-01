package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cartes";;
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            System.out.println("failed to connnect sql");
            return null;
        }
    }
}

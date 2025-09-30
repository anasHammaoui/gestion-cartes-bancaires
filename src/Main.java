import dao.DbConnection;

import java.sql.Connection;

public class Main {

    public static void main(String[] args){
        Connection connect = DbConnection.getConnection();
        System.out.println(connect);
    }

}

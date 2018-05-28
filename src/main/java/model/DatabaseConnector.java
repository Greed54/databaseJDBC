package model;

import utils.DialogManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Database";
    private static final String USER = "postgres";
    private static final String PASS = "aezakmi123";


    public static void connectionDataBase(){
        Connection connection;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            connection = getConnection();
        } catch (SQLException e){
            DialogManager.showErrorDialog("Нет соединения", "Нет соединения с базой!");
            e.printStackTrace();
            return;
        }

        if (connection != null){
            System.out.println("You successfully connected to database now");
        } else {
            DialogManager.showErrorDialog("Ошибка подключения", "Не удалось установить соединение с базой данных!");
        }

    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}

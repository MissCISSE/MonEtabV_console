package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonDataBase {

    private static final String URL = "jdbc:mysql://localhost:3306/etab_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    private SingletonDataBase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()) {
                new SingletonDataBase();
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'état de la connexion : " + e.getMessage());
        }
        return connection;
    }
}

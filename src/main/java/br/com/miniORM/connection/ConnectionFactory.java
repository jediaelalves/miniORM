package br.com.miniORM.connection;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class ConnectionFactory {

    private static final Properties props = new Properties();

    static {
        try (InputStream input =
                     ConnectionFactory.class
                             .getClassLoader()
                             .getResourceAsStream("application.properties")) {

            props.load(input);

            createDatabaseIfNotExists();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar propriedades", e);
        }
    }

    private static void createDatabaseIfNotExists() throws SQLException {

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // remove o nome do banco da URL
        String serverUrl = url.substring(0, url.lastIndexOf("/"));

        try (Connection conn =
                     DriverManager.getConnection(serverUrl, user, password);
             Statement stmt = conn.createStatement()) {

            String dbName = url.substring(url.lastIndexOf("/") + 1);

            stmt.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + dbName
            );
        }
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}

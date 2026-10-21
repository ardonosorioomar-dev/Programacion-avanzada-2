package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=AvanzadaII;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private static final String USER = leer("DB_USER", "sa");
    // La contraseña NO se escribe en el código: se lee de una variable de entorno
    // o de una propiedad de la JVM (-DDB_PASS=...). Ver README.md.
    private static final String PASS = leer("DB_PASS", "TU_CONTRASEÑA");

    private static Connection connection = null;

    private ConnectionDB() {}

    private static String leer(String nombre, String valorPorDefecto) {
        String valor = System.getProperty(nombre);
        if (valor == null) valor = System.getenv(nombre);
        return valor != null ? valor : valorPorDefecto;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("-> Conexión Exitosa a SQL");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: no se encontró el driver mssql-jdbc -> " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con SQL -> " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("-> La conexión con SQL está cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión -> " + e.getMessage());
        }
    }
}

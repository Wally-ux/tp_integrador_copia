package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import config.DataBaseConnection;

public class TestConexion {

    public static void main(String[] args) {
        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn != null) {
                System.out.println("✓ Conexión establecida con éxito a la base: " + conn.getCatalog());
            } else {
                System.out.println("X No se pudo establecer la conexión.");
            }

        } catch (SQLException e) {
            System.err.println("A Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    
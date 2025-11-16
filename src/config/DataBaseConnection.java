package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    // URL de conexión a la base de datos MySQL con el puerto 3306 y la base llamada "db"
    private static final String URL = "jdbc:mysql://localhost:3308/db_historia_clinica";
    // Usuario para la conexión
    private static final String USER = "root";
    // Contraseña del usuario
    private static final String PASSWORD = "307051";

    // Bloque estático para cargar el driver JDBC una sola vez al iniciar la clase
    static {
        try {
            // Se registra el driver MySQL en el DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Si no se encuentra el driver, lanza excepción en tiempo de ejecución
            throw new RuntimeException("Error: No se encontró el driver JDBC, ", e);
        }
    }

    // Método estático que devuelve una conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        // Validación simple para evitar URLs o credenciales vacías
        if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty() || PASSWORD == null || PASSWORD.isEmpty()) {
            throw new SQLException("Configuración de la base de datos incompleta o inválida");
        }
        // Se obtiene la conexión con DriverManager usando URL, usuario y contraseña
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import config.DataBaseConnection;

public class TestConexion {

    public static void main(String[] args) {
       /*      try (Connection conn = DataBaseConnection.getConnection()) {
            System.out.println("✓ Conexión OK a: " + conn.getCatalog());
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar: " + e.getMessage());
        }
    }
}
*/

       // Intentamos obtener una conexión con try-with-resources para que se cierre sola
        try (Connection conn = DataBaseConnection.getConnection()) {

            if (conn != null) {
                System.out.println("✓ Conexión establecida con éxito.");

                // Sentencia SQL para seleccionar todos los productos
                String sql = "SELECT * FROM producto";

                // Preparar la sentencia para prevenir inyección SQL y optimizar ejecución
                try (PreparedStatement pstmt = conn.prepareStatement(sql);

                     // Ejecutar la consulta y obtener el resultado
                     ResultSet rs = pstmt.executeQuery()) {

                    System.out.println("Listado de productos:");

                    // Recorrer el ResultSet fila por fila mientras haya registros
                    while (rs.next()) {
                        // Obtener los campos id, nombre y precio de cada fila
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        double precio = rs.getDouble("precio");

                        // Mostrar los datos por consola
                        System.out.println("ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio);
                    }
                }

            } else {
                // En caso que la conexión no se haya establecido correctamente
                System.out.println("X No se pudo establecer la conexión.");
            }

        } catch (SQLException e) {
            // Captura y muestra errores relacionados con la base de datos
            System.err.println("A Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}


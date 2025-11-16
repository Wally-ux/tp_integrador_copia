package DAO;

import Models.HistoriaClinica;
import Models.Paciente;
import config.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements DAO<Paciente> {

    private static final String INSERT_SQL = """
        INSERT INTO paciente
            (nombre, apellido, dni, fecha_nacimiento, historia_clinica_id, eliminado)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    private static final String UPDATE_SQL = """
        UPDATE paciente
           SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ?, 
               historia_clinica_id = ?, eliminado = ?
         WHERE id = ?
        """;

    // LOGIG delete
    private static final String SOFT_DELETE_SQL = """
        UPDATE paciente SET eliminado = 1 WHERE id = ?
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, nombre, apellido, dni, fecha_nacimiento, historia_clinica_id, eliminado
          FROM paciente
         WHERE id = ?
        """;

    // Solo activos (eliminado = 0)
    private static final String SELECT_ALL_SQL = """
        SELECT id, nombre, apellido, dni, fecha_nacimiento, historia_clinica_id, eliminado
          FROM paciente
         WHERE eliminado = 0
         ORDER BY apellido, nombre
        """;


    // CREATE

    @Override
    public void insertar(Paciente p) throws Exception {
        if (p == null) throw new IllegalArgumentException("paciente no puede ser null");

        try (Connection conn = DataBaseConnection.getConnection()) {
            System.out.println("Conectado a: " + conn.getCatalog());
            insertarInterno(p, conn);
        }
    }


    @Override
    public void insertTx(Paciente p, Connection conn) throws Exception {
        if (p == null) throw new IllegalArgumentException("paciente no puede ser null");
        if (conn == null) throw new IllegalArgumentException("conn no puede ser null");
        insertarInterno(p, conn);
    }


    //Metodo auxiliar para insercion de datos
    private void insertarInterno(Paciente p, Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getDni());
            ps.setDate(4, p.getFechaNacimiento() != null ? Date.valueOf(p.getFechaNacimiento()) : null);

                if (p.getHistoriaClinica() != null && p.getHistoriaClinica().getId() > 0) {
                    ps.setLong(5, p.getHistoriaClinica().getId());
                } else {
                    ps.setNull(5, Types.BIGINT);
                }

            ps.setBoolean(6, p.isEliminado()); // por default es 0

            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se insertó el paciente");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // UPDATE

    @Override
    public void actualizar(Paciente p) throws Exception {
        if (p == null) throw new IllegalArgumentException("paciente no puede ser null");
        if (p.getId() <= 0) throw new IllegalArgumentException("id inválido para actualizar");

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getDni());
            ps.setDate(4, p.getFechaNacimiento() != null ? Date.valueOf(p.getFechaNacimiento()) : null);


            //Validacion para corroborar que no tenga una historia clinica
            if (p.getHistoriaClinica() != null && p.getHistoriaClinica().getId() > 0) {
                ps.setLong(5, p.getHistoriaClinica().getId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }

            ps.setBoolean(6, p.isEliminado());
            ps.setLong(7, p.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se actualizó ninguna fila (id=" + p.getId() + ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // DELETE Logico
    @Override
    public void eliminar(long id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("id inválido");
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SOFT_DELETE_SQL)) {
            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se eliminó  ningún paciente (id=" + id + ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // READ

    //buscar por id
    @Override
    public Paciente getById(long id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("id inválido");
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Paciente> getAll() throws Exception {
        List<Paciente> list = new ArrayList<>();
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // =========================
    // Mapper
    // =========================
    private Paciente mapRow(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setDni(rs.getString("dni"));

        Date fn = rs.getDate("fecha_nacimiento");
        p.setFechaNacimiento(fn != null ? fn.toLocalDate() : null);

        long hcId = rs.getLong("historia_clinica_id");
        if (!rs.wasNull() && hcId > 0) {
            HistoriaClinica hc = new HistoriaClinica();
            hc.setId(hcId);
            p.setHistoriaClinica(hc);
        } else {
            p.setHistoriaClinica(null);
        }

        p.setEliminado(rs.getBoolean("eliminado"));

        return p;
    }
}

package DAO;

import Models.GrupoSanguineo;
import Models.HistoriaClinica;
import Models.Paciente;
import config.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinicaDAO implements DAO<HistoriaClinica> {
    
        private static final String INSERT_SQL = """
        INSERT INTO historia_clinica
            (eliminado, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    private static final String UPDATE_SQL = """
        UPDATE historia_clinica
           SET eliminado = ?, nro_historia = ?, grupo_sanguineo = ?, antecedentes = ?, 
               medicacion_actual = ?, observaciones = ?
         WHERE id = ?
        """;

    // LOGIG delete
    private static final String SOFT_DELETE_SQL = """
        UPDATE historia_clinica SET eliminado = 1 WHERE id = ?
        """;

    private static final String SELECT_BY_ID_SQL = """
        SELECT id, eliminado, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones
          FROM historia_clinica
         WHERE id = ?
        """;

    // Solo activos (eliminado = 0)
    private static final String SELECT_ALL_SQL = """
        SELECT id, eliminado, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones
          FROM historia_clinica
         WHERE eliminado = 0
         ORDER BY nro_historia
        """;
    
        private static final String SELECT_ALL_SQL_ID = """
        SELECT id, eliminado, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones
          FROM historia_clinica
         WHERE eliminado = 0
         ORDER BY id
        """;
        
    // CREATE

    @Override
    public void insertar(HistoriaClinica h) throws Exception {
        if (h == null) throw new IllegalArgumentException("historia clínica no puede ser null");

        try (Connection conn = DataBaseConnection.getConnection()) {
            System.out.println("Conectado a: " + conn.getCatalog());
            insertarInterno(h, conn);
        }
    }


    @Override
    public void insertTx(HistoriaClinica h, Connection conn) throws Exception {
        if (h == null) throw new IllegalArgumentException("Historia Clinica no puede ser null");
        if (conn == null) throw new IllegalArgumentException("conn no puede ser null");
        insertarInterno(h, conn);
    }


    //Metodo auxiliar para insercion de datos
    private void insertarInterno(HistoriaClinica h, Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, h.isEliminado());
            ps.setString(2, h.getNroHistoria());
            ps.setString(3, h.getGrupoSanguineo().toString());
            ps.setString(4, h.getAntecedentes());
            ps.setString(5, h.getMedicacionActual());
            ps.setString(6, h.getObservaciones());
            

            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No se insertó una historia clínica");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) h.setId(rs.getLong(1));
            }
        }
    }
    
        // UPDATE

    @Override
    public void actualizar(HistoriaClinica h) throws Exception {
        if (h == null) throw new IllegalArgumentException("historia clínica no puede ser null");
        if (h.getId() <= 0) throw new IllegalArgumentException("id inválido para actualizar");

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

               ps.setBoolean(1, h.isEliminado());
               ps.setString(2, h.getNroHistoria());
               ps.setString(3, h.getGrupoSanguineo().toString());
               ps.setString(4, h.getAntecedentes());
               ps.setString(5, h.getMedicacionActual());
               ps.setString(6, h.getObservaciones());
               ps.setLong(7, h.getId());  // importante para ir a buscarlo mediante el Where
            
               int rows = ps.executeUpdate(); //ejecuta el update en la base y devuelve un int con la cantidad de filas afectadas
               if (rows == 0) { //si no hay ninguna fila actualizada 
               throw new SQLException("No se actualizó ninguna historia clínica (id=" + h.getId() + ")");
        }
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
            if (rows == 0) throw new SQLException("No se eliminó  ninguna historia clínica (id=" + id + ")");
        }
    }
    
        // READ

    //buscar por id
    @Override
    public HistoriaClinica getById(long id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("id inválido");
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        }
    }

    @Override
    public List<HistoriaClinica> getAll() throws Exception {
        List<HistoriaClinica> list = new ArrayList<>();
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }
    
    public List<HistoriaClinica> getAllById() throws Exception {
        List<HistoriaClinica> list = new ArrayList<>();
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL_ID);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // =========================
    // Mapper
    // =========================
    private HistoriaClinica mapRow(ResultSet rs) throws SQLException {
        HistoriaClinica h = new HistoriaClinica();
        h.setId(rs.getLong("id"));
        h.setEliminado(rs.getBoolean("eliminado"));
        h.setNroHistoria(rs.getString("nro_historia"));
        h.setGrupoSanguineo(GrupoSanguineo.valueOf(rs.getString("grupo_sanguineo")));
        h.setAntecedentes(rs.getString("antecedentes"));
        h.setMedicacionActual(rs.getString("medicacion_actual"));
        h.setObservaciones(rs.getString("observaciones"));
        return h;
    }
    
}

package Service;

import java.util.List;

import DAO.HistoriaClinicaDAO;
import DAO.PacienteDAO;
import Models.HistoriaClinica;
import Models.Paciente;


/**
 * Servicio de negocio para la entidad Paciente.
 *
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio.
 *
 * Responsabilidades:
 * - Validar que los datos del paciente sean correctos antes de persistir
 * - Aplicar reglas de negocio (RN-201: nombre y DNI obligatorios)
 * - Delegar operaciones al DAO
 * - Transformar excepciones técnicas en errores de negocio comprensibles
 */
public class PacienteServiceImpl implements Service<Paciente> {

    /** DAO para acceso a datos de Paciente. */
    private final PacienteDAO pacienteDAO;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param pacienteDAO DAO para pacientes (no debe ser null)
     */
    public PacienteServiceImpl(PacienteDAO pacienteDAO) {
        if (pacienteDAO == null) {
            throw new IllegalArgumentException("PacienteDAO no puede ser null");
        }
        this.pacienteDAO = pacienteDAO;
    }

    /** Inserta un nuevo paciente. */
    @Override
    public void insertar(Paciente paciente) throws Exception {
        validatePaciente(paciente);
        pacienteDAO.insertar(paciente);
    }

    /** Actualiza los datos de un paciente existente. */
    @Override
    public void actualizar(Paciente paciente) throws Exception {
        validatePaciente(paciente);
        if (paciente.getId() <= 0) {
            throw new IllegalArgumentException("El ID del paciente debe ser mayor a 0");
        }
        pacienteDAO.actualizar(paciente);
    }



    /** Elimina lógicamente un paciente (soft delete). */
    @Override
    public void eliminar(Long id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        pacienteDAO.eliminar(id);
    }

    /** Obtiene un paciente por su ID.
     * @throws java.lang.Exception */

    @Override
    public Paciente getById(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El paciente no puede ser null");
        }

        if(id <= 0){
            throw new IllegalArgumentException("El ID  de paciente debe ser mayor a 0");
        }
        return pacienteDAO.getById(id);
    }

    /** Obtiene todos los pacientes activos. */
    @Override

    public List<Paciente> getAll() throws Exception {

        return pacienteDAO.getAll();
    }

    /** Valida los datos básicos del paciente antes de persistir. */
    private void validatePaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser null");
        }
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio");
        }
        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI del paciente es obligatorio");
        }
        // Puedes agregar más reglas de negocio aquí (edad mínima, teléfono válido, etc.)
    }
}

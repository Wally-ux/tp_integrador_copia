/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import Models.*;
import Service.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
/**
 *
 * @author agust
 */
public class AppMenu {
      // Servicios para manejar la lógica de gestión de paciente e historia clínica
    private final Service<Paciente> pacienteService;
    private final Service<HistoriaClinica> historiaService;

    // Scanner para leer desde la consola
    private final Scanner sc = new Scanner(System.in);

    // Constructor: recibe ambos servicios (paciente e historia clínica)
    public AppMenu(Service<Paciente> pacienteService,
                   Service<HistoriaClinica> historiaService) {
        this.pacienteService = pacienteService;
        this.historiaService = historiaService;
    }

    // Declaramos variable opcion
    public void iniciar() {
        int opcion = -1; // Inicializamos -1 para entrar al menú

        // mientras el usuario NO elija 0, seguimos mostrando el menú
        while (opcion != 0) {

            System.out.println("\n===== MENÚ CLÍNICA =====");
            System.out.println("1) Crear Paciente");
            System.out.println("2) Listar Pacientes");
            System.out.println("3) Buscar Paciente por DNI");
            System.out.println("4) Actualizar Paciente");
            System.out.println("5) Eliminar Paciente (lógico)");
            System.out.println("6) Crear Historia Clínica");
            System.out.println("7) Listar Historias Clínicas");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            try {
                // toma la opción como número
                opcion = Integer.parseInt(sc.nextLine());

                if (opcion == 1) crearPaciente();
                else if (opcion == 2) listarPacientes();
                else if (opcion == 3) buscarPacientePorDNI();
                else if (opcion == 4) actualizarPaciente();
                else if (opcion == 5) eliminarPaciente();
                else if (opcion == 6) crearHistoria();
                else if (opcion == 7) listarHistorias();
                else if (opcion == 0) System.out.println("Saliendo...");
                else System.out.println("Opción inválida.");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ======================
    // CRUD PACIENTE
    // ======================

    private void crearPaciente() throws Exception {
        System.out.println("\n--- Crear Paciente ---");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim().toUpperCase();

        System.out.print("Apellido: ");
        String apellido = sc.nextLine().trim().toUpperCase();

        System.out.print("DNI: ");
        String dni = sc.nextLine().trim();

        System.out.print("Fecha nacimiento (YYYY-MM-DD, vacío opcional): ");
        String fechaTxt = sc.nextLine().trim();
        LocalDate fecha = fechaTxt.isEmpty() ? null : LocalDate.parse(fechaTxt);

        Paciente p = new Paciente();
        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setDni(dni);
        p.setFechaNacimiento(fecha);
        p.setEliminado(false);
        p.setGrupoSanguineo(GrupoSanguineo.O_POS); // valor por defecto

        pacienteService.insertar(p);

        System.out.println("Paciente creado con ID: " + p.getId());
    }

    private void listarPacientes() throws Exception {
        System.out.println("\n--- Listado Pacientes ---");
        List<Paciente> lista = pacienteService.getAll();

        if (lista.isEmpty()) {
            System.out.println("No hay pacientes cargados.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private void buscarPacientePorDNI() throws Exception {
        System.out.println("\n--- Buscar Paciente por DNI ---");
        System.out.print("DNI: ");
        String dniBuscado = sc.nextLine().trim();

        List<Paciente> lista = pacienteService.getAll();
        Paciente encontrado = null;

        for (Paciente p : lista) {
            // Si todavía no encontré nada y coincide el DNI
            if (encontrado == null && p.getDni().equalsIgnoreCase(dniBuscado)) {
                encontrado = p;   // Lo guardo
            }
        }
        // Mostrar el resultado
        if (encontrado == null) {
            System.out.println("No existe paciente con DNI " + dniBuscado);
        } else {
            System.out.println("Paciente encontrado:");
            System.out.println(encontrado);
        }
    }

    private void actualizarPaciente() throws Exception {
        System.out.println("\n--- Actualizar Paciente ---");

        System.out.print("ID del paciente: ");
        long id = Long.parseLong(sc.nextLine());

        Paciente p = pacienteService.getById(id);

        if (p == null) {
            System.out.println("No existe un paciente con ese ID.");
            return;
        }

        System.out.print("Nuevo nombre (" + p.getNombre() + "): ");
        String nombre = sc.nextLine().trim();
        if (!nombre.isEmpty()) p.setNombre(nombre.toUpperCase());

        System.out.print("Nuevo apellido (" + p.getApellido() + "): ");
        String apellido = sc.nextLine().trim();
        if (!apellido.isEmpty()) p.setApellido(apellido.toUpperCase());

        pacienteService.actualizar(p);
        System.out.println("✔️ Paciente actualizado.");
    }

    private void eliminarPaciente() throws Exception {
        System.out.println("\n--- Eliminar Paciente (Lógico) ---");

        System.out.print("ID del paciente: ");
        long id = Long.parseLong(sc.nextLine());

        pacienteService.eliminar(id);
        System.out.println("✔️ Paciente eliminado (lógico).");
    }

    // ======================
    // CRUD HISTORIA CLÍNICA
    // ======================

    private void crearHistoria() throws Exception {
        System.out.println("\n--- Crear Historia Clínica ---");

        // Mostrar pacientes para que el usuario vea los IDs
        System.out.println("Pacientes disponibles:");
        listarPacientes();  // usa tu opción 2 internamente

        // 1) Elegir paciente al que se le va a crear la HC
        System.out.print("ID del paciente: ");
        long idPaciente = Long.parseLong(sc.nextLine());

        Paciente paciente = pacienteService.getById(idPaciente);
        if (paciente == null) {
            System.out.println("✗ No existe un paciente con ese ID.");
            return;
        }

        // 2) Verificar que NO tenga ya una historia clínica (relación 1→1)
        if (paciente.getHistoriaClinica() != null) {
            System.out.println("✗ El paciente ya tiene una historia clínica asociada (ID HC = "
                    + paciente.getHistoriaClinica().getId() + ")");
            return;
        }

        // 3) Pedir datos de la historia clínica
        System.out.print("Número historia (ej: HC001): ");
        String numero = sc.nextLine().trim().toUpperCase();

        System.out.print("Grupo sanguíneo (ej: O_POS): ");
        String gsTxt = sc.nextLine().trim().toUpperCase();

        GrupoSanguineo grupo;
        try {
            grupo = GrupoSanguineo.valueOf(gsTxt); // debe coincidir con el enum
        } catch (IllegalArgumentException e) {
            System.out.println("Grupo sanguíneo inválido, se usará O_POS por defecto.");
            grupo = GrupoSanguineo.O_POS;
        }

        System.out.print("Antecedentes (vacío = SIN ANTECEDENTES): ");
        String antecedentes = sc.nextLine().trim();
        if (antecedentes.isEmpty()) antecedentes = "SIN ANTECEDENTES";

        System.out.print("Medicación actual (vacío = NINGUNA): ");
        String medicacion = sc.nextLine().trim();
        if (medicacion.isEmpty()) medicacion = "NINGUNA";

        System.out.print("Observaciones (vacío = NINGUNA): ");
        String observaciones = sc.nextLine().trim();
        if (observaciones.isEmpty()) observaciones = "NINGUNA";

        // 4) Crear objeto HistoriaClinica
        HistoriaClinica h = new HistoriaClinica(
                numero,
                grupo,
                antecedentes,
                medicacion,
                observaciones
        );
        h.setEliminado(false);

        // 5) Guardar la historia clínica en la BD
        historiaService.insertar(h);

        // 6) Asociar la HC al paciente (relación 1→1 unidireccional)
        paciente.setHistoriaClinica(h);
        pacienteService.actualizar(paciente);

        System.out.println("✔️ Historia creada con ID: " + h.getId()
                + " y asociada al paciente " + paciente.getNombre() + " " + paciente.getApellido());
    }

    private void listarHistorias() throws Exception {
        System.out.println("\n--- Listado Historias Clínicas ---");
        List<HistoriaClinica> lista = historiaService.getAll();

        if (lista.isEmpty()) {
            System.out.println("No hay historias cargadas.");
            return;
        }
        lista.forEach(System.out::println);
    }
}
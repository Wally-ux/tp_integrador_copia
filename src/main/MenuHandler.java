/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import Models.Paciente;
import Service.PacienteServiceImpl;
import java.util.List;
import java.util.Scanner;



/**
 *
 * @author agust
 */
public class MenuHandler {
    private final Scanner scanner;
    
    private final PacienteServiceImpl pacienteService;
    
  public MenuHandler(Scanner scanner, PacienteServiceImpl pacienteService) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        if (pacienteService == null) {
            throw new IllegalArgumentException("PersonaService no puede ser null");
        }
        this.scanner = scanner;
        this.pacienteService = pacienteService;
    }
  
         public void crearPaciente() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();
            System.out.print("DNI: ");
            String dni = scanner.nextLine().trim();

          

           Paciente paciente = new Paciente(nombre, apellido, dni);
            
            pacienteService.insertar(paciente);
            System.out.println("Persona creada exitosamente con ID: " + paciente.getId());
        } catch (Exception e) {
            System.err.println("Error al crear persona: " + e.getMessage());
        }
    }
            public void listarPacientes() {
     try {
        // Obtenemos todos los pacientes
        List<Paciente> pacientes = pacienteService.getAll();

        if (pacientes == null || pacientes.isEmpty()) {
            System.out.println("No se encontraron personas.");
            return;
        }

        for (Paciente p : pacientes) {
            System.out.println(
                    "ID: " + p.getId()
                    + ", Nombre: " + p.getNombre()
                    + ", Apellido: " + p.getApellido()
                    + ", DNI: " + p.getDni()
            );
        }

    } catch (Exception e) {
        System.err.println("Error al listar pacientes: " + e.getMessage());
    }
}
            
         public void actualizarPaciente() {
            try {
            System.out.print("ID del paciente a actualizar: ");
            Long id = Long.parseLong(scanner.nextLine());   // ID como Long
            Paciente p = pacienteService.getById(id);

            if (p == null) {
                System.out.println("Paciente no encontrada.");
                return;
            }

            System.out.print("Nuevo nombre (actual: " + p.getNombre() + ", Enter para mantener): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                p.setNombre(nombre);
            }

            System.out.print("Nuevo apellido (actual: " + p.getApellido() + ", Enter para mantener): ");
            String apellido = scanner.nextLine().trim();
            if (!apellido.isEmpty()) {
                p.setApellido(apellido);
            }

            System.out.print("Nuevo DNI (actual: " + p.getDni() + ", Enter para mantener): ");
            String dni = scanner.nextLine().trim();
            if (!dni.isEmpty()) {
                p.setDni(dni);
            }
            }catch (NumberFormatException e) {
                System.err.println("ID inválido. Debe ser un número.");
            } catch (Exception e) {
                System.err.println("Error al actualizar paciente: " + e.getMessage());
             }
}
            

             public void eliminarPaciente() {
            try {
            System.out.print("ID de la paciente a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());   // ID como Long
            pacienteService.eliminar(id);
            System.out.println("Paciente eliminada exitosamente.");
            } catch (Exception e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
        }
    }
  }


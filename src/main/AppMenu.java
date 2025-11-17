/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;



import java.util.Scanner;
import DAO.PacienteDAO;
import Service.PacienteServiceImpl;
import main.MenuHandler;
/**
 *
 * @author agust
 */
       public  class AppMenu {
      // Servicios para manejar la lógica de gestión de paciente e historia clínica
      private final Scanner scanner;
      private final MenuHandler menuHandler;
      private boolean running;
     
        // Constructor
       
         public AppMenu() {
            this.scanner = new Scanner(System.in);

       
        
         PacienteDAO pacienteDAO = new PacienteDAO();
         PacienteServiceImpl pacienteService = new PacienteServiceImpl(pacienteDAO);

        // Le pasamos scanner y service al manejador del menú
        this.menuHandler = new MenuHandler(scanner, pacienteService);
        this.running = true;
    }
         // Punto de entrada del programa
        public  static void main(String[] args) {
            AppMenu app = new AppMenu();
            app.run();
        }
        // Bucle principal del menú
         public void run() {
             while(running){
                try {
                    MenuDisplay.mostrarMenuPrincipal();
                    int opcion= Integer.parseInt(scanner.nextLine());
                   
                    processOption(opcion);
                } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                }
            }
             scanner.close();
        }
        

         private void processOption(int opcion) {
            switch (opcion) {
            case 1 -> menuHandler.crearPaciente();
            case 2 -> menuHandler.listarPacientes();
            case 3 -> menuHandler.actualizarPaciente();
            case 4 -> menuHandler.eliminarPaciente();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
            }
            }
    }